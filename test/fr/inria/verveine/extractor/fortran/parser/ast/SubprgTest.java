package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTCallStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTVarOrFnRefNode;

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
		Collection<ASTVarOrFnRefNode> calls = ast.findAll(ASTVarOrFnRefNode.class);
		assertEquals(2, calls.size());

		Iterator<ASTVarOrFnRefNode> iter = calls.iterator(); 
		assertTrue( iter.next().getName().getText().equals("blih") || iter.next().getName().getText().equals("blih"));
	}

}
