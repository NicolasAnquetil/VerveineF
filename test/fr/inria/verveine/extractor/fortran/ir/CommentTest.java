package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class CommentTest extends AbstractIrTest {

	public static final String SOURCE_CODE = 
			"MODULE simpleModuleWithComments\n" + 
			"CONTAINS\n" + 
			"  ! routine comment\n" + 
			"  SUBROUTINE blah()\n" + 
			"  ! cmt inside routine\n" +
			"  END SUBROUTINE blah\n" + 
			"  ! module comment\n" + 
			"END MODULE";

	@Before
	public void setUp() throws Exception {
		parseCode(SOURCE_CODE);
	}

	@Test
	public void testCommentInParent() {
		int modCmt = 0;
		int subCmt = 0;

		Collection<IREntity> allCmts = dico.allWithKind(IRKind.COMMENT);
		assertEquals(3, allCmts.size());
		for (IREntity cmt : allCmts) {
			assertNotNull("Comment without parent entity", cmt.getParent());
			if( cmt.getParent().getKind().equals(IRKind.MODULE)) {
				modCmt++;
			}
			else if( cmt.getParent().getKind().equals(IRKind.SUBPROGRAM)) {
				subCmt++;
			}
		}
		assertEquals(1, modCmt);
		assertEquals(2, subCmt);
	}

	@Test
	public void testCommentContent() {
		boolean commentFound = false;
		for (IREntity cmt : dico.allWithKind(IRKind.COMMENT) ) {
			commentFound =  true;
			assertNotNull("Comment without parent entity", cmt.getParent());
			if( cmt.getParent().getKind().equals(IRKind.MODULE)) {
				assertEquals("  ! module comment\n", cmt.getData(IREntity.COMMENT_CONTENT));
			}
			else if( cmt.getParent().getKind().equals(IRKind.SUBPROGRAM)) {
				assertTrue( cmt.getData(IREntity.COMMENT_CONTENT).equals("  ! routine comment\n  ") ||
							cmt.getData(IREntity.COMMENT_CONTENT).equals("  ! cmt inside routine\n  ") );
			}
		}
		assertTrue("No comment found", commentFound);
	}

}
