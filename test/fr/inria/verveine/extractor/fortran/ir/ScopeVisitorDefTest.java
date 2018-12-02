package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ScopeVisitorDefTest extends AbstractIRTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/simpleModule.f90");
	}

	@Test
	public void testModule() {
		IREntity root;
		root = dico.getRoot();
		
		assertNotNull(root);
		assertEquals("simpleModule", root.getName());
		assertEquals("0", root.getData("anchorstart"));
		assertEquals("177", root.getData("anchorend"));
	}

}
