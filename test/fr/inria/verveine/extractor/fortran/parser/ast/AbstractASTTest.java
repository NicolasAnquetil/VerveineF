package fr.inria.verveine.extractor.fortran.parser.ast;

import fr.inria.verveine.extractor.fortran.AbstractFortranExtractorTest;
import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

public abstract class AbstractASTTest extends AbstractFortranExtractorTest {

	protected IASTNode ast;

	protected void parseCode(String[] args) {
		super.parseCode(args);
		ast = parser.getAst();
	}

}