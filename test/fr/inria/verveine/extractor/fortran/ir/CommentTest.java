package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class CommentTest extends AbstractIRTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/simpleModuleWithComments.f90");
	}

	@Test
	public void testComments() {
		Collection<IREntity> allCmts = dico.allWithKind(IRKind.COMMENT);
		assertEquals(2, allCmts.size());
		for (IREntity cmt : allCmts) {
			assertEquals(IRKind.MODULE, cmt.getParent().getKind());
		}
	}

	@Test
	public void testCommentContent() {
		Iterator<IREntity> iter = dico.allWithKind(IRKind.COMMENT).iterator();

		// don't know the order of the comments, so have to test both
		assertTrue( iter.next().getData("content").equals("  ! Comments again\n") ||
				    iter.next().getData("content").equals("  ! Comments again\n"));
	}

}
