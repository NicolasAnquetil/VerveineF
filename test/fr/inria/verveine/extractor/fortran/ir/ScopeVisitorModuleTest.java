package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class ScopeVisitorModuleTest extends AbstractIrTest {

	public static final String SOURCE_CODE = "MODULE simpleModule\n" + 
			"  CHARACTER(LEN = 10), PARAMETER :: aString\n" + 
			"  INTEGER, intent(inout) :: anInt = 19\n" + 
			"  LOGICAL, PARAMETER, PUBLIC :: aBool = .false.\n" + 
			"  REAL(KIND(0D0))  :: aReal = 2000.0D0\n" + 
			"  integer, dimension(dim) :: dimVar =&\n" + 
			"       (/init1, init2, init3, init4/)\n" + 
			"END MODULE simpleModule";

	@Before
	public void setUp() throws Exception {
		parseCode(SOURCE_CODE);
	}

	@Test
	public void testCompilationUnit() {
		IREntity root;
		root = dico.getRoot();
		
		assertNotNull(root);
		assertEquals("["+VerveineFParser.STRING_SOURCE_FILENAME+"]", root.getName());
	}

	@Test
	public void testModule() {
		Collection<IREntity> mods = dico.allWithKind(IRKind.MODULE);
		assertEquals(1, mods.size());

		IREntity mod = mods.iterator().next(); 
		
		assertNotNull(mod);
		assertEquals("simpleModule", mod.getName());
		assertEquals(0, mod.getData("anchorstart"));
		assertEquals(290, mod.getData("anchorend"));
	}

}
