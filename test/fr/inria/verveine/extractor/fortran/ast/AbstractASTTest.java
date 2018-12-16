package fr.inria.verveine.extractor.fortran.ast;

import java.io.IOException;

import fortran.ofp.FrontEnd;
import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractASTTest {

	protected IASTNode ast;

	public void setup(String filename) {
		FrontEnd ofpParser = null;

		try {
			ofpParser = new FrontEnd(/*args*/new String[] {}, filename, VerveineFParser.VERVEINEF_PARSER_ACTION);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			ofpParser.call();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ast = ((ParserActionAST)ofpParser.getParser().getAction()).getAST();
	}

}