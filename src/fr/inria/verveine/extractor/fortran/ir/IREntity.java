package fr.inria.verveine.extractor.fortran.ir;

import java.util.Hashtable;
import java.util.Map;

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

	protected String key;
	protected IREntity parent;
	protected IRKind kind;
	protected Map<String, Object> data;

	public IREntity(IREntity parent, IRKind kind) {
		this(parent, kind, /*key*/null);
	}

	public IREntity(IREntity parent, IRKind kind, String key) {
		this.key = key;
		this.parent = parent;
		this.kind = kind;
		this.data = new Hashtable<String, Object>();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public IREntity getParent() {
		return parent;
	}

	public IRKind getKind() {
		return kind;
	}

	public void name(String name) {
		data(ENTITY_NAME, name);
	}

	public String getName() {
		return (String) getData(ENTITY_NAME);
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

	public void data(String key, Object value) {
		data.put(key, value);
	}

	public Object getData(String key) {
		return data.get(key);
	}

	public Map<String, Object> getData() {
		return data;
	}

}
