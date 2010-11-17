/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Cache<K, V> {

	private final ConcurrentMap<K, V> cache = new ConcurrentHashMap<K, V>();

	private final Creator<K, V> creator;

	private boolean disabled;

	public Cache(Creator<K, V> creator) {
		this.creator = creator;
	}

	public V get(K key) {

		// Don't use cache
		if (disabled) {
			return creator.create(key);
		}

		V value = cache.get(key);

		if (value == null) {
			V newValue = creator.create(key);

			if (newValue == null) {
				throw new IllegalStateException("Create can't return null");
			}

			value = cache.putIfAbsent(key, newValue);
			if (value == null) {
				value = newValue;
			}
		}
		return value;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void reset() {
		cache.clear();
	}

	public static interface Creator<K, V> {

		V create(K key);

	}
}