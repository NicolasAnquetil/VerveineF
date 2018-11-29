package fr.inria.verveine.extractor.fortran.visitors;


import ch.akuhn.fame.Repository;
import fr.inria.verveine.extractor.fortran.VerveineFParser;

public class AbstractFunctionalTest {

	protected Repository repo;

	public void setup(String filename) throws Exception {
		VerveineFParser parser = new VerveineFParser();
		parser.setOptions( new String[] {filename});
		parser.parse();
		repo = parser.getFamixRepo();
	}

}