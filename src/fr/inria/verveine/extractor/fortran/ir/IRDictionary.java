package fr.inria.verveine.extractor.fortran.ir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class IRDictionary implements Iterable<IREntity> {
	protected Map<String,IREntity> entities;
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
		IREntity entity = new IREntity(parent, kind, key);
		addEntity(key, entity);
		return entity;
	}
	
	/**
	 * Adds an "anonymous" entity, i.e. entity with no key, that cannot, therefore, be recovered independently 
	 */
	public IREntity addAnonymousEntity( IRKind kind, IREntity parent) {
		return addEntity( "_anonymous_" + (++anonymousCounter), kind, parent );
	}
	
	public Collection<IREntity> allWithKind (IRKind kind) {
		Collection<IREntity> selected = new ArrayList<IREntity>();
		
		for (IREntity ent : entities.values()) {
			if (ent.getKind() == kind) {
				selected.add(ent);
			}
		}
		return selected;
	}

	@Override
	public Iterator<IREntity> iterator() {
		return entities.values().iterator();
	}
}
