package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class preprocTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "module a_module\n" + 
			//"!before #if\n" + 
			"#if MACRO==1\n" + 
			"integer :: VAR1\n" + 
			"#endif\n" + 
			"integer :: VAR2\n" + 
			"end module\n";

	@Test
	public void testMacroNotDefined() {
		parseCode(SOURCE_CODE);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testMacroIsDefined() {
		parseCode(new String[] {"-DMACRO=1"} , SOURCE_CODE);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testMacroDefinedWrongValue() {
		parseCode(new String[] {"-DMACRO=2"} , SOURCE_CODE);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

}
