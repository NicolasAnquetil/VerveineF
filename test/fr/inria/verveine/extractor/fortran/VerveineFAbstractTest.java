package fr.inria.verveine.extractor.fortran;


import ch.akuhn.fame.Repository;
import fr.inria.verveine.extractor.fortran.ast.ASTNode;

public class VerveineFAbstractTest {

	protected ASTNode ast;
	
	protected Repository repo;

	public void setup(String filename) throws Exception {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions( new String[] {filename});
		parser.parse();
		repo = parser.getFamixRepo();
		ast = parser.getAst();
	}

}