package fr.inria.verveine.extractor.fortran.visitors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import eu.synectique.verveine.core.gen.famix.Comment;

public class CommentVisitorTest extends AbstractFunctionalTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/simpleModuleWithComments.f90");
	}

	@Test
	public void testNumberComments() {
		assertEquals(2, repo.count(Comment.class));
	}

	@Test
	public void testCommentContent() {
		Iterator<Comment> cmtsIter = repo.all(Comment.class).iterator();
		String expected = "  ! Comments again\n";
		// order of comments not guarenteed, so we need to check both
		assertTrue("Expected comment 's"+expected+"' not found", cmtsIter.next().getContent().equals(expected) || cmtsIter.next().getContent().equals(expected) );
	}

}
