package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class BodyTest extends AbstractASTTest {

	public static final String SOURCE_CODE =
		"PROGRAM aProgram\n"+
		//"  IMPLICIT NONE\n"+
		"  INTEGER, DIMENSION(1:someVar()%otherVar) :: ntryh\n" + 
		"  save ntryh\n" + 
		"  data ntryh / 4, 2, 3, 5 /\n" + 
		"  var1 = 6\n" +
		"  read(*,*)var2%var3\n" + 
		"END PROGRAM\n";

	@Before
	public void setup() {
		parseCode(SOURCE_CODE);
	}

	@Test
	public void testVariableName() {
		Collection<ASTVariableNode> vars = ast.findAll(ASTVariableNode.class);
		assertEquals(2, vars.size());
		ASTVariableNode var;
		var = vars.iterator().next();
		assertTrue( var.fortranNameToString().equals("var1") ||
					var.fortranNameToString().equals("var2%var3") );
		var = vars.iterator().next();
		assertTrue( var.fortranNameToString().equals("var1") ||
					var.fortranNameToString().equals("var2%var3") );

		Collection<ASTAssignmentStmtNode> asss = ast.findAll(ASTAssignmentStmtNode.class);
		assertEquals(1, asss.size());
		ASTAssignmentStmtNode ass = asss.iterator().next();
		assertEquals( "var1", ass.getLhsVariable().fortranNameToString() );
		assertEquals(ASTWaterExprNode.class, ass.getRhs().getClass());
		assertEquals(0, ((ASTWaterExprNode)ass.getRhs()).getExprMembers().size());
	}

	@Test
	public void testBody() {
		Collection<ASTMainProgramNode> pgms = ast.findAll(ASTMainProgramNode.class);
		assertEquals(1, pgms.size());
		ASTMainProgramNode pgm = pgms.iterator().next();

		assertEquals(3+2, pgm.getBody().size());  // SpecificationPart=3 + ExecutionPart=2
	}

}
