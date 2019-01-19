package fr.inria.verveine.extractor.fortran;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Options {

	public static final String STRING_SOURCE_OPTION = "--source";
	public static final String DEFAULT_OUTPUT_FILENAME = "output.ir";
	
	public static final String VERBOSE_OPTION = "--verbose";
	public static final String SILENT_OPTION = "--silent";
	public static final String ALLLOCALS_OPTION = "--alllocals";
	public static final String WITHINTRINSICS_OPTION = "--withintrinsics";


	// possible forms of Fortran code
	public static final int FIXED_FORM = 2;
	public static final int FREE_FORM = 1;
	public static final int UNKNOWN_SOURCE_FORM = -1;

	// levels of "verbosity"
	public static final int TRACE_NOTHING = 0;
	public static final int TRACE_VISITORS = 1;
	public static final int TRACE_ENTITIES = 2;

	/** command line option: Directory where the project to analyze is located
	 */
	protected String[] sourcesToParse;
	
	/** command line option: Whether the source is a string or contained in a file (which name is given)
	 */
	protected boolean sourceIsString;

	/** command line option: Temporary variable to gather macros defined from the command line
	 */
	protected Map<String,String> macros;

	/** command line option: Temporary variable to gather include dirs defined from the command line
	 */
	protected Collection<String> includeDirs;

	/** command line option: Name of the file where to put the result see also {@link #DEFAULT_OUTPUT_FILENAME}
	 */
	protected String outputFileName;

	/** command line option: whether to trace the parsing process
	 */
	protected int verbose;
	
	/** command line option: Whether to output all local variables 
	 */
	protected boolean allLocals;

	/** command line option: Whether to output references to intrinsic functions/subroutines names
	 */
	protected boolean withIntrinsics;

	public Options() {
		this.macros = new HashMap<String,String>();
		this.includeDirs = new ArrayList<>();
		this.sourcesToParse = null;
		this.sourceIsString = false;
		this.outputFileName = DEFAULT_OUTPUT_FILENAME;
		this.allLocals = false;
		this.verbose = TRACE_NOTHING;
		this.withIntrinsics = false;
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
//				version();
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
			else if (arg.equals(VERBOSE_OPTION)) {
				if (verbose < TRACE_ENTITIES) {
					verbose++;
				}
			}
			else if (arg.equals(SILENT_OPTION)) {
				if (verbose > TRACE_NOTHING) {
					verbose--;
				}
			}
			else if (arg.equals(WITHINTRINSICS_OPTION)) {
				withIntrinsics = true;
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
		System.err.println("      "+VERBOSE_OPTION+": increment verbosity level by 1: 0=silent; 1=advertise visitor execution; 2=advertise entity creation");
		System.err.println("      "+SILENT_OPTION+":  decrement verbosity level by 1 (see --verbose)");
		System.err.println("      "+ALLLOCALS_OPTION+": Forces outputing all local variables, even those with primitive type");
		System.err.println("      "+WITHINTRINSICS_OPTION+" Forces outputing all references to Fortran intrinsics functions/subroutines");
		System.err.println("      <Fortran project directory>: directory containing the Fortran project to export in MSE");
		System.exit(0);
	}

	
	public String[] getSourcesToParse() {
		return sourcesToParse;
	}

	public boolean sourceIsString() {
		return sourceIsString;
	}

	public Map<String, String> getMacros() {
		return macros;
	}

	public Collection<String> getIncludeDirs() {
		return includeDirs;
	}

	public void addIncludeDirs(String dir) {
		includeDirs.add(dir);
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public int getVerbose() {
		return verbose;
	}

	public boolean withAllLocals() {
		return allLocals;
	}

	public boolean withIntrinsics() {
		return withIntrinsics;
	}

}
