package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

public class CommentTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "! This is a simple Module\n" + 
			"! It has comments\n" + 
			"MODULE simpleModuleWithComments\n" + 
			"  ! Comments again\n" + 
			"END MODULE";

	private Set<ASTToken> tokens;
	private List<String> comments;

	@Before
	public void setUp() throws Exception {
		parseCode(SOURCE_CODE);
		
		tokens = new HashSet<>();		
		for (IASTNode node: ast.findAll(ASTNode.class) ) {
			tokens.add(node.findFirstToken());
		}
		
		comments = new ArrayList<>();
		for (ASTToken tk: tokens ) {
			tk.setWhiteBefore(null); // removes the empty comments introduced by CommentVisitor
			// so that we have access to the comments stored in the actualToken. See ASTToken
			String cmt = tk.getActualToken().getWhiteText();
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
		// order of comments might be inverted, so we need to test both
		assertTrue( cmtsIter.next().equals("  ! Comments again\n") ||
					cmtsIter.next().equals("  ! Comments again\n"));
	}

}
