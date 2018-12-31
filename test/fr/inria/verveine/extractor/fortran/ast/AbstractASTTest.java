package fr.inria.verveine.extractor.fortran.ast;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractASTTest {

	protected IASTNode ast;

	protected void parseCode(String sourceCode) {
		parseCode(new String[] {"--stdinput",sourceCode});
	}

	protected void parseCode(String[] args, String sourceCode) {
		int i;
		String[] newArgs = new String[args.length + 2];
		for (i=0; i < args.length; i++) {
			newArgs[i] = args[i];
		}
		newArgs[i++] = "--stdinput";
		newArgs[i++] = sourceCode;
		parseCode(newArgs);
	}

	protected void parseCode(String[] args) {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions(args);
		parser.parse(args);
		ast = parser.getAst();
	}

}