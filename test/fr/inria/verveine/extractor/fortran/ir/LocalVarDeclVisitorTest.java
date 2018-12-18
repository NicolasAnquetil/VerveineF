package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.VerveineFParser;


public class LocalVarDeclVisitorTest extends AbstractIRTest {
	
	private void parse(String[] args) {
		try {
			super.setup(args);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testNumberLocalsVarDecl() {
		parse(new String[] {VerveineFParser.ALLLOCALS_OPTION, "test_src/unit-tests/simpleSubPrgs.f90"});
		assertEquals(2, dico.allWithKind(IRKind.VARIABLE).size());
	}

	@Test
	public void testNumberGlobalsVarDecl() {
		parse(new String[] { "test_src/unit-tests/simpleSubPrgs.f90"});
		assertEquals(0, dico.allWithKind(IRKind.VARIABLE).size());
	}
	
	@Test
	public void testVarDeclNames() {
		parse(new String[] {VerveineFParser.ALLLOCALS_OPTION, "test_src/unit-tests/simpleSubPrgs.f90"});
		for (IREntity var : dico.allWithKind(IRKind.VARIABLE)) {
			assertEquals("i", var.getName());
		}
	}

}
