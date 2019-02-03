package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class AllocateTest extends AbstractIrTest {

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
	public void testAllocateCalls() {
		int nbAlloc = 0;
		int nbDealloc = 0;
		String name;

		Collection<IREntity> calls = dico.allWithKind(IRKind.SUBPRGCALL);
		assertEquals(4, calls.size());

		for (IREntity alloc : calls) {
			name = alloc.getName();

			if (name.equals(IREntity.ALLOCATE_NAME)) {
				nbAlloc++;
			}
			else if (name.equals(IREntity.DEALLOCATE_NAME)) {
				nbDealloc++;
			}
		}
		
		assertEquals(2, nbAlloc);
		assertEquals(2, nbDealloc);		
	}

	@Test
	public void testNameRefs() {
		String name;
		IREntity parent;
		Collection<IREntity>  vars = dico.allWithKind(IRKind.NAMEREF);
		assertEquals(7, vars.size());

		for (IREntity var : vars) {
			name = var.getName();
			parent = var.getParent();
			assertNotNull(parent);
			assertEquals(IRKind.SUBPRGCALL, parent.getKind());
			
			if (name.equals("var1%sel")) {
				assertEquals(IREntity.ALLOCATE_NAME, parent.getName());
			}
			else if (name.equals("var2")) {
				assertEquals(IREntity.ALLOCATE_NAME, parent.getName());
			}
			else if (name.equals("var3")) {
				assertEquals(IREntity.ALLOCATE_NAME, parent.getName());
			}
			else if (name.equals("erreur")) {
				assertEquals(IREntity.ALLOCATE_NAME, parent.getName());
			}
			else if (name.equals("var4")) {
				assertEquals(IREntity.DEALLOCATE_NAME, parent.getName());
			}
			else if (name.equals("var5")) {
				assertEquals(IREntity.DEALLOCATE_NAME, parent.getName());
			}
			else if (name.equals("var6%sel")) {
				assertEquals(IREntity.DEALLOCATE_NAME, parent.getName());
			}
			else {
				fail("Unknown variable name " + name);
			}
		}

	}

}
