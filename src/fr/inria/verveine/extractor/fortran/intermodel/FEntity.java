package fr.inria.verveine.extractor.fortran.intermodel;

import org.eclipse.photran.internal.core.lexer.Token;

@SuppressWarnings("restriction")
public class FEntity {
	private String name;
	private String file;
	private int line;
	private Token key;

	public FEntity(Token key, String name) {
		this.name = name;
		this.key = key;
	}

	public Token getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

}
