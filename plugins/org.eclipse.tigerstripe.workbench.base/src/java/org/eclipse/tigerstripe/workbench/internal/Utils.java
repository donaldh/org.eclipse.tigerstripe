/*******************************************************************************
 * Copyright (c) 2011 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Valentin Erastov)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utils {

	public static <K, V> void addToSetInMap(Map<K, Set<V>> map, K key, V value) {
		Set<V> set = map.get(key);
		if (set == null) {
			set = new HashSet<V>();
			map.put(key, set);
		}
		set.add(value);
	}
	
	public static <K, V> void removeFromSetInMap(Map<K, Set<V>> map, K key, V value) {
		Set<V> set = map.get(key);
		if (set != null) {
			set.remove(value);
		}
	}
}
