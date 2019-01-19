package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.Options;

public class InvokAccessVisitorTest extends AbstractIrTest {

	public static final String SOURCE_CODE =
			"MODULE simpleModule\n" + 
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

	public static final String CODE_WITH_INTRINSIC =
			"PROGRAM aProgram\n" + 
			"	write( fraction(42))\n" + 
			"	CALL  random_seed(size=5)\n" + 
			"END PROGRAM\n";

	@Test
	public void testSubProgCalls() {
		parseCode(SOURCE_CODE);
		Collection<IREntity> calls = dico.allWithKind(IRKind.SUBPRGCALL);
		
		assertEquals(1, calls.size());
		assertEquals("blah", calls.iterator().next().getName());
	}

	@Test
	public void testVarOrFunctionRef() {
		parseCode(SOURCE_CODE);
		Collection<IREntity> calls = dico.allWithKind(IRKind.NAMEREF);
		
		assertEquals(2, calls.size());
		
		Iterator<IREntity> iter = calls.iterator(); 
		String name = iter.next().getName();
		assertTrue( name.equals("blih") || name.equals("i") );
		name = iter.next().getName();
		assertTrue( name.equals("blih") || name.equals("i") );
	}

	@Test
	public void testWithoutIntrinsic() {
		parseCode(CODE_WITH_INTRINSIC);
		
		assertEquals(0, dico.allWithKind(IRKind.SUBPRGCALL).size());
		assertEquals(0, dico.allWithKind(IRKind.NAMEREF).size());
	}

	@Test
	public void testWithIntrinsic() {
		Collection<IREntity> entities;

		parseCode(new String[] {Options.WITHINTRINSICS_OPTION}, CODE_WITH_INTRINSIC);

		entities = dico.allWithKind(IRKind.SUBPRGCALL);
		assertEquals(1, entities.size());
		assertEquals("random_seed", entities.iterator().next().getName());

		entities = dico.allWithKind(IRKind.NAMEREF);
		assertEquals(1, entities.size());
		assertEquals("fraction", entities.iterator().next().getName());
	}

}
