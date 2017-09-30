package fr.inria.verveine.extractor.fortran.intermodel;

import java.util.ArrayList;
import java.util.Collection;

public class FModel {
	Collection<FEntity> model;
	
	public FModel() {
		model = new ArrayList<>();
	}
	
	public void addEntity(FEntity e) {
		model.add(e);
	}
}
