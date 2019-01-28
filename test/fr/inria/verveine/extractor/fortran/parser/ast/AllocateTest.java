package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class AllocateTest extends AbstractASTTest {

	public static final String SOURCE_CODE =
			"PROGRAM aProgram\n"+
			"  allocate(var1)\n" + 
			"  allocate(var2, stat=erreur)\n" + 
			"  deallocate(var1)\n" + 
			"  deallocate(var2)\n" + 
			"END PROGRAM\n";

	@Before
	public void setup() {
		parseCode(SOURCE_CODE);
	}

	@Test
	public void testAllocate() {
		Collection<ASTAllocateStmtNode> allocs = ast.findAll(ASTAllocateStmtNode.class);
		assertEquals(2, allocs.size());
	}

	@Test
	public void testDeallocate() {
		Collection<ASTDeallocateStmtNode> deallocs = ast.findAll(ASTDeallocateStmtNode.class);
		assertEquals(2, deallocs.size());
	}

}
