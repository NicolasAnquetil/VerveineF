package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class ScopeVisitorModuleTest extends AbstractIRTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/simpleModule.f90");
	}

	@Test
	public void testCompilationUnit() {
		IREntity root;
		root = dico.getRoot();
		
		assertNotNull(root);
		assertEquals("[test_src/unit-tests/simpleModule.f90]", root.getName());
	}

	@Test
	public void testModule() {
		Collection<IREntity> mods = dico.allWithKind(IRKind.MODULE);
		assertEquals(1, mods.size());

		IREntity mod = mods.iterator().next(); 
		
		assertNotNull(mod);
		assertEquals("simpleModule", mod.getName());
		assertEquals(0, mod.getData("anchorstart"));
		assertEquals(177, mod.getData("anchorend"));
	}

}
