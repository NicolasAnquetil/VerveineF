package fr.inria.verveine.extractor.fortran;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.RecognitionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranLexicalPrepass;
import fortran.ofp.parser.java.FortranParser;
import fortran.ofp.parser.java.FortranParser2008;
import fortran.ofp.parser.java.FortranTokenStream;
import fortran.ofp.parser.java.IFortranParser;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTNode;
import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ParserActionAST;
import fr.inria.verveine.extractor.fortran.visitors.CommentVisitor;
import fr.inria.verveine.extractor.fortran.visitors.InvokAccessVisitor;
import fr.inria.verveine.extractor.fortran.visitors.ScopeDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.SubprgDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.UseModuleVisitor;
import fr.inria.verveine.extractor.fortran.visitors.VarDefVisitor;

public class VerveineFParser  {
	public static final String STRING_SOURCE_PATH = "/no/path/";
	public static final String STRING_SOURCE_FILENAME = "-source-from-string-";

	public static final String VERVEINEF_PARSER_ACTION = "fr.inria.verveine.extractor.fortran.parser.ast.ParserActionAST";

	private static final String VERVEINEF_VERSION = "0.1.0_20190119-withintrinsics";

	protected IRDictionary dico;
	protected ASTNode ast;
	protected Options options;

	public static void main(String[] args) {
		new VerveineFParser().run(args);
	}

	public VerveineFParser() {
		this.dico = new IRDictionary();
		this.ast = null;
		options = new Options();
	}

	protected void run(String[] args) {
		setOptions(args);
		parseSources();
		outputIR();
	}

	public void parseSources() {
		String filename = null;

		for (String src : options.getSourcesToParse()) {
			if (options.sourceIsString()) {
				VerveineFortranStream stream = null;
				try {
					stream = new VerveineFortranStream(options.getMacros(), src, options.FREE_FORM);
				} catch (IOException e) {
					// should not occur, there is no reason for VerveineFortranStream to fail on a string source
				}
				filename = STRING_SOURCE_FILENAME;
				ast = parseStream(filename, STRING_SOURCE_PATH, stream);
			}
			else {
				filename = src;
				ast = parseFile(new File(src));
			}
			if (ast != null) {
				runAllVisitors(filename, ast);
			}
		}

	}

	/**
	 * Parses one file
	 */
	public ASTNode parseFile( File file) {
		VerveineFortranStream stream = null;

		if (! file.exists()) {
			System.err.println("Error: " + file.getName() + " not found");
		}
		else if (file.isDirectory()) {
			System.err.println("Not accepting directories as argument at this point");
			return null;
		}
		String parentDir = file.getParent();
		if ( (parentDir == null) || parentDir.equals("") ) {
			options.addIncludeDirs(".");
		}
		else {
			options.addIncludeDirs(parentDir);
		}

		try {
			stream = new VerveineFortranStream(options.getMacros(), file.getAbsolutePath() );
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return parseStream(file.getName(), file.getAbsolutePath(), stream);
	}
		
	/**
	 * Parses source code in a stream<br>
	 * Note: it may contain several CompilationUnits
	 * 
	 * The parser likes that source streams have a filename and file path
	 */
	public ASTNode parseStream( String filename, String path, VerveineFortranStream inputStream) {
		int sourceForm;
		FortranParser parser;
		FortranLexer lexer;
		FortranLexicalPrepass prepass;
		FortranTokenStream tokens;
		
		lexer = new FortranLexer(inputStream);

		// Changes associated with antlr version 3.3 require that includeDirs
		// be set here as the tokens are loaded by the constructor.
		lexer.setIncludeDirs((ArrayList<String>) options.getIncludeDirs());
		tokens = new FortranTokenStream(lexer);

		parser = new FortranParser2008(tokens);
		parser.initialize(/*ActionArgs*/null, VERVEINEF_PARSER_ACTION, filename, path);

		prepass = new FortranLexicalPrepass(lexer, tokens, parser);
		sourceForm = inputStream.getSourceForm();

		// determine whether the file is fixed or free form and
		// set the source form in the prepass so it knows how to handle lines.
		prepass.setSourceForm(sourceForm);

		// apply Sale's algorithm to the tokens to allow keywords
		// as identifiers. also, fixup labeled do's, etc.
		prepass.performPrepass();

		// overwrite the old token stream with the (possibly) modified one
		tokens.finalizeTokenStream();

		// parse each program unit in a given file
		while (tokens.LA(1) != FortranLexer.EOF) {
			if (! parseCompilationUnit(lexer, tokens, parser)) {
				System.out.println("Parsing failed for: "+filename);
				return null;
			}
		} 

		// Call the end_of_file action here so that it comes after the
		// end_program_stmt occurs.
		parser.eofAction();

		return ((ParserActionAST)parser.getAction()).getAST();
	}

	/**
	 * Parses one compilation unit<br>
	 * One of:
	 * <ul>
	 * <li> main program;
	 * <li> module;
	 * <li> subroutine;
	 * <li> function;
	 * <li> submodule;
	 * <li> block data.
	 * </ul>
	 */
	protected boolean parseCompilationUnit(FortranLexer lexer, FortranTokenStream tokens, IFortranParser parser) {
		int firstToken;
		int lookAhead = 1;

		// check for opening with an include file
		parser.checkForInclude();

		// first token on the *line*. will check to see if it's
		// equal to module, block, etc. to determine what rule of
		// the grammar to start with.
		try {
			lookAhead = 1;
			do {
				firstToken = tokens.LA(lookAhead);
				lookAhead++;
			} while (firstToken == FortranLexer.LINE_COMMENT || firstToken == FortranLexer.T_EOS);

			// attempt to match the program unit
			// lookahead gave us a token that should indicate what's the proper parse
			// (see also performPrepass()
			if (firstToken != FortranLexer.EOF) {
				// CER (2011.10.18): Module is now (F2008) a prefix-spec so
				// must look for subroutine and functions before module stmts.
				// Part of fix for bug 3425005.
				if (tokens.lookForToken(FortranLexer.T_SUBROUTINE) == true) {
					// try matching a subroutine
					parser.subroutine_subprogram();
				}
				else if (tokens.lookForToken(FortranLexer.T_FUNCTION) == true) {
					// try matching a function
					parser.ext_function_subprogram();
				}
				else if (firstToken == FortranLexer.T_MODULE
						&& tokens.LA(lookAhead) != FortranLexer.T_PROCEDURE) {
					// try matching a module
					parser.module();
				}
				else if (firstToken == FortranLexer.T_SUBMODULE) {
					// try matching a submodule
					parser.submodule();
				}
				else if ( firstToken == FortranLexer.T_BLOCKDATA
						|| (firstToken == FortranLexer.T_BLOCK
						&& tokens.LA(lookAhead) == FortranLexer.T_DATA)) {
					// try matching block data
					parser.block_data();
				}
				else {
					// what's left should be a main program
					parser.main_program();
				} // end else(unhandled token)
			} // end if(file had nothing but comments empty)
		} catch (RecognitionException e) {
			e.printStackTrace();
			return false; // i.e. error
		}

		return ! parser.hasErrorOccurred();  // return true if no error
	}


	protected void runAllVisitors(String filename, ASTNode ast)  {
		ast.accept(new ScopeDefVisitor(dico, filename, options));
		ast.accept(new SubprgDefVisitor(dico, filename, options));
		ast.accept(new VarDefVisitor(dico, filename, options));

		ast.accept(new CommentVisitor(dico, filename, options));

		ast.accept(new UseModuleVisitor(dico, filename, options));
		ast.accept(new InvokAccessVisitor(dico, filename, options));
	}

	protected void outputIR() {
		GsonBuilder gsonBldr = new GsonBuilder();
		gsonBldr.registerTypeAdapter(IREntity.class, new GSonIREntitySerializer());
		Gson gsonSerializer = gsonBldr.create();
		
		try {
			FileWriter fout = new FileWriter(options.getOutputFileName());
			for (IREntity ent : dico) {
				fout.append(gsonSerializer.toJson(ent));
				fout.append('\n');
			}
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void setOptions(String[] args) {
		options.setOptions(args);
	}

	protected void version() {
		System.out.println("VerveineF version:"+VERVEINEF_VERSION);
		System.exit(0);
	}


	public IRDictionary getDico() {
		return dico;
	}

	public IASTNode getAst() {
		return ast;
	}

}
