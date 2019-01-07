package fr.inria.verveine.extractor.fortran;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.VerveineFortranStream.StringTree;

public class StringTreeTest {

	@Test
	public void testMacroAsTerm() {
		StringTree tree = new StringTree("macro", StringTree.TERM);
		assertEquals("macro", tree.getValue());
		assertNull(tree.getLeft());
		assertNull(tree.getRight());
	}

	@Test
	public void testMacroAsComparison() {
		StringTree tree = new StringTree("macro", StringTree.COMPARISON);
		assertEquals("macro", tree.getValue());
		assertNull(tree.getLeft());
		assertNull(tree.getRight());
	}

	@Test
	public void testMacroAsBoolean() {
		StringTree tree = new StringTree("macro", StringTree.BOOLEAN);
		assertEquals("macro", tree.getValue());
		assertNull(tree.getLeft());
		assertNull(tree.getRight());
	}

	@Test
	public void testComparisonAsComparison() {
		StringTree tree = new StringTree("macro == 1", StringTree.COMPARISON);
		assertEquals("==", tree.getValue());
		assertEquals("macro", tree.getLeft().getValue());
		assertEquals("1", tree.getRight().getValue());
	}

	@Test
	public void testComparisonAsBoolean() {
		StringTree tree = new StringTree("macro == 1", StringTree.BOOLEAN);
		assertEquals("==", tree.getValue());
		assertEquals("macro", tree.getLeft().getValue());
		assertEquals("1", tree.getRight().getValue());
	}

	@Test
	public void testBooleanAsBoolean() {
		StringTree tree = new StringTree("macro == 1 || macro != 2", StringTree.BOOLEAN);
		assertEquals("||", tree.getValue());
		assertEquals("==", tree.getLeft().getValue());
		assertEquals("!=", tree.getRight().getValue());
	}

}
