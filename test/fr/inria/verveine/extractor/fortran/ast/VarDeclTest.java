package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class VarDeclTest extends AbstractASTTest {

	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleModule.f90");
	}

	@Test
	public void testNumberVarDecl() {
		assertEquals(4, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
	}
	
	@Test
	public void testVarDeclNames() {
		int aString = 0;
		int aBool = 0;
		int anInt =0;
		int aReal = 0;

		for (ASTTypeDeclarationStmtNode decls : ast.findAll(ASTTypeDeclarationStmtNode.class)) {
			assertEquals(1, decls.getEntityDeclList().size());
			String name = decls.getEntityDeclList().iterator().next().getObjectName().getText();
			switch (name) {
			case "aString": aString++; break;
			case "anInt":   anInt++;   break;
			case "aBool":   aBool++;   break;
			case "aReal":   aReal++;   break;
			default: fail("Unknown declared variable: "+name);
			}
		}
		assertEquals(1, aString);
		assertEquals(1, anInt);
		assertEquals(1, aBool);
		assertEquals(1, aReal);
	}


	@Test
	public void testAttrSpecParameter() {
		for (ASTTypeDeclarationStmtNode decls : ast.findAll(ASTTypeDeclarationStmtNode.class)) {
			String name = decls.getEntityDeclList().iterator().next().getObjectName().getText();
			switch (name) {
			case "aString": 
			case "aBool":
				assertEquals(1, decls.getAttrSpecSeq().size());
				assertTrue( decls.getAttrSpecSeq().iterator().next().getAttrSpec().isParameter());
				break;
			
			case "anInt":
			case "aReal":
				assertEquals(0, decls.getAttrSpecSeq().size());
				break;
				
			default:
				break;
			}
			
		}
	}

}
