package fr.inria.verveine.extractor.fortran;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class OutputOptionTest extends AbstractOptionsTest {

	private static final String SOURCE_CODE = "PROGRAM LE_PROGRAMME\nEND PROGRAM\n";

	@Test
	public void defaultOutputTest() {
		parseCode(new String[] {Options.STRING_SOURCE_OPTION, SOURCE_CODE});
		assertTrue("Default output file not found",ir.exists());
		assertTrue("Output file is empty", ir.length() > 0);
		assertEquals(2, countLines(ir));
	}

	@Test
	public void nonDefaultOutputTest() {
		File testIr = new File("test.ir");

		deleteIfExist(testIr);

		parseCode(new String[] {"-o", "test.ir", Options.STRING_SOURCE_OPTION, SOURCE_CODE});
		assertFalse("Default output file found",ir.exists());
		assertTrue("Non-default output file not found",testIr.exists());
		assertTrue("Output file is empty", testIr.length() > 0);
		assertEquals(2, countLines(testIr));

		deleteIfExist(testIr);
	}

}
