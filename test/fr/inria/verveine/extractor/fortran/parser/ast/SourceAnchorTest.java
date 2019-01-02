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
		assertEquals(104, mod.findLastToken().getStopIndex());
	}


	@Test
	public void testAnchorVars() {
		parseCode(SOURCE_CODE);

		Collection<ASTTypeDeclarationStmtNode> vars = ast.findAll(ASTTypeDeclarationStmtNode.class);
		assertEquals(4, vars.size());

		for (ASTTypeDeclarationStmtNode decl : vars) {
			ASTEntityDeclNode var = decl.getEntityDeclList().iterator().next();
			switch (var.getObjectName().getText()) {
			case "VAR1" :
				assertEquals(27, var.findFirstToken().getStartIndex());
				assertEquals(31, var.findLastToken().getStopIndex());
				break;
			case "VAR2" :
				assertEquals(50, var.findFirstToken().getStartIndex());
				assertEquals(54, var.findLastToken().getStopIndex());
				break;
			case "VAR3" :
				assertEquals(66, var.findFirstToken().getStartIndex());
				assertEquals(70, var.findLastToken().getStopIndex());
				break;
			case "VAR4" :
				assertEquals(89, var.findFirstToken().getStartIndex());
				assertEquals(93, var.findLastToken().getStopIndex());
				break;
			}
		}
	}

}