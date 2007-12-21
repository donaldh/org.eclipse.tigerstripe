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
package org.eclipse.tigerstripe.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PredicatedList<E> implements List<E> {

	private Predicate predicate = null;
	private List<E> list = new ArrayList<E>();
	private List<E> matchingElements = new ArrayList<E>();

	public PredicatedList() {
	}

	public PredicatedList(Predicate predicate) {
		this.predicate = predicate;
	}

	public boolean add(E o) {
		if (!list.add(o))
			return false;
		if (predicate != null && !predicate.evaluate(o))
			return false;
		matchingElements.add(o);
		return true;
	}

	public void add(int index, E element) {
		// if trying to get an index that outside of the bounds of the list,
		// throw an exception
		if (index > matchingElements.size() || index < 0)
			throw new IndexOutOfBoundsException();
		// otherwise, add the element to the backing store and (if it matches
		// the predicate) to
		// the list of matching elements
		list.add(element);
		if (predicate == null || predicate.evaluate(element))
			matchingElements.add(index, element);
	}

	public boolean addAll(Collection<? extends E> c) {
		// if any elements in the collection evaluate as false in the predicate,
		// don't add anything
		for (E o : c) {
			if (predicate != null && !predicate.evaluate(o))
				return false;
		}
		// if cannot add them all to the list, don't add anything
		if (!list.addAll(c))
			return false;
		// otherwise, loop through and add all of them to the matchingElements
		// list
		for (E o : c) {
			matchingElements.add(o);
		}
		return true;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		// if trying to add to an index that is past the end of the list, throw
		// an exception
		if (index > matchingElements.size() || index < 0)
			throw new IndexOutOfBoundsException();
		// if any elements in the collection evaluate as false in the predicate,
		// don't add anything
		for (E o : c) {
			if (predicate != null && !predicate.evaluate(o))
				return false;
		}
		// if cannot add them all to the list, don't add anything
		if (!list.addAll(c))
			return false;
		// otherwise, loop through and add all of them to the matchingElements
		// list
		int offset = 0;
		for (E o : c) {
			matchingElements.add(index + offset++, o);
		}
		return true;
	}

	public void clear() {
		matchingElements.clear();
		list.clear();
	}

	public boolean contains(Object o) {
		return matchingElements.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return matchingElements.containsAll(c);
	}

	public E get(int index) {
		// if trying to get an index that outside of the bounds of the list,
		// throw an exception
		if (index > matchingElements.size() || index < 0)
			throw new IndexOutOfBoundsException();
		return matchingElements.get(index);
	}

	public int indexOf(Object o) {
		return matchingElements.indexOf(o);
	}

	public boolean isEmpty() {
		return matchingElements.isEmpty();
	}

	public Iterator<E> iterator() {
		return matchingElements.iterator();
	}

	public int lastIndexOf(Object o) {
		return matchingElements.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return matchingElements.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		// if trying to get an index that outside of the bounds of the list,
		// throw an exception
		if (index > matchingElements.size() || index < 0)
			throw new IndexOutOfBoundsException();
		return matchingElements.listIterator(index);
	}

	public boolean remove(Object o) {
		if (!list.remove(o))
			return false;
		return matchingElements.remove(o);
	}

	public E remove(int index) {
		// if trying to get an index that outside of the bounds of the list,
		// throw an exception
		if (index > matchingElements.size() || index < 0)
			throw new IndexOutOfBoundsException();
		E obj = matchingElements.remove(index);
		if (obj != null)
			list.remove(obj);
		return obj;
	}

	public boolean removeAll(Collection<?> c) {
		boolean canRemoveAll = matchingElements.removeAll(c);
		if (!canRemoveAll)
			return false;
		list.removeAll(c);
		return canRemoveAll;
	}

	public boolean retainAll(Collection<?> c) {
		boolean canRetailAll = matchingElements.retainAll(c);
		if (!canRetailAll)
			return false;
		list.retainAll(c);
		return canRetailAll;
	}

	public E set(int index, E element) {
		// if trying to get an index that outside of the bounds of the list,
		// throw an exception
		if (index > matchingElements.size() || index < 0)
			throw new IndexOutOfBoundsException();
		E obj = null;
		if (predicate == null || predicate.evaluate(element))
			obj = matchingElements.set(index, element);
		if (obj != null) {
			list.remove(obj);
			list.add(element);
		}
		return obj;
	}

	public int size() {
		return matchingElements.size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return matchingElements.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return matchingElements.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return matchingElements.toArray(a);
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
		refresh();
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public List<E> getBackingList() {
		return Collections.unmodifiableList(list);
	}

	public void refresh() {
		matchingElements = new ArrayList<E>();
		for (E value : list) {
			if (predicate == null || predicate.evaluate(value))
				matchingElements.add(value);
		}
	}

	public boolean isInScope(Object o) {
		if (matchingElements.indexOf(o) >= 0)
			return true;
		else if (list.indexOf(o) >= 0)
			return false;
		// shouldn't ever get here, but just in case...
		throw new IllegalArgumentException(
				"status of object cannot be determined");
	}

}
