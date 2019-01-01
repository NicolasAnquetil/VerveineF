package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class CommentTest extends AbstractIrTest {

	public static final String SOURCE_CODE = "! This is a simple Module\n" + 
			"! It has comments\n" + 
			"MODULE simpleModuleWithComments\n" + 
			"  ! Comments again\n" + 
			"END MODULE";

	@Before
	public void setUp() throws Exception {
		parseCode(SOURCE_CODE);
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
