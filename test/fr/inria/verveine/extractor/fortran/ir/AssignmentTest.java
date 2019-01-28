package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;



public class AssignmentTest extends AbstractIrTest {

	public static final String SOURCE_CODE =
			"PROGRAM LE_PROGRAMME\n" +
			" someVar = 1\n" +
			" anotherVar = function(arg1,arg2)\n" +
			"END PROGRAM\n";

	@Test
	public void testAssignment() {
		parseCode(SOURCE_CODE);

		Collection<IREntity> assigns = dico.allWithKind(IRKind.NAMEREF);
		assertEquals(5, assigns.size());

		for (IREntity assg : assigns) {
			String varName = assg.getName();
			if ( varName.equals("someVar")) {
				assertTrue( assg.getData(IREntity.IS_WRITE).equals("true"));
			}
			else if ( varName.equals("anotherVar")) {
				assertTrue( assg.getData(IREntity.IS_WRITE).equals("true"));
			}
			else {
				assertNull( assg.getData(IREntity.IS_WRITE));
			}
		}
	}

}
