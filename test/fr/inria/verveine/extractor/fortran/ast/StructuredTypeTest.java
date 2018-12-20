package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;


public class StructuredTypeTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "MODULE simpleModule\n" + 
			"  type structuredType\n" + 
			"    CHARACTER(LEN = 10) :: aString\n" + 
			"    INTEGER :: anInt = 19\n" + 
			"    LOGICAL :: aBool = .false.\n" + 
			"    REAL(KIND(0D0))  :: aReal = 2000.0D0\n" + 
			"  end type\n" + 
			"END MODULE";

	@Test
	public void testStruct() {
		parseCode(SOURCE_CODE);

		// kind of a "smoke test"
		Collection<ASTModuleNode> mods = ast.findAll(ASTModuleNode.class);
		assertEquals(1, mods.size());
	}

}
