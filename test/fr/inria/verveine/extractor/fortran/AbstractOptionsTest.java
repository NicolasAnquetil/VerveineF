package fr.inria.verveine.extractor.fortran;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractOptionsTest extends AbstractFortranExtractorTest {

	protected void parseCode(String[] args) {
		super.parseCode(args);
		parser.outputIR();
	}

	protected void deleteIfExist(File f) {
		if (f.exists()) {
			f.delete();
		}
	}

	protected int countLines(File ir) {
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
	
		return count.getLineNumber();
	}

	protected File ir;
	
	@Before
	public void setup() {
		ir = new File(VerveineFParser.DEFAULT_OUTPUT_FILENAME);
		deleteIfExist(ir);
	}

	@After
	public void tearDown() {
		deleteIfExist(ir);
	}

}