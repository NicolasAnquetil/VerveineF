package fr.inria.verveine.extractor.fortran;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fortran.ofp.FrontEnd;
import fr.inria.verveine.extractor.fortran.ast.ASTNode;
import fr.inria.verveine.extractor.fortran.ast.ParserActionAST;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.visitors.CommentVisitor;
import fr.inria.verveine.extractor.fortran.visitors.InvokAccessVisitor;
import fr.inria.verveine.extractor.fortran.visitors.ScopeDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.SubprgDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.VarDefVisitor;

public class VerveineFParser  {
	public static final String ALLLOCALS_OPTION = "--alllocals";

	public static final String VERVEINEF_PARSER_ACTION = "fr.inria.verveine.extractor.fortran.ast.ParserActionAST";

	private static final String VERVEINEF_VERSION = "0.1.0_201801201-IR";

	/**
	 * Directory where the project to analyze is located
	 */
	private String userProjectDir = null;
	
	/**
	 * AST generated by the parser
	 */
	private ASTNode ast = null;

	protected IRDictionary dico;

	/**
	 * Temporary variable to gather macros defined from the command line
	 */
	private Map<String,String> argDefined;

	/**
	 * Temporary variable to gather include dirs defined from the command line
	 */
	private Collection<String> includeDirs;

	private String outputFileName;

	/**
	 * Whether to output all local variables (even those with primitive type or not (default is not).<br>
	 * Note: allLocals => not classSummary
	 */
	private boolean allLocals;

	public static void main(String[] args) {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions(args);
		parser.parse();
		parser.outputIR();
	}

	public VerveineFParser() {
		this.argDefined = new HashMap<String,String>();
		includeDirs = new ArrayList<>();
		userProjectDir = null;
		outputFileName = "output.ir";
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

	public void parse() {
		FrontEnd ofpParser = null;
		dico = new IRDictionary();

		try {
			ofpParser = createParser();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			ofpParser.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ast = ((ParserActionAST)ofpParser.getParser().getAction()).getAST();
		runAllVisitors( dico, userProjectDir, ast);
	}

	protected FrontEnd createParser() throws IOException {
		return new FrontEnd(/*args*/new String[] {}, userProjectDir, VERVEINEF_PARSER_ACTION);
	}

	private void runAllVisitors(IRDictionary dico, String filename, ASTNode ast)  {
		ast.accept(new ScopeDefVisitor(dico, filename, allLocals));
		ast.accept(new SubprgDefVisitor(dico, filename, allLocals));
		ast.accept(new VarDefVisitor(dico, filename, allLocals));

		ast.accept(new CommentVisitor(dico, filename, allLocals));

		ast.accept(new InvokAccessVisitor(dico, filename, allLocals));
	}

	public void setOptions(String[] args) {
		int i = 0;
		while (i < args.length && args[i].trim().startsWith("-")) {
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
				parseMacroDefinition(arg);
			}
			else if (arg.equals(ALLLOCALS_OPTION)) {
				this.allLocals = true;
			}
			else {
				System.err.println("** Unrecognized option: " + arg);
				usage();
			}
		}

		for ( ; i < args.length; i++) {
			userProjectDir = args[i];
		}
		
		if (userProjectDir == null) {
			System.err.println("Nos project directory set");
			usage();
		}
	}

	private void parseMacroDefinition(String arg) {
		int i;
		String macro;
		String value;

		i = arg.indexOf('=');
		if (i < 0) {
			macro=arg.substring(2);  // remove '-D' at the beginning
			value = "";
		}
		else {
			macro = arg.substring(2, i);
			value = arg.substring(i+1);
		}
		argDefined.put(macro, value);
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
	
	public ASTNode getAst() {
		return ast;
	}

	public void setAst(ASTNode ast) {
		this.ast = ast;
	}

}
