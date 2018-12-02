package fr.inria.verveine.extractor.fortran.ir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class IRDictionary {
	protected Dictionary<String,IREntity> entities;
	protected IREntity root;
	protected int anonymousCounter;

	public IRDictionary() {
		entities = new Hashtable<>();
		root = null;
		anonymousCounter = 0;
	}

	public IREntity getRoot() {
		return root;
	}
	
	public IREntity getEntityByKey(String key) {
		return entities.get(key);
	}

	public void addEntity(String key, IREntity entity) {
		entities.put(key, entity);
		if (root == null) {
			root = entity;
		}
	}

	public IREntity addEntity(String key, IRKind kind, IREntity parent) {
		IREntity entity = new IREntity(parent, kind);
		addEntity(key, entity);
		return entity;
	}
	
	public Collection<IREntity> allWithKind (IRKind kind) {
		Collection<IREntity> selected = new ArrayList<IREntity>();
		Enumeration<IREntity> iter = entities.elements();
		while (iter.hasMoreElements()) {
			IREntity e = iter.nextElement();
			if (e.getKind() == kind) {
				selected.add(e);
			}
		}
		return selected;
	}

	/**
	 * Adds an "anonymous" entity, i.e. entity with no key, that cannot, therefore, be recovered independently 
	 */
	public void addAnonymousEntity(IREntity entity) {
		addEntity( "_anonymous_" + (++anonymousCounter), entity );
	}
}
