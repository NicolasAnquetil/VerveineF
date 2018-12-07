package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ScopeVisitorProgramTest extends AbstractIRTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/smallProgram.f90");
	}

	@Test
	public void testProgram() {
		IREntity root;
		root = dico.getRoot();
		
		assertNotNull(root);
		assertEquals("LE_PROGRAMME", root.getName());
		assertEquals(0, root.getData("anchorstart"));
		assertEquals(33, root.getData("anchorend"));
	}

}
