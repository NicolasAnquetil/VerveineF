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

	public static final String STRING_SOURCE_OPTION = "--source";

	public static final String DEFAULT_OUTPUT_FILENAME = "output.ir";

	public static final String ALLLOCALS_OPTION = "--alllocals";

	public static final String VERVEINEF_PARSER_ACTION = "fr.inria.verveine.extractor.fortran.parser.ast.ParserActionAST";

	private static final String VERVEINEF_VERSION = "0.1.0_20190101-IR";

	// possible forms of Fortran code
	public static final int FIXED_FORM = 2;
	public static final int FREE_FORM = 1;
	public static final int UNKNOWN_SOURCE_FORM = -1;

	/**
	 * Directory where the project to analyze is located
	 */
	protected String[] sourcesToParse;
	
	protected boolean sourceIsString;

	protected IRDictionary dico;

	/**
	 * Temporary variable to gather macros defined from the command line
	 */
	protected Map<String,String> macros;

	/**
	 * Temporary variable to gather include dirs defined from the command line
	 */
	protected Collection<String> includeDirs;

	protected String outputFileName;
	
	/**
	 * Whether to output all local variables 
	 */
	protected boolean allLocals;

	protected ASTNode ast;

	public static void main(String[] args) {
		VerveineFParser verveine = new VerveineFParser();
		verveine.setOptions(args);
		verveine.parseSources();
		verveine.outputIR();
	}

	public VerveineFParser() {
		this.macros = new HashMap<String,String>();
		this.includeDirs = new ArrayList<>();
		this.sourcesToParse = null;
		this.sourceIsString = false;
		this.outputFileName = DEFAULT_OUTPUT_FILENAME;
		this.dico = new IRDictionary();
		this.allLocals = false;
		this.ast = null;
	}

	public void parseSources() {
		String filename = null;

		for (String src : sourcesToParse) {
			if (sourceIsString) {
				VerveineFortranStream stream = null;
				try {
					stream = new VerveineFortranStream(macros, src, FREE_FORM);
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
			includeDirs.add(".");
		}
		else {
			includeDirs.add(parentDir);
		}

		try {
			stream = new VerveineFortranStream(macros, file.getAbsolutePath() );
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
		lexer.setIncludeDirs((ArrayList<String>) includeDirs);
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
		ast.accept(new ScopeDefVisitor(dico, filename, allLocals));
		ast.accept(new SubprgDefVisitor(dico, filename, allLocals));
		ast.accept(new VarDefVisitor(dico, filename, allLocals));

		ast.accept(new CommentVisitor(dico, filename, allLocals));

		ast.accept(new UseModuleVisitor(dico, filename, allLocals));
		ast.accept(new InvokAccessVisitor(dico, filename, allLocals));
	}

	protected void outputIR() {
		GsonBuilder gsonBldr = new GsonBuilder();
		gsonBldr.registerTypeAdapter(IREntity.class, new GSonIREntitySerializer());
		Gson gsonSerializer = gsonBldr.create();
		
		try {
			FileWriter fout = new FileWriter(outputFileName);
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
		this.sourceIsString = false;  // default is to parse files

		int i = 0;
		while (i < args.length && (! sourceIsString) && args[i].trim().startsWith("-")) {
		    String arg = args[i++].trim();

			if (arg.equals("-h")) {
				usage();
			}
			else if (arg.equals("-v")) {
				version();
			}
			else if (arg.equals("-o")) {
				if (i < args.length) {
					outputFileName = args[i];
					i++;
				} else {
					System.err.println("-o requires a filename");
				}
			}
			else if (arg.startsWith("-I")) {
				includeDirs.add(arg.substring(2));   // remove -I from argument, the rest is the include dir
			}
			else if (arg.startsWith("-D")) {
				parseMacroDefinition(arg.substring(2));
			}
			else if (arg.equals(STRING_SOURCE_OPTION)) {
				this.sourceIsString = true;
				// after this option, everything should be Fortran source code
			}
			else if (arg.equals(ALLLOCALS_OPTION)) {
				this.allLocals = true;
			}
			else {
				System.err.println("** Unrecognized option: " + arg);
				usage();
			}
		}

		sourcesToParse = new String[args.length - i];
		for (int j=0 ; i < args.length; j++, i++) {
			sourcesToParse[j] = args[i];
		}
		
		if (sourcesToParse == null) {
			System.err.println("Nos project directory set");
			usage();
		}
	}

	protected void parseMacroDefinition(String arg) {
		int i;
		String macro;
		String value;

		i = arg.indexOf('=');
		if (i < 0) {
			macro=arg;
			value = "";
		}
		else {
			macro = arg.substring(0, i);
			value = arg.substring(i+1);
		}
		macros.put(macro, value);
	}

	protected void usage() {
		System.err.println("Usage: VerveineF [options] <Fortran project directory>");
		System.err.println("Recognized options:");
		System.err.println("      -h: prints this message");
		System.err.println("      -v: prints the version");
		System.err.println("      -o <output-file-name>: changes the name of the output file (default: output.mse)");
		System.err.println("      -D<macro>: defines a C/C++ macro");
		System.err.println("      ["+ALLLOCALS_OPTION+"] Forces outputing all local variables, even those with primitive type (incompatible with \"-summary\"");
		System.err.println("      <Fortran project directory>: directory containing the Fortran project to export in MSE");
		System.exit(0);
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
