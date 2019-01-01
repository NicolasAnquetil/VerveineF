package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class ScopeVisitorProgramTest extends AbstractIrTest {

	public static final String SOURCE_CODE = "PROGRAM LE_PROGRAMME\nEND PROGRAM\n";

	@Test
	public void testCompilationUnit() {
		parseCode(SOURCE_CODE);

		IREntity root = dico.getRoot();
		
		assertNotNull(root);
		assertEquals(IRKind.COMPILATION_UNIT, root.getKind());
		assertEquals("["+VerveineFParser.STRING_SOURCE_FILENAME+"]", root.getName());
	}

	@Test
	public void testProgram() {
		parseCode(SOURCE_CODE);

		Collection<IREntity> pgms = dico.allWithKind(IRKind.PROGRAM);
		assertEquals(1, pgms.size());

		IREntity pgm = pgms.iterator().next(); 
		
		assertNotNull(pgm);
		assertEquals("LE_PROGRAMME", pgm.getName());
		assertEquals(0, pgm.getData("anchorstart"));
		assertEquals(32, pgm.getData("anchorend"));
	}

}
