package fr.inria.verveine.extractor.fortran.ir;

import fr.inria.verveine.extractor.fortran.AbstractFortranExtractorTest;

public abstract class AbstractIrTest extends AbstractFortranExtractorTest {

	protected IRDictionary dico;

	protected void parseCode(String[] args) {
		super.parseCode(args);
		dico = parser.getDico();
	}

}
