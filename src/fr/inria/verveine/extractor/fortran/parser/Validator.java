package fr.inria.verveine.extractor.fortran.parser;

import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

/**
 * Helper class for island grammar parsing: Allows to pop many entries from the parsingContext valuesStack
 */
public abstract class Validator {
	public abstract boolean validate(IASTNode node);
}