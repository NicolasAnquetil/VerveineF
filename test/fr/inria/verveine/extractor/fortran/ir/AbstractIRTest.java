package fr.inria.verveine.extractor.fortran.ir;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractIRTest {

	protected IRDictionary dico;

	public void setup(String filename) throws Exception {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions( new String[] {filename});
		parser.parse();
		dico = parser.getDico();
	}

}
