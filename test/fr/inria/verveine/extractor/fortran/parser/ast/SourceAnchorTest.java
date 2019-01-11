package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;


public class SourceAnchorTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "module a_module\n" +	// '\n' at pos 15
			"integer :: VAR1\n" +									// '\n' at pos 31
			"integer :: &\n" +										// '\n' at pos 44
			"     VAR2\n" +											// '\n' at pos 54
			"integer :: VAR3\n" +									// '\n' at pos 70
			"integer :: &\n" +										// '\n' at pos 83
			"     VAR4\n" +											// '\n' at pos 93
			"end module\n";											// '\n' at pos 104

	@Test
	public void testAnchorModule() {
		parseCode(SOURCE_CODE);

		Collection<ASTModuleNode> mods = ast.findAll(ASTModuleNode.class);
		assertEquals(1, mods.size());

		ASTModuleNode mod = mods.iterator().next(); 
		assertEquals(0, mod.findFirstToken().getStartIndex());
		assertEquals(104, mod.findLastToken().getStopIndex());   // EOS token
	}


	@Test
	public void testAnchorVars() {
		parseCode(SOURCE_CODE);

		Collection<ASTTypeDeclarationStmtNode> vars = ast.findAll(ASTTypeDeclarationStmtNode.class);
		assertEquals(4, vars.size());

/* helps debugging
		for (ASTTypeDeclarationStmtNode decl : vars) {
			ASTEntityDeclNode var = decl.getEntityDeclList().iterator().next();
			System.out.println(var.getObjectName().getText() + ": "+
					var.findFirstToken().getStartIndex()  + " / "+
					var.findLastToken().getStopIndex());
			}
*/

		for (ASTTypeDeclarationStmtNode decl : vars) {
			ASTEntityDeclNode var = decl.getEntityDeclList().iterator().next();
			switch (var.getObjectName().getText()) {
			case "VAR1" :
				assertEquals(16, var.findFirstToken().getStartIndex());
				assertEquals(30, var.findLastToken().getStopIndex());
				break;
			case "VAR2" :
				assertEquals(32, var.findFirstToken().getStartIndex());
				// because line break after & was suppressed, indexes are -1, see VerveineFortranStream.convertFreeFormInputBuffer()
				assertEquals(52, var.findLastToken().getStopIndex());
				// one line break extra after line to correct indexes, see VerveineFortranStream.convertFreeFormInputBuffer()
				break;
			case "VAR3" :
				assertEquals(55, var.findFirstToken().getStartIndex());
				assertEquals(69, var.findLastToken().getStopIndex());
				break;
			case "VAR4" :
				assertEquals(71, var.findFirstToken().getStartIndex());
				// because line break after & was suppressed, indexes are -1 , see VerveineFortranStream.convertFreeFormInputBuffer()
				assertEquals(91, var.findLastToken().getStopIndex());
				break;
			}
		}
	}

}