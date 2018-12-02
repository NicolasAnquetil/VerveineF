package fr.inria.verveine.extractor.fortran.ast;

import fortran.ofp.FrontEnd;
import fr.inria.verveine.extractor.fortran.VerveineFParser;
import fr.inria.verveine.extractor.fortran.ast.ASTNode;

public class AbstractASTTest {

	protected ASTNode ast;

	public void setup(String filename) throws Exception {
		FrontEnd ofpParser = null;

		ofpParser = new FrontEnd(/*args*/new String[] {}, filename, VerveineFParser.VERVEINE_AST_BUILDER);
		ofpParser.call();

		ast = ((FortranParserActionAST)ofpParser.getParser().getAction()).getAST();

	}

}