package fr.inria.verveine.extractor.fortran.ir;

import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractIRTest {

	protected IRDictionary dico;

	public void setup(String filename) throws Exception {
		setup(new String[] {filename});
	}

	public void setup(String[] args) throws Exception {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions( args);
		parser.parse();
		dico = parser.getDico();
	}

}
