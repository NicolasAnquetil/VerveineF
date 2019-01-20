package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTTypeDeclarationStmtNode;


public class VarDeclVisitorTest extends AbstractIrTest {

	public static final String SOURCE_CODE = "MODULE simpleModule\n" + 
			"  CHARACTER(LEN = 10), PARAMETER :: aString\n" + 
			"  INTEGER, intent(inout) :: anInt = 19\n" + 
			"  LOGICAL, PARAMETER, PUBLIC :: aBool = .false.\n" + 
			"  REAL(KIND(0D0))  :: aReal = 2000.0D0\n" + 
			"  integer, dimension(dim) :: dimVar =&\n" + 
			"       (/init1, init2, init3, init4/)\n" + 
			"END MODULE simpleModule";

	@Before
	public void setUp() throws Exception {
		parseCode(SOURCE_CODE);
	}

	@Test
	public void testNumberVarDecl() {
		
		assertEquals(5, dico.allWithKind(IRKind.VARIABLE).size());
	}
	
	@Test
	public void testVarDeclNames() {
		int aString = 0;
		int aBool = 0;
		int anInt =0;
		int aReal = 0;

		for (IREntity var : dico.allWithKind(IRKind.VARIABLE)) {
			switch (var.getName()) {
			case "aString": aString++; break;
			case "anInt":   anInt++;   break;
			case "aBool":   aBool++;   break;
			case "aReal":   aReal++;   break;
			case "dimVar":   anInt++;   break;
			default: fail("Unknown declared variable: "+var.getName());
			}
		}
		assertEquals(1, aString);
		assertEquals(2, anInt);
		assertEquals(1, aBool);
		assertEquals(1, aReal);
	}


	@Test
	public void testVarDeclTypes() {
		for (IREntity var :  dico.allWithKind(IRKind.VARIABLE)) {
			switch (var.getName()) {
			case "aString": assertEquals("CHARACTER", var.getData(IREntity.DECLARED_TYPENAME)); break;
			case "anInt":   assertEquals("INTEGER", var.getData(IREntity.DECLARED_TYPENAME));   break;
			case "aBool":   assertEquals("LOGICAL", var.getData(IREntity.DECLARED_TYPENAME));   break;
			case "aReal":   assertEquals("REAL", var.getData(IREntity.DECLARED_TYPENAME));      break;
			case "dimVar":  assertEquals("integer", var.getData(IREntity.DECLARED_TYPENAME));   break;
			default: fail("Unknown declared variable: "+var.getName());
			}
		}
	}

	@Test
	public void testAttrSpecParameter() {
		for (IREntity var : dico.allWithKind(IRKind.VARIABLE)) {
			switch (var.getName()) {
			case "aString": 
			case "aBool":
				assertTrue( (Boolean)var.getData(IREntity.DECLARED_PARAM));
				break;
			
			case "anInt":
			case "aReal":
				assertFalse( (Boolean)var.getData(IREntity.DECLARED_PARAM));
				break;
				
			default:
				break;
			}
			
		}
	}

}
