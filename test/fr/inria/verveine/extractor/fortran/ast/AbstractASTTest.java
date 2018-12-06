package fr.inria.verveine.extractor.fortran.ast;

import java.io.IOException;

import fortran.ofp.FrontEnd;
import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractASTTest {

	protected IASTNode ast;

	public void setup(String filename) {
		FrontEnd ofpParser = null;

		try {
			ofpParser = new FrontEnd(/*args*/new String[] {}, filename, VerveineFParser.VERVEINE_AST_BUILDER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ofpParser.call();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ast = ((ParserActionAST)ofpParser.getParser().getAction()).getAST();

	}

}