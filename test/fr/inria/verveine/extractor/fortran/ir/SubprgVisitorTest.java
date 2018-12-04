package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class SubprgVisitorTest extends AbstractIRTest {
		
	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleSubPrgs.f90");
	}

	@Test
	public void testFunction() {
		Collection<IREntity> subs = dico.allWithKind(IRKind.FUNCTION);
		assertEquals(1, subs.size());

		IREntity sub = subs.iterator().next(); 
		assertEquals("blih", sub.getName());
		assertEquals(1, sub.getData("cyclomaticComplexity"));
		assertEquals(IRKind.MODULE, sub.getParent().getKind());
	}

	@Test
	public void testSubroutine() {
		Collection<IREntity> subs = dico.allWithKind(IRKind.SUBPROGRAM);
		assertEquals(1, subs.size());

		IREntity sub = subs.iterator().next(); 
		assertEquals("blah", sub.getName());
		assertEquals(1, sub.getData("cyclomaticComplexity"));
		assertEquals(IRKind.MODULE, sub.getParent().getKind());
	}

}
