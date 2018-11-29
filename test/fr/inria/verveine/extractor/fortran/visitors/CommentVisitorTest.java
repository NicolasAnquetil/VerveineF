package fr.inria.verveine.extractor.fortran.visitors;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eu.synectique.verveine.core.gen.famix.Comment;
import fr.inria.verveine.extractor.fortran.VerveineFAbstractTest;

public class CommentVisitorTest extends VerveineFAbstractTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/simpleModuleWithComments.f90");
	}

	@Test
	public void testNumberComments() {
		assertEquals(2, repo.count(Comment.class));
	}

	
}
