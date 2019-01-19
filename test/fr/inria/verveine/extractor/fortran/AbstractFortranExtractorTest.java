package fr.inria.verveine.extractor.fortran;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractFortranExtractorTest {

	protected VerveineFParser parser;

	protected void parseCode(String sourceCode) {
		parseCode(new String[] {} , sourceCode);
	}

	protected void parseCode(String[] args, String sourceCode) {
		int i = 0;
		String newArgs[] = new String[ args.length+2 ];
		
		for (i=0; i<args.length; i++) {
			newArgs[i] = args[i];
		}
		newArgs[i] = Options.STRING_SOURCE_OPTION;
		i++;
		newArgs[i] = sourceCode;
		
		parseCode(newArgs);
	}

	protected void parseCode(String[] args) {
		parser = new VerveineFParser();
		parser.setOptions(args);
		parser.parseSources();
	}

}
