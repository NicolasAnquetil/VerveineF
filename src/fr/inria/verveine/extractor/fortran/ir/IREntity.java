package fr.inria.verveine.extractor.fortran.ir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import fr.inria.verveine.extractor.fortran.ast.ASTNode;

public class IREntity {
	protected IREntity parent;
	protected IRKind kind;
	public IREntity getParent() {
		return parent;
	}

	public IRKind getKind() {
		return kind;
	}

	protected String name;
	protected Collection<IREntity> children;
	protected Dictionary<String, String> data;

	public IREntity(IREntity parent, IRKind kind) {
		this.parent = parent;
		this.kind = kind;
		this.children = new ArrayList<IREntity>();
		this.data = new Hashtable<String, String>();
	}

	public void data(String key, String value) {
		data.put(key, value);
	}

	public void name(String name) {
		data("name", name);
	}

	public void stub(boolean stub) {
		data("isstub", stub ? "true" : "false");
	}

	/**
	 * Adds location information to an Entity.
	 * Location informations are: <b>name</b> of the source file and <b>position</b> in this file.
	 * @param filename -- name of the file being visited
	 * @param ast -- ASTNode, where the information are extracted
	 */
	public void addSourceAnchor(String filename, ASTNode node) {
		
		if (node == null) {
			return;
		}
		
		data("anchorfile", filename);
		data("anchorstart", ""+ node.findFirstToken().getStartIndex());
		data("anchorend", ""+ node.findLastToken().getStopIndex());
	}

	public String getName() {
		return getData("name");
	}

	public String getData(String key) {
		return data.get(key);
	}

}
