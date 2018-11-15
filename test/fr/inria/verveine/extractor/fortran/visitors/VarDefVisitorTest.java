package fr.inria.verveine.extractor.fortran.visitors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.akuhn.fame.Repository;
import eu.synectique.verveine.core.gen.famix.GlobalVariable;
import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class VarDefVisitorTest  {

	protected Repository repo;
		
	@Before
	public void setup() throws Exception {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions( new String[] {"test_src/unit-tests/simpleModule.f90"});
		parser.parse();
		repo = parser.getFamixRepo();
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
