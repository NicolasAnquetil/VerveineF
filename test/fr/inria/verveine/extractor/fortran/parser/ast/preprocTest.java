package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class preprocTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "module a_module\n" + 
			//"!before #if\n" + 
			"#if MACRO1==1\n" + 
			"#if MACRO2==1\n" + 
			"integer :: VAR1\n" + 
			"#endif\n" + 
			"integer :: VAR2\n" + 
			"#endif\n" + 
			"integer :: VAR3\n" + 
			"end module\n";

	@Test
	public void testMacrosNotDefined() {
		parseCode(SOURCE_CODE);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR3
	}

	@Test
	public void testWrongMacro1() {
		parseCode(new String[] {"-DMACRO1=0"} , SOURCE_CODE);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR3
	}

	@Test
	public void testMacro1NotMacro2() {
		parseCode(new String[] {"-DMACRO1=1"} , SOURCE_CODE);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR2, VAR3
	}

	@Test
	public void testMacro1WrongMacro2() {
		parseCode(new String[] {"-DMACRO1=1", "-DMACRO2=0"} , SOURCE_CODE);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR2, VAR3
	}

	@Test
	public void testMacro1AndMacro2() {
		parseCode(new String[] {"-DMACRO1=1", "-DMACRO2=1"} , SOURCE_CODE);
		assertEquals(3, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR1, VAR2, VAR3
	}

}
