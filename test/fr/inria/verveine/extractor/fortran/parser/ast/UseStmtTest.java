package fr.inria.verveine.extractor.fortran.parser.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

public class UseStmtTest extends AbstractASTTest {

	public static final String SOURCE_CODE = "MODULE simpleModule\n" + 
			"use used1\n" + 
			"use used2\n" + 
			"END MODULE\n";

	@Test
	public void testUseStmt() {
		boolean use1 = false;
		boolean use2 = false;

		parseCode(SOURCE_CODE);

		Collection<ASTUseStmtNode> uses = ast.findAll(ASTUseStmtNode.class);
		assertEquals(2, uses.size());

		for (ASTUseStmtNode use : uses) {
			if (use.getName().getText().equals("used1")) {
				use1 = true;
			}
			else if (use.getName().getText().equals("used2")) {
				use2 = true;
			}
			else {
				fail("Unknown used module "+use.getName().getText());
			}
		}

		assertTrue("Used module 'used1' not found", use1);
		assertTrue("Used module 'used1' not found", use2);
	}

}
