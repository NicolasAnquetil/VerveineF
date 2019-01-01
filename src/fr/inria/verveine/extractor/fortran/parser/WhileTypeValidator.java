package fr.inria.verveine.extractor.fortran.parser;

import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

/**
 * Helper class for island grammar parsing: Allows to pop all entries of a given type from the parsingContext valuesStack
 */
public class WhileTypeValidator extends Validator {
	Class<? extends IASTNode> clazz;

	public WhileTypeValidator(Class<? extends IASTNode> clazz) {
		this.clazz = clazz;
	}

	public boolean validate(IASTNode node) {
		return clazz.isInstance(node);
	}
}