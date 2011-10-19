package org.eclipse.tigerstripe.annotation.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.annotation.internal.core.WriteCommand;

/**
 * Set of the often used methods in the annotations plugin 
 */
public class Helper {

	/**
	 * Type safe way to run something in a transaction 
	 */
	public static <T> T runForWrite(TransactionalEditingDomain domain,
			final RunnableWithResult<T> runnable) {
		runForWrite(domain, (Runnable) runnable);
		return runnable.getResult();
	}
	
	/**
	 * Convenient way to run something in a transaction on the command stack. 
	 */
	public static void runForWrite(TransactionalEditingDomain domain,
			final Runnable runnable) {
		domain.getCommandStack().execute(new WriteCommand() {

			public void execute() {
				runnable.run();
			}
		});
	}
	
	/**
	 * Run the runnable in the domain, catch the {@link InterruptedException} and log it.
	 */
	public static Object runExclusive(TransactionalEditingDomain domain, Runnable runnable) {
		try {
			return domain.runExclusive(runnable);
		} catch (InterruptedException e) {
			AnnotationPlugin.log(e);
			return null;
		}
	}

	/**
	 * Type safe way to run something in a transaction
	 * Run the runnable in the domain, catch the {@link InterruptedException} and log it. 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T runExclusive(TransactionalEditingDomain domain, RunnableWithResult<T> runnable) {
		try {
			return (T) domain.runExclusive(runnable);
		} catch (InterruptedException e) {
			AnnotationPlugin.log(e);
			return null;
		}
	}
	
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
