package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;

public class MissingIncludeTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "PROGRAM LE_PROGRAMME\n" +
			"include 'inexistant-include.file'\n" +
			"END PROGRAM\n";

	@Test
	public void testProgram() {
		parseCode(SOURCE_CODE);

		assertEquals(1, ast.findAll(ASTMainProgramNode.class).size());

	}

}
