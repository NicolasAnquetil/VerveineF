package fr.inria.verveine.extractor.fortran.ast;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractASTTest {

	protected IASTNode ast;

	protected void parseCode(String sourceCode) {
		String[] args = new String[] {"--stdinput",sourceCode};
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions(args);
		parser.parse(args);
		ast = parser.getAst();
	}

}