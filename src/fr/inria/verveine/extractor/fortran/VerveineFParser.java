package fr.inria.verveine.extractor.fortran;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eu.synectique.verveine.core.VerveineParser;
import eu.synectique.verveine.core.gen.famix.FortranSourceLanguage;
import eu.synectique.verveine.core.gen.famix.SourceLanguage;
import fortran.ofp.FrontEnd;
import fr.inria.verveine.extractor.fortran.ast.ASTNode;
import fr.inria.verveine.extractor.fortran.ast.FortranParserActionAST;
import fr.inria.verveine.extractor.fortran.visitors.VarDefVisitor;

public class VerveineFParser extends VerveineParser {

	private static final String VERVEINEF_VERSION = "0.4.0_20180731";

	/**
	 * Dictionary used to create all entities. Contains a Famix repository
	 */
	private FDictionary dico = null;

	/**
	 * Directory where the project to analyze is located
	 */
	private String userProjectDir = null;

	/**
	 * Temporary variable to gather macros defined from the command line
	 */
	private Map<String,String> argDefined;

	private FortranSourceLanguage srcLggeInstance;

	public static void main(String[] args) {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions(args);
		parser.parse();
		parser.emitMSE();
	}

	public VerveineFParser() {
		dico = new FDictionary(getFamixRepo());
		this.argDefined = new HashMap<String,String>();
		userProjectDir = null;
	}

	public boolean parse() {
		FrontEnd ofpParser = null;
		ASTNode ast = null;

		if (linkToExisting()) {
			// incremental parsing ...
		}

		try {
			ofpParser = new FrontEnd(/*args*/new String[] {}, userProjectDir, "fr.inria.verveine.extractor.fortran.ast.FortranParserActionAST");
			ofpParser.call();
			
			ast = ((FortranParserActionAST)ofpParser.getParser().getAction()).getAST();
			runAllVisitors( dico, ast);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private void runAllVisitors(FDictionary dico, ASTNode ast)  {
		//ast.accept(new ScopeDefVisitor(dico));
		//ast.accept(new SubprgDefVisitor(dico));
		ast.accept(new VarDefVisitor(dico));
		//ast.accept(new CommentVisitor(dico));

		//ast.accept(new InvokAccessVisitor(dico));
	}

	@Override
	protected SourceLanguage getMyLgge() {
		if (srcLggeInstance == null) {
			srcLggeInstance = new FortranSourceLanguage();
		}
		return srcLggeInstance;
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
			else if (arg.startsWith("-D")) {
				parseMacroDefinition(arg);
			}
			else {
				int j = super.setOption(i - 1, args);
				if (j > 0) {     // j is the number of args consumed by super.setOption()
					i += j;      // advance by that number of args
					i--;         // 1 will be added at the beginning of the loop ("args[i++]")
				}
				else {
					System.err.println("** Unrecognized option: " + arg);
					usage();
				}
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
		System.err.println("      <Fortran project directory>: directory containing the Fortran project to export in MSE");
		System.exit(0);
	}

	protected void version() {
		System.out.println("VerveineF version:"+VERVEINEF_VERSION);
		System.exit(0);
	}

}
