package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class InvokAccessVisitorTest extends AbstractIRTest {

	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleSubPrgs.f90");
	}

	@Test
	public void testSubProgCalls() {
		Collection<IREntity> calls = dico.allWithKind(IRKind.SUBPRGCALL);
		assertEquals(1, calls.size());
		assertEquals("blah", calls.iterator().next().getName());
	}

	@Test
	public void testVarOrFunctionRef() {
		Collection<IREntity> calls = dico.allWithKind(IRKind.NAMEREF);
		assertEquals(2, calls.size());
		
		Iterator<IREntity> iter = calls.iterator(); 
		String name = iter.next().getName();
		assertTrue( name.equals("blih") || name.equals("i") );
		name = iter.next().getName();
		assertTrue( name.equals("blih") || name.equals("i") );
	}

}
