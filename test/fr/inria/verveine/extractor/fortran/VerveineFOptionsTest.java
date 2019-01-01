package fr.inria.verveine.extractor.fortran;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.junit.Test;

public class VerveineFOptionsTest extends AbstractFortranExtractorTest {

	private static final String SOURCE_CODE = "PROGRAM LE_PROGRAMME\nEND PROGRAM\n";

	protected void parseCode(String[] args) {
		super.parseCode(args);
		parser.outputIR();
	}

	protected void deleteIfExist(File f) {
		if (f.exists()) {
			f.delete();
		}
	}

	private int countLines(File ir) {
		LineNumberReader count;
		try {
			count = new LineNumberReader(new FileReader(ir));
			count.skip(Long.MAX_VALUE);  // read the entire file
			count.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		return  count.getLineNumber();
	}

	@Test
	public void defaultOutputTest() {
		File ir = new File(VerveineFParser.DEFAULT_OUTPUT_FILENAME);

		deleteIfExist(ir);

		parseCode(new String[] {"--stdinput", SOURCE_CODE});
		assertTrue("Default output file not found",ir.exists());
		assertTrue("Output file is empty", ir.length() > 0);
		assertEquals(2, countLines(ir));
		
		deleteIfExist(ir);
	}

	@Test
	public void nonDefaultOutputTest() {
		File ir = new File("test.ir");
		File defaultIr = new File(VerveineFParser.DEFAULT_OUTPUT_FILENAME);

		deleteIfExist(ir);
		deleteIfExist(defaultIr);

		parseCode(new String[] {"-o", "test.ir", "--stdinput", SOURCE_CODE});
		assertFalse("Default output file found",defaultIr.exists());
		assertTrue("Non-default output file not found",ir.exists());
		assertTrue("Output file is empty", ir.length() > 0);
		assertEquals(2, countLines(ir));

		deleteIfExist(ir);
	}

}
