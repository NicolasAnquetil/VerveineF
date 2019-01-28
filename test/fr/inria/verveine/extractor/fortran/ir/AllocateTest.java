package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class AllocateTest extends AbstractIrTest {

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
		int nbAlloc = 0;
		int nbDealloc = 0;

		Collection<IREntity> allocs = dico.allWithKind(IRKind.SUBPRGCALL);
		assertEquals(4, allocs.size());

		for (IREntity call : allocs) {
			String varName = call.getName();
			String alloc_var = null;

			if (varName.equals("allocate")) {
				nbAlloc++;
				alloc_var = (String) call.getData(IREntity.ALLOC_VAR);
				assertTrue( alloc_var.equals("var1") || alloc_var.equals("var2") );
			}
			else if (varName.equals("deallocate")) {
				nbDealloc++;
				alloc_var = (String) call.getData(IREntity.ALLOC_VAR);
				assertTrue( alloc_var.equals("var1") || alloc_var.equals("var2") );
			}
		}
		
		assertEquals(2, nbAlloc);
		assertEquals(2, nbDealloc);
	}

}
