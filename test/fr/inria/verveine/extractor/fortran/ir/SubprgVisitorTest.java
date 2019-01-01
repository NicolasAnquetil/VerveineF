package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class SubprgVisitorTest extends AbstractIrTest {

	public static final String SOURCE_CODE = "MODULE simpleModule\n" + 
			"CONTAINS\n" + 
			"\n" + 
			"    SUBROUTINE blah()\n" + 
			"		INTEGER i\n" + 
			"		i = blih()\n" + 
			" 		i = i+1\n" + 
			"    END SUBROUTINE blah\n" + 
			"\n" + 
			"	function blih()  result(i)\n" + 
			"		INTEGER :: i\n" + 
			"		CALL blah()\n" + 
			"		i = 0\n" + 
			"	END function blih\n" + 
			"\n" + 
			"END MODULE\n";

	@Before
	public void setUp() throws Exception {
		parseCode(SOURCE_CODE);
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
