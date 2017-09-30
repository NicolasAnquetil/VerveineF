package fr.inria.verveine.extractor.fortran.intermodel;

import org.eclipse.photran.internal.core.lexer.Token;

@SuppressWarnings("restriction")
public class FCall extends FEntity {

	private FSubprgm called;

	public FSubprgm getCalled() {
		return called;
	}
	

	public void setCalled(FSubprgm called) {
		this.called = called;
	}
	

	public FCall(Token key, String name) {
		super(key, name);
	}

}
