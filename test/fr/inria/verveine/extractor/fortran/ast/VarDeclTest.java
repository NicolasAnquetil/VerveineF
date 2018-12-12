package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class VarDeclTest extends AbstractASTTest {

	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleModule.f90");
	}

	@Test
	public void testNumberVarDecl() {
		assertEquals(5, ast.findAll(ASTTypeDeclarationStmtNode.class).size());
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
			case "dimVar":   anInt++;   break;
			default: fail("Unknown declared variable: "+name);
			}
		}
		assertEquals(1, aString);
		assertEquals(2, anInt);
		assertEquals(1, aBool);
		assertEquals(1, aReal);
	}


	@Test
	public void testAttrSpecParameter() {
		Iterator<ASTAttrSpecSeqNode> iter;
		for (ASTTypeDeclarationStmtNode decls : ast.findAll(ASTTypeDeclarationStmtNode.class)) {
			String name = decls.getEntityDeclList().iterator().next().getObjectName().getText();
			switch (name) {
			case "aString": 
				assertEquals(1, decls.getAttrSpecSeq().size());
				iter = decls.getAttrSpecSeq().iterator();
				assertTrue( iter.next().getAttrSpec().isParameter());
				break;
			case "anInt":
				assertEquals(1, decls.getAttrSpecSeq().size());
				iter = decls.getAttrSpecSeq().iterator();
				assertTrue( iter.next().getAttrSpec().isIntent());
				break;
			case "aBool":
				assertEquals(2, decls.getAttrSpecSeq().size());
				iter = decls.getAttrSpecSeq().iterator();
				assertTrue( iter.next().getAttrSpec().isParameter() || iter.next().getAttrSpec().isParameter());
				break;			
			case "aReal":
				assertEquals(0, decls.getAttrSpecSeq().size());
				break;
			case "dimVar":
				assertEquals(1, decls.getAttrSpecSeq().size());
				iter = decls.getAttrSpecSeq().iterator();
				assertTrue( iter.next().getAttrSpec().isDimension());
				break;
		
			default:
				break;
			}
			
		}
	}

}
