package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class CommentVisitorTest extends AbstractIrTest {

	public static final String SOURCE_CODE = 
			"MODULE simpleModuleWithComments\n" + 
			"CONTAINS\n" + 
			"  ! routine comment\n" + 
			"  SUBROUTINE blah()\n" + 
			"  ! var comment\n" +
			"  INTEGER :: i\n" +
			"  ! cmt inside routine\n" +
			"  END SUBROUTINE blah\n" + 
			"  ! module comment\n" + 
			"END MODULE";

	@Test
	public void testCommentOfLocalVar() {
		int modCmt = 0;
		int subCmt = 0;
		int varCmt = 0;

		parseCode(new String[] {VerveineFParser.ALLLOCALS_OPTION}, SOURCE_CODE);

		Collection<IREntity> allCmts = dico.allWithKind(IRKind.COMMENT);
		assertEquals(4, allCmts.size());
		for (IREntity cmt : allCmts) {
			assertNotNull("Comment without parent entity", cmt.getParent());
			if( cmt.getParent().getKind().equals(IRKind.MODULE)) {
				modCmt++;
			}
			else if( cmt.getParent().getKind().equals(IRKind.SUBPROGRAM)) {
				subCmt++;
			}
			else if( cmt.getParent().getKind().equals(IRKind.VARIABLE)) {
				varCmt++;
			}
		}
		assertEquals(1, modCmt);
		assertEquals(2, subCmt);
		assertEquals(1, varCmt);
	}

	@Test
	public void testCommentInLocalVarParent() {
		int modCmt = 0;
		int subCmt = 0;

		parseCode( SOURCE_CODE);

		Collection<IREntity> allCmts = dico.allWithKind(IRKind.COMMENT);
		assertEquals(4, allCmts.size());
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
		assertEquals(3, subCmt);
	}

	@Test
	public void testCommentContent() {
		boolean commentFound = false;

		parseCode(new String[] {VerveineFParser.ALLLOCALS_OPTION}, SOURCE_CODE);

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
