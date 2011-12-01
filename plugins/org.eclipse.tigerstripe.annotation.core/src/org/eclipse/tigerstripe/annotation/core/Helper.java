package org.eclipse.tigerstripe.annotation.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;

/**
 * Set of the often used methods in the annotations plugin 
 */
public class Helper {

	
	/**
	 * @return null if the given list is empty or first element 
	 */
	public static <T> T firstOrNull(List<T> list) {
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	/**
	 * Single point to know the URI of the given resource 
	 */
	public static URI makeUri(IResource resource) {
		return URI.createPlatformResourceURI(resource.getFullPath().toString(),
				false);
	}
	
	/**
	 * Simple method to convert collection to array.
	 */
	public static <T> T[] toArray(Class<T> type, Collection<T> c) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(type, c.size());
		c.toArray(array);
		return array;
	}
	
	/**
	 * Add element to the list in the map for given key. If list doesn't exist create it. 
	 */
	public static <K, V> void addToListInMap(Map<K, List<V>> map, K key, V value) {
		List<V> list = map.get(key);
		if (list == null) {
			list = new ArrayList<V>();
			map.put(key, list);
		}
		list.add(value);
	}
}
