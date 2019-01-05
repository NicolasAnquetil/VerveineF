package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PreprocTest extends AbstractASTTest {

	public static final String SIMPLE_CONDITION =
			"module a_module\n" + 
			"#if MACRO1==1\n" + 
			"#if MACRO2==1\n" + 
			"integer :: VAR1\n" + 
			"#endif\n" + 
			"integer :: VAR2\n" + 
			"#endif\n" + 
			"integer :: VAR3\n" + 
			"end module\n";

	public static final String IFELSE_CONDITION =
			"module a_module\n" + 
			"#if MACRO==1\n" + 
			"integer :: VAR1\n" + 
			"#else\n" + 
			"integer :: VAR2\n" + 
			"integer :: VAR3\n" + 
			"#endif\n" + 
			"end module\n";

	public static final String NOTEQUAL_CONDITION =
			"module a_module\n" + 
			"#if MACRO!=1\n" + 
			"integer :: VAR1\n" + 
			"#endif\n" + 
			"integer :: VAR2\n" + 
			"end module\n";

	public static final String OR_CONDITION =
			"module a_module\n" + 
			"#if MACRO==0 || MACRO==1\n" + 
			"integer :: VAR1\n" + 
			"#endif\n" + 
			"integer :: VAR2\n" + 
			"end module\n";

	public static final String AND_CONDITION =
			"module a_module\n" + 
			"#if MACRO1==1 && MACRO2==1\n" + 
			"integer :: VAR1\n" + 
			"#endif\n" + 
			"integer :: VAR2\n" + 
			"end module\n";

	@Test
	public void testSimpleMacrosNotDefined() {
		parseCode(SIMPLE_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR3
	}

	@Test
	public void testSimpleWrongMacro1() {
		parseCode(new String[] {"-DMACRO1=0"} , SIMPLE_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR3
	}

	@Test
	public void testSimpleMacro1NotMacro2() {
		parseCode(new String[] {"-DMACRO1=1"} , SIMPLE_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR2, VAR3
	}

	@Test
	public void testSimpleMacro1WrongMacro2() {
		parseCode(new String[] {"-DMACRO1=1", "-DMACRO2=0"} , SIMPLE_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR2, VAR3
	}

	@Test
	public void testSimpleMacro1AndMacro2() {
		parseCode(new String[] {"-DMACRO1=1", "-DMACRO2=1"} , SIMPLE_CONDITION);
		assertEquals(3, ast.findAll(ASTTypeDeclarationStmtNode.class).size()); // VAR1, VAR2, VAR3
	}

	public void testIfelseUndefined() {
		parseCode(new String[] {} , IFELSE_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	public void testIfelseTrue() {
		parseCode(new String[] {"-DMACRO=1"} , IFELSE_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	public void testIfelseFalse() {
		parseCode(new String[] {"-DMACRO=0"} , IFELSE_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testDifferentUndefined() {
		parseCode(new String[] {} , NOTEQUAL_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testDifferentTrue() {
		parseCode(new String[] {"-DMACRO=0"} , NOTEQUAL_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testDifferentFalse() {
		parseCode(new String[] {"-DMACRO=1"} , NOTEQUAL_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testOrUndefined() {
		parseCode(new String[] {} , NOTEQUAL_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testOrTrue_False() {
		parseCode(new String[] {"-DMACRO=0"} , NOTEQUAL_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testOrFalse_True() {
		parseCode(new String[] {"-DMACRO=1"} , NOTEQUAL_CONDITION);
		assertEquals(2, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testOrFalse_False() {
		parseCode(new String[] {"-DMACRO=2"} , NOTEQUAL_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testAndUndefinedUndefined() {
		parseCode(new String[] {} , AND_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testAndDefinedUndefined() {
		parseCode(new String[] {"-DMACRO1=1"} , AND_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testAndWrongDefined() {
		parseCode(new String[] {"-DMACRO1=0", "-DMACRO2=1"} , AND_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

	@Test
	public void testAndDefinedDefined() {
		parseCode(new String[] {"-DMACRO1=1", "-DMACRO2=1"} , AND_CONDITION);
		assertEquals(1, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}

}
