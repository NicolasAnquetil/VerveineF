package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

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
	public void testFunctionInvocations() {
		Collection<IREntity> calls = dico.allWithKind(IRKind.NAMEREF);
		assertEquals(1, calls.size());
		assertEquals("blih", calls.iterator().next().getName());
	}

}
