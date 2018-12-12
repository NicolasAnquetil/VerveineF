package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class VarDeclVisitorTest extends AbstractIRTest {

	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleModule.f90");
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
	public void testAttrSpecParameter() {
		for (IREntity var : dico.allWithKind(IRKind.VARIABLE)) {
			switch (var.getName()) {
			case "aString": 
			case "aBool":
				assertTrue( (Boolean)var.getData("declaredParam"));
				break;
			
			case "anInt":
			case "aReal":
				assertFalse( (Boolean)var.getData("declaredParam"));
				break;
				
			default:
				break;
			}
			
		}
	}

}
