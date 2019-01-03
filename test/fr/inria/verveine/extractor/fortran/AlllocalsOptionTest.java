package fr.inria.verveine.extractor.fortran;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AlllocalsOptionTest extends AbstractOptionsTest {

	public static final String SOURCE_CODE = 
			"  SUBROUTINE blah\n" + 
			"    INTEGER :: i\n" + 
			"  END SUBROUTINE\n";

	@Test
	public void noLocalTest() {
		parseCode( SOURCE_CODE);

		assertTrue("Output file not found",ir.exists());
		assertTrue("Output file is empty", ir.length() > 0);
		assertEquals(2, countLines(ir));  // CompilationUnit, blah
	}

	@Test
	public void allLocalsTest() {
		parseCode(new String[] {VerveineFParser.ALLLOCALS_OPTION}, SOURCE_CODE);

		assertTrue("Output file not found",ir.exists());
		assertTrue("Output file is empty", ir.length() > 0);
		assertEquals(3, countLines(ir));  // CompilationUnit, blah, i
	}

}
