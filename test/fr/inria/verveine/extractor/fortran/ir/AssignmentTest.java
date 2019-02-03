package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

		Collection<IREntity> refs = dico.allWithKind(IRKind.NAMEREF);
		assertEquals(1, refs.size());


		refs = dico.allWithKind(IRKind.VARREF);
		assertEquals(2, refs.size());

		for (IREntity ref : refs) {
			String varName = (String) ref.getData(IREntity.ASSIGN_LHS);
			assertNotNull(varName);
			if ( varName.equals("someVar")) {
				assertTrue( ref.getData(IREntity.IS_WRITE).equals("true"));
			}
			else if ( varName.equals("anotherVar")) {
				assertTrue( ref.getData(IREntity.IS_WRITE).equals("true"));
			}
		}
	}

}
