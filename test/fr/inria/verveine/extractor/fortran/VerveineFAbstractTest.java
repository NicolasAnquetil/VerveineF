package fr.inria.verveine.extractor.fortran;


import fortran.ofp.FrontEnd;
import fr.inria.verveine.extractor.fortran.VerveineFParser;
import fr.inria.verveine.extractor.fortran.ast.ASTNode;
import fr.inria.verveine.extractor.fortran.ast.FortranParserActionAST;

public class VerveineFAbstractTest {

	protected ASTNode ast;

	public void setup(String filename) throws Exception {
		FrontEnd ofpParser = new FrontEnd(/*args*/new String[] {}, filename, VerveineFParser.VERVEINE_AST_BUILDER);
		ofpParser.call();
			
		ast = ((FortranParserActionAST)ofpParser.getParser().getAction()).getAST();
	}

}