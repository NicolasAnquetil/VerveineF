package fr.inria.verveine.extractor.fortran.parser;

import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

/**
 * Helper class for island grammar parsing: Pops a given number of entries from the parsingContext valuesStack
 */
public class CountValidator extends Validator {
	int count;
	public CountValidator(int count) { this.count = count; }
	public boolean validate(IASTNode node) { return count-- > 0; }
}