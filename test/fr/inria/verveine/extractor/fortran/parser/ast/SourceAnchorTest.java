package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;


public class SourceAnchorTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "module a_module\n" + 
			"integer :: VAR1\n" + 
			"integer :: &\n" + 
			"     VAR2\n" + 
			"integer :: VAR3\n" + 
			"integer :: &\n" + 
			"     VAR4\n" + 
			"end module\n";

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
				assertEquals(27, var.findFirstToken().getStartIndex());  // VAR1 token, should be INTEGER token -> 16
				assertEquals(30, var.findLastToken().getStopIndex());    // VAR1 token, could be EOS token -> +1
				break;
			case "VAR2" :
				// because line break after & was suppressed, indexes are -1, see VerveineFortranStream.convertFreeFormInputBuffer()
				assertEquals(49, var.findFirstToken().getStartIndex());  // VAR2 token, should be INTEGER token -> 32
				assertEquals(52, var.findLastToken().getStopIndex());    // VAR2 token, could be EOS token -> +1
				break;
			case "VAR3" :
				assertEquals(66, var.findFirstToken().getStartIndex());  // VAR3 token, should be INTEGER token -> 55
				assertEquals(69, var.findLastToken().getStopIndex());    // VAR3 token, could be EOS token -> +1
				break;
			case "VAR4" :
				// because line break after & was suppressed, indexes are -1 , see VerveineFortranStream.convertFreeFormInputBuffer()
				assertEquals(88, var.findFirstToken().getStartIndex());  // VAR4 token, should be INTEGER token -> 71
				assertEquals(91, var.findLastToken().getStopIndex());    // VAR4 token, could be EOS token -> +1
				break;
			}
		}
	}

}