package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


public class StructuredTypeTest extends AbstractASTTest {
		
	@Before
	public void setup() {
		super.setup("test_src/unit-tests/structuredTypeDecl.f90");
	}

	@Test
	public void testStruct() {
		// kind of a "smoke test"
		Collection<ASTModuleNode> mods = ast.findAll(ASTModuleNode.class);
		assertEquals(1, mods.size());
	}

}
