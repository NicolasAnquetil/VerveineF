package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CommentTest extends AbstractASTTest {

	private Set<ASTToken> tokens;
	private List<String> comments;

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/simpleModuleWithComments.f90");
		
		tokens = new HashSet<>();		
		for (IASTNode node: ast.findAll(ASTNode.class) ) {
			tokens.add(node.findFirstToken());
		}
		
		comments = new ArrayList<>();
		for (ASTToken tk: tokens ) {
			String cmt = tk.getWhiteBefore();
			if (cmt.indexOf('!') >= 0) {
				comments.add(cmt);
			}
		}
	}

	@Test
	public void testNumberComments() {
		assertEquals(2, comments.size());
	}

	@Test
	public void testCommentContent() {
		Iterator<String> cmtsIter = comments.iterator();
		cmtsIter.next(); // skip 1st comment
		assertEquals("  ! Comments again\n", cmtsIter.next());
	}

}
