/**
 * Copyright (c) 2002-2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *   xored software, Inc. - initial API and Implementation (Yuri Strot) 
 */
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ExtensibleURIConverterImpl$OptionsMap
 * 
 * @author Yuri Strot
 */
public class OptionsMap implements Map<Object, Object> {
	
	protected Object key;
	protected Object value;
	protected Map<?, ?> options;
	protected Map<Object, Object> mergedMap;

	public OptionsMap(Object key, Object value, Map<?, ?> options) {
		this.options = options == null ? Collections.EMPTY_MAP : options;
		this.key = key;
		this.value = value;
	}

	protected Map<Object, Object> mergedMap() {
		if (mergedMap == null) {
			mergedMap = new LinkedHashMap<Object, Object>(options);
			mergedMap.put(key, value);
		}
		return mergedMap;
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		return this.key == key || this.key.equals(key)
				|| options.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.value == value || options.containsValue(value);
	}

	public Set<Map.Entry<Object, Object>> entrySet() {
		return mergedMap().entrySet();
	}

	public Object get(Object key) {
		return this.key == key || this.key.equals(key) ? value : options
				.get(key);
	}

	public boolean isEmpty() {
		return false;
	}

	public Set<Object> keySet() {
		return mergedMap().keySet();
	}

	public Object put(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends Object, ? extends Object> t) {
		throw new UnsupportedOperationException();
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return mergedMap().size();
	}

	public Collection<Object> values() {
		return mergedMap().values();
	}

	@Override
	public int hashCode() {
		return mergedMap().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return mergedMap().equals(0);
	}
}
