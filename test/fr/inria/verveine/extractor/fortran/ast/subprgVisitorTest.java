package fr.inria.verveine.extractor.fortran.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class subprgVisitorTest extends AbstractASTTest {
		
	@Before
	public void setup() throws Exception {
		super.setup("test_src/unit-tests/simpleSubPrgs.f90");
	}

	@Test
	public void testFunction() {
		Collection<ASTFunctionSubprogramNode> subs = ast.findAll(ASTFunctionSubprogramNode.class);
		assertEquals(1, subs.size());

		ASTFunctionSubprogramNode sub = subs.iterator().next(); 
		assertEquals("blih", sub.basename());
	}

	@Test
	public void testSubroutine() {
		Collection<ASTSubroutineSubprogramNode> subs = ast.findAll(ASTSubroutineSubprogramNode.class);
		assertEquals(1, subs.size());

		ASTSubroutineSubprogramNode sub = subs.iterator().next(); 
		assertEquals("blah", sub.basename());
	}

}
