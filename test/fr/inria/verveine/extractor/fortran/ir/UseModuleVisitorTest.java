package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UseModuleVisitorTest extends AbstractIrTest {

	public static final String SOURCE_CODE = "MODULE simpleModule\n" + 
			"use used1\n" + 
			"use used2\n" + 
			"END MODULE\n";

	@Test
	public void testUseStmt() {
		boolean use1 = false;
		boolean use2 = false;

		parseCode(SOURCE_CODE);

		for (IREntity use : dico.allWithKind(IRKind.USEMODULE)) {
			switch (use.getName()) {
			case "used1": use1 = true; break;
			case "used2": use2 = true; break;
			default: fail("Unknown used module: "+use.getName());
			}
		}

		assertTrue("Used module 'used1' not found", use1);
		assertTrue("Used module 'used1' not found", use2);
	}

}
