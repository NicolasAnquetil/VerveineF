package fr.inria.verveine.extractor.fortran.ir;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractIRTest {

	protected IRDictionary dico;

	protected void parseCode(String sourceCode) {
		parseCode( new String[] {"--stdinput",sourceCode});
	}

	protected void parseCode(String[] args) {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions(args);
		parser.parse(args);
		dico = parser.getDico();
	}

}
