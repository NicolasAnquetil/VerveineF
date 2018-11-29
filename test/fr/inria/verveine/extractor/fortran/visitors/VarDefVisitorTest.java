package fr.inria.verveine.extractor.fortran.visitors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import eu.synectique.verveine.core.gen.famix.GlobalVariable;
import fr.inria.verveine.extractor.fortran.VerveineFAbstractTest;

public class VarDefVisitorTest extends VerveineFAbstractTest {
		
	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleModule.f90");
	}

	@Test
	public void testVariableNumber() {
		assertEquals(4, repo.count(GlobalVariable.class));
	}

	@Test
	public void testVariableNames() {
		int aString = 0;
		int aBool = 0;
		int anInt =0;
		int aReal = 0;

		for (GlobalVariable var : repo.all(GlobalVariable.class)) {
			switch (var.getName()) {
			case "aString": aString++; break;
			case "anInt":   anInt++;   break;
			case "aBool":   aBool++;   break;
			case "aReal":   aReal++;   break;
			default: fail("Unknown declared variable: "+var.getName());
			}
		}
		assertEquals(1, aString);
		assertEquals(1, anInt);
		assertEquals(1, aBool);
		assertEquals(1, aReal);
	}

	@Test
	public void testAttrSpecParameter() {
		for (GlobalVariable var : repo.all(GlobalVariable.class)) {
			switch (var.getName()) {
			case "aString": 
			case "aBool":
				assertTrue( var.getIsDeclaredFortranParameter());
				break;
			
			case "anInt":
			case "aReal":
				assertFalse( var.getIsDeclaredFortranParameter());
				break;
				
			default:
				break;
			}
			
		}
	}

}
