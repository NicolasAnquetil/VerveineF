package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


public class ModuleTest extends AbstractASTTest {
		
	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleModule.f90");
	}

	@Test
	public void testModule() {
		Collection<ASTModuleNode> mods = ast.findAll(ASTModuleNode.class);
		assertEquals(1, mods.size());

		ASTModuleNode mod = mods.iterator().next(); 
		assertEquals("simpleModule", mod.basename());
	}

}
