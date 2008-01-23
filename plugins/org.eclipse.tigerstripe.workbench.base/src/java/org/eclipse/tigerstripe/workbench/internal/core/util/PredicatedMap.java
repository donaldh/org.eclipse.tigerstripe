/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PredicatedMap<K, V> implements Map<K, V> {

	private Predicate predicate = null;
	private HashMap<K, V> values = new HashMap<K, V>();
	private HashMap<K, V> matchingValues = new HashMap<K, V>();

	public PredicatedMap() {
	}

	public PredicatedMap(Predicate predicate) {
		this.predicate = predicate;
	}

	public void clear() {
		values.clear();
		matchingValues.clear();
	}

	public boolean containsKey(Object key) {
		return matchingValues.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return matchingValues.containsValue(value);
	}

	public Set<Map.Entry<K, V>> entrySet() {
		return matchingValues.entrySet();
	}

	public V get(Object key) {
		return matchingValues.get(key);
	}

	public boolean isEmpty() {
		return matchingValues.isEmpty();
	}

	public Set<K> keySet() {
		return matchingValues.keySet();
	}

	public V put(K key, V value) {
		// add it to the values map, then if the value passes the predicate
		// add it to the matchingValues map
		values.put(key, value);
		if (predicate != null && !predicate.evaluate(value))
			return null;
		return matchingValues.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> t) {
		// if any of the Map values don't pass the predicate evaluation, add
		// none of them to either the values list or the matchingValues list
		for (V value : t.values()) {
			if (predicate != null && !predicate.evaluate(value))
				return;
		}
		values.putAll(t);
		matchingValues.putAll(t);
	}

	public V remove(Object key) {
		// remove the key (and the object referenced by that key) from both maps
		// (if it exists)
		V oldVal = values.remove(key);
		if (oldVal == null)
			return null;
		return matchingValues.remove(key);
	}

	public int size() {
		return matchingValues.size();
	}

	public Collection<V> values() {
		return matchingValues.values();
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
		refresh();
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public Map<K, V> getBackingMap() {
		return Collections.unmodifiableMap(values);
	}

	public void refresh() {
		matchingValues = new HashMap<K, V>();
		for (K key : values.keySet()) {
			V value = values.get(key);
			if (predicate == null || predicate.evaluate(value))
				matchingValues.put(key, value);
		}
	}

	public boolean isInScope(Object value) {
		if (matchingValues.containsValue(value))
			return true;
		else if (values.containsValue(value))
			return false;
		// shouldn't ever get here, but just in case...
		throw new IllegalArgumentException(
				"status of object cannot be determined");
	}

}
