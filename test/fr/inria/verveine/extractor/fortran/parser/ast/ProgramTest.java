package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;


public class ProgramTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "PROGRAM LE_PROGRAMME\nEND PROGRAM\n";

	@Test
	public void testProgram() {
		parseCode(SOURCE_CODE);

		Collection<ASTMainProgramNode> mods = ast.findAll(ASTMainProgramNode.class);
		assertEquals(1, mods.size());

		ASTMainProgramNode mod = mods.iterator().next(); 
		assertEquals("LE_PROGRAMME", mod.getProgramStmt().getProgramName().getText());
		assertEquals(0, mod.getBody().size());
	}

}
