package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


public class ProgramTest extends AbstractASTTest {
		
	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/smallProgram.f90");
	}

	@Test
	public void testProgram() {
		Collection<ASTMainProgramNode> mods = ast.findAll(ASTMainProgramNode.class);
		assertEquals(1, mods.size());

		ASTMainProgramNode mod = mods.iterator().next(); 
		assertEquals("LE_PROGRAMME", mod.getProgramStmt().getProgramName().getText());
	}

}
