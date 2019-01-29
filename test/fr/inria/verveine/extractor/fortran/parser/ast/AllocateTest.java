package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class AllocateTest extends AbstractASTTest {

	public static final String SOURCE_CODE =
			"PROGRAM aProgram\n"+
			"  allocate(var1%sel)\n" + 
			"  allocate(var2, var3, stat=erreur)\n" + 
			"  deallocate(var4, var5)\n" + 
			"  deallocate(var6%sel)\n" + 
			"END PROGRAM\n";

	@Before
	public void setup() {
		parseCode(SOURCE_CODE);
	}

	@Test
	public void testAllocate() {
		Collection<ASTAllocateStmtNode> allocs = ast.findAll(ASTAllocateStmtNode.class);
		assertEquals(2, allocs.size());
		
		for (ASTAllocateStmtNode alloc : allocs) {
			if (alloc.getAllocationList().size() == 1) {
				assertEquals("var1%sel",alloc.getAllocationList().iterator().next().getAllocateObject().fortranNameToString());
			}
			else if (alloc.getAllocationList().size() == 2) {
				String varName = alloc.getAllocationList().iterator().next().getAllocateObject().fortranNameToString();
				assertTrue( varName.equals("var2") || varName.equals("var3"));

			}
		}
	}

	@Test
	public void testDeallocate() {
		Collection<ASTDeallocateStmtNode> deallocs = ast.findAll(ASTDeallocateStmtNode.class);
		assertEquals(2, deallocs.size());
		
		for (ASTDeallocateStmtNode alloc : deallocs) {
			if (alloc.getAllocateObjectList().size() == 1) {
				assertEquals("var6%sel",alloc.getAllocateObjectList().iterator().next().fortranNameToString());
			}
			else if (alloc.getAllocateObjectList().size() == 2) {
				String varName = alloc.getAllocateObjectList().iterator().next().fortranNameToString();
				assertTrue( varName.equals("var4") || varName.equals("var5"));

			}
		}
	}

}
