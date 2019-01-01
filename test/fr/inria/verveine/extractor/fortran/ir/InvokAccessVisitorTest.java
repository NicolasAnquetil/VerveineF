package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class InvokAccessVisitorTest extends AbstractIrTest {

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
