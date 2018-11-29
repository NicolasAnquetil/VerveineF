package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fr.inria.verveine.extractor.fortran.VerveineFAbstractTest;

public class CommentTest extends VerveineFAbstractTest {

	@Before
	public void setUp() throws Exception {
		super.setup("test_src/unit-tests/simpleModuleWithComments.f90");
	}

	@Test
	public void testNumberComments() {
		
		ArrayList<String> comments = new ArrayList<>();

		for (IASTNode node: ast.findAll(ASTNode.class) ) {
			String cmt = node.findFirstToken().getWhiteBefore();
			int start = cmt.indexOf('!');
			if (start >= 0) {
				comments.add(cmt);
			}
		}
		
		assertEquals(4, comments.size());
	}

	
}
