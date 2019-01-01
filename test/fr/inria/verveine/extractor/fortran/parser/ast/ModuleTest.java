package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;


public class ModuleTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "MODULE simpleModule\n" + 
			"implicit none\n" + 
			"  private :: aProc\n" + 
			"  interface anotherProc\n" + 
			"    module procedure aProc\n" + 
			"  end interface\n" + 
			"\n" + 
			"  integer :: aVar\n" + 
			"END MODULE simpleModule";

	@Test
	public void testModule() {
		parseCode(SOURCE_CODE);

		Collection<ASTModuleNode> mods = ast.findAll(ASTModuleNode.class);
		assertEquals(1, mods.size());

		ASTModuleNode mod = mods.iterator().next(); 
		assertEquals(3, mod.getModuleBody().size());
	}

}
