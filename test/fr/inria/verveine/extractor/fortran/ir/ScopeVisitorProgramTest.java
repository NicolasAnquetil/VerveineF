package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class ScopeVisitorProgramTest extends AbstractIRTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/smallProgram.f90");
	}

	@Test
	public void testCompilationUnit() {
		IREntity root;
		root = dico.getRoot();
		
		assertNotNull(root);
		assertEquals(IRKind.COMPILATION_UNIT, root.getKind());
		assertEquals("[test_src/unit-tests/smallProgram.f90]", root.getName());
	}

	@Test
	public void testProgram() {
		Collection<IREntity> pgms = dico.allWithKind(IRKind.PROGRAM);
		assertEquals(1, pgms.size());

		IREntity pgm = pgms.iterator().next(); 
		
		assertNotNull(pgm);
		assertEquals("LE_PROGRAMME", pgm.getName());
		assertEquals(0, pgm.getData("anchorstart"));
		assertEquals(33, pgm.getData("anchorend"));
	}

}
