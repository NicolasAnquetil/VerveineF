package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;



public class AssignmentTest extends AbstractASTTest {

	public static final String SOURCE_CODE =
			"PROGRAM LE_PROGRAMME\n" +
			" someVar = 1\n" +
			" anotherVar = function(arg1,arg2)\n" +
			"END PROGRAM\n";

	@Test
	public void testAssignment() {
		parseCode(SOURCE_CODE);

		Collection<ASTAssignmentStmtNode> assigns = ast.findAll(ASTAssignmentStmtNode.class);
		assertEquals(2, assigns.size());

		for (ASTAssignmentStmtNode assg : assigns) {
			String varName = assg.getLhsVariable().fortranNameToString();
			if ( varName.equals("someVar")) {
				assertEquals(0, ((ASTWaterExprNode)assg.getRhs()).getExprMembers().size());
			}
			else if ( varName.equals("anotherVar")) {
				assertEquals(1, ((ASTWaterExprNode)assg.getRhs()).getExprMembers().size());
			}
			else {
				fail("Wrong variable name: " + varName);
			}
		}
	}

}
