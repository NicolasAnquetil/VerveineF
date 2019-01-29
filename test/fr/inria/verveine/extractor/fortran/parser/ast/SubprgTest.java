package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class SubprgTest extends AbstractASTTest {

	public static final String SOURCE_CODE =
			"MODULE simpleModule\n" + 
			"CONTAINS\n" + 

			"    SUBROUTINE blah()\n" + 
			"		INTEGER i\n" + 
			"		i = blih()\n" + 
			" 		i = i+1\n" + 
			"    END SUBROUTINE blah\n" + 

			"	function blih()  result(i)\n" + 
			"		INTEGER :: i\n" + 
			"		CALL blah()\n" + 
			"		i = 0\n" + 
			"	END function blih\n" + 

			"END MODULE";

	@Before
	public void setup() {
		parseCode(SOURCE_CODE);
	}

	@Test
	public void testFunction() {
		Collection<ASTFunctionSubprogramNode> subs = ast.findAll(ASTFunctionSubprogramNode.class);
		assertEquals(1, subs.size());

		ASTFunctionSubprogramNode sub = subs.iterator().next();

		
		assertEquals( "blih", sub.basename() );
	}

	@Test
	public void testSubroutine() {
		Collection<ASTSubroutineSubprogramNode> subs = ast.findAll(ASTSubroutineSubprogramNode.class);
		assertEquals(1, subs.size());

		ASTSubroutineSubprogramNode sub = subs.iterator().next(); 
		assertEquals("blah", sub.basename());
	}

	@Test
	public void testCallStmt() {
		Collection<ASTCallStmtNode> calls = ast.findAll(ASTCallStmtNode.class);
		assertEquals(1, calls.size());

		ASTCallStmtNode stmt = calls.iterator().next(); 
		assertEquals("blah", stmt.getSubroutineName().getText());
	}

	@Test
	public void testVarOrFunctionRef() {
		int nbI = 0;
		int nbBlih = 0;

		Collection<ASTDataRefNode> refs = ast.findAll(ASTDataRefNode.class);
		assertEquals(5, refs.size());

		for (ASTDataRefNode ref : refs) {
			if (ref.fortranNameToString().equals("i")) {
				nbI++;
			}
			else if(ref.fortranNameToString().equals("blih")) {
				nbBlih++;
			}
			else {
				fail("Unknown DataRef name " + ref.fortranNameToString());
			}
		}
		
		assertEquals(4, nbI);
		assertEquals(1, nbBlih);
	}

	@Test
	public void testParents() {
		for (IASTNode parent : ast.findAll(ASTNode.class)) {
			for (IASTNode child : parent.getChildren()) {
				if (! (child instanceof ASTNullNode)) {
					assertEquals(parent, child.getParent());
				}
			}
		}
		
	}

}
