package fr.inria.verveine.extractor.fortran.parser;

import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

/**
 * Helper class for island grammar parsing: Allows to pop all entries that are not {@link #IASTNode.isTopLevel()} from the parsingContext valuesStack
 */
public class UntilTopEntityValidator extends Validator {

	public boolean validate(IASTNode node) {
		return ! node.isTopLevelNode();
	}
}