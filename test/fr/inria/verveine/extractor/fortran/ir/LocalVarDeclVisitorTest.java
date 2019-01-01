package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fr.inria.verveine.extractor.fortran.VerveineFParser;


public class LocalVarDeclVisitorTest extends AbstractIrTest {

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
	public void testNumberLocalsVarDecl() {
		parseCode( new String[] {VerveineFParser.ALLLOCALS_OPTION, "--stdinput", SOURCE_CODE} );
		assertEquals(2, dico.allWithKind(IRKind.VARIABLE).size());
	}

	@Test
	public void testNumberGlobalsVarDecl() {
		parseCode( SOURCE_CODE);
		assertEquals(0, dico.allWithKind(IRKind.VARIABLE).size());
	}
	
	@Test
	public void testVarDeclNames() {
		parseCode( new String[] {VerveineFParser.ALLLOCALS_OPTION, "--stdinput", SOURCE_CODE} );
		for (IREntity var : dico.allWithKind(IRKind.VARIABLE)) {
			assertEquals("i", var.getName());
		}
	}

}
