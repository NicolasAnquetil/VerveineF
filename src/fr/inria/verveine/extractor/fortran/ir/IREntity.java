package fr.inria.verveine.extractor.fortran.ir;

import java.util.Dictionary;
import java.util.Hashtable;

import fr.inria.verveine.extractor.fortran.ast.ASTNode;

public class IREntity {
	// constants used to store data in the entity
	public static final String ENTITY_NAME = "name";
	public static final String DECLARED_PARAM = "declaredParam";
	public static final String CYCLOMATIC_COMPLEXITY = "cyclomaticComplexity";
	public static final String COMMENT_CONTENT = "content";
	public static final String ANCHOR_FILE = "anchorfile";
	public static final String ANCHOR_START = "anchorstart";
	public static final String ANCHOR_END = "anchorend";
	public static final String IS_STUB = "isstub";

	protected IREntity parent;
	protected IRKind kind;
	protected String name;
	protected Dictionary<String, Object> data;

	public IREntity getParent() {
		return parent;
	}

	public IRKind getKind() {
		return kind;
	}

	public IREntity(IREntity parent, IRKind kind) {
		this.parent = parent;
		this.kind = kind;
		this.data = new Hashtable<String, Object>();
	}

	public void data(String key, Object value) {
		data.put(key, value);
	}

	public void name(String name) {
		data(ENTITY_NAME, name);
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
		
		data(ANCHOR_FILE, filename);
		data(ANCHOR_START, node.findFirstToken().getStartIndex());
		data(ANCHOR_END, node.findLastToken().getStopIndex());
	}

	public String getName() {
		return (String) getData(ENTITY_NAME);
	}

	public Object getData(String key) {
		return data.get(key);
	}

}
