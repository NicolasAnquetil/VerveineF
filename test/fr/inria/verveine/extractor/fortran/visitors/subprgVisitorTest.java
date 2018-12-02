package fr.inria.verveine.extractor.fortran.visitors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.GlobalVariable;

public class subprgVisitorTest extends AbstractFunctionalTest {
		
	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleSubPrgs.f90");
	}

	@Test
	public void testSubPrg() {
		Collection<Function> subs = repo.all(Function.class);
		Function blah = null;
		Function blih = null;

		assertEquals( 2, subs.size());
		for (Function f : subs) {
			switch (f.getName()) {
			case "blah" : blah = f; break;
			case "blih" : blih = f; break;
			default : fail("Unexpected subprogram name: " + f.getName());
			}
		}
		assertNotEquals(null, blah);
		assertNotEquals(null, blih);
	}

}
