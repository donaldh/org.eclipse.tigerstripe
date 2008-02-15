/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.IOriginalChangeListener;
import org.eclipse.tigerstripe.workbench.IWorkingCopy;
import org.eclipse.tigerstripe.workbench.OriginalChangeEvent;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;

/**
 * This class can be used by all objects implementing IWorkingCopy to delegate
 * most of the functionalities
 * 
 * @author erdillon
 * 
 */
public abstract class WorkingCopyManager implements IWorkingCopy {

	protected final static String _UNKWOWN_FIELD = "_UNKWOWN_FIELD";

	private boolean isWorkingCopy = false;

	private WorkingCopyManager original;

	private Set<IOriginalChangeListener> listeners = new HashSet<IOriginalChangeListener>();
	private Set<IWorkingCopy> copies = new HashSet<IWorkingCopy>();

	private BitSet fieldDirtyStatus = new BitSet(0);
	protected Vector<String> managedFields = new Vector<String>();

	protected WorkingCopyManager() {
		addManagedFields();
		resetDirtyFields();
	}

	/**
	 * Returns true if the given field is dirty.
	 * 
	 * If the field is not a managed field, a TigerstripeException is thrown If
	 * this is not a working copy this method always returns true;
	 * 
	 * @param fieldName
	 * @return
	 * @throws TigerstripeException
	 */
	public boolean fieldIsDirty(String fieldName) throws TigerstripeException {
		if (!isWorkingCopy())
			return true;

		int index = managedFields.indexOf(fieldName);
		if (index == -1)
			throw new TigerstripeException("Field: " + fieldName
					+ " is not managed.");

		return fieldDirtyStatus.get(index);
	}

	public WorkingCopyManager getOriginal() {
		return this.original;
	}

	protected void setOriginal(WorkingCopyManager original) {
		this.original = original;
	}

	public boolean isWorkingCopy() {
		return isWorkingCopy;
	}

	protected void setWorkingCopy(boolean isWorkingCopy) {
		this.isWorkingCopy = isWorkingCopy;
	}

	protected abstract WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException;

	protected abstract void doCommit(IProgressMonitor monitor)
			throws TigerstripeException;

	protected abstract void addManagedFields();

	public IWorkingCopy makeWorkingCopy(IProgressMonitor monitor)
			throws TigerstripeException {

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		if (isWorkingCopy()) {
			return getOriginal().makeWorkingCopy(monitor);
		}

		WorkingCopyManager copy = (WorkingCopyManager) doCreateCopy(monitor);
		copy.setWorkingCopy(true);
		copy.setOriginal(this);
		copies.add(copy);

		return copy;
	}

	/**
	 * This method performs a commit by calling the internalCommit
	 * 
	 * @param monitor
	 * @throws TigerstripeException
	 */
	public void commit(IProgressMonitor monitor) throws TigerstripeException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		if (!isDirty())
			return;

		if (!isWorkingCopy())
			throw new TigerstripeException("Commit was called on original.");

		if (wasDisposed()) {
			throw new TigerstripeException("This working copy was disposed.");
		}

		doCommit(monitor);

		OriginalChangeEvent event = new OriginalChangeEvent(
				OriginalChangeEvent.ORIGINAL_CHANGED);
		notifyListeners(event);

		resetDirtyFields();
	}

	private void dispose(IWorkingCopy copy) {
		copies.remove(copy);
	}

	public boolean wasDisposed() {
		return isWorkingCopy() && getOriginal() == null;
	}

	public void dispose() {
		if (isWorkingCopy && !wasDisposed()) {
			((WorkingCopyManager) getOriginal()).dispose(this);
			original = null;
		}
	}

	public void addOriginalChangeListener(IOriginalChangeListener listener) {
		listeners.add(listener);
	}

	public void removeOriginalChangeListener(IOriginalChangeListener listener) {
		listeners.remove(listener);
	}

	private void notifyListeners(OriginalChangeEvent changeEvent) {
		for (IOriginalChangeListener listener : listeners) {
			listener.originalChanged(changeEvent);
		}
	}

	public void assertSet(String fieldID) throws WorkingCopyException,
			TigerstripeException {

		int index = managedFields.indexOf(fieldID);

		if (index == -1)
			throw new TigerstripeException("Field: " + fieldID
					+ " is not managed.");

		if (!isWorkingCopy()) {
			throw new WorkingCopyException(
					"Please get a workingCopy to perform set");
		} else if (wasDisposed()) {
			throw new WorkingCopyException(
					"This working copy was already disposed.");
		}

		markFieldDirty(fieldID);
	}

	protected void markFieldDirty(String fieldID) throws TigerstripeException {
		int index = managedFields.indexOf(fieldID);
		if (index == -1)
			throw new TigerstripeException("Field '" + fieldID
					+ "' not managed.");
		fieldDirtyStatus.set(index);
	}

	public boolean isDirty() {
		return !fieldDirtyStatus.isEmpty();
	}

	private void resetDirtyFields() {
		fieldDirtyStatus = new BitSet(managedFields.size());
	}
}
