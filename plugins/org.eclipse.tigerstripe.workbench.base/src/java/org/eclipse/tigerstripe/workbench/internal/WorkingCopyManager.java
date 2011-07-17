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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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

	protected WorkingCopyManager() {
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

		CommitEvent commitEvent = new CommitEvent(this, original);
		doCommit(monitor);

		OriginalChangeEvent event = new OriginalChangeEvent(
				OriginalChangeEvent.ORIGINAL_CHANGED);
		notifyListeners(event);
		fireCommitListeners(commitEvent);
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

	public void assertSet() throws WorkingCopyException, TigerstripeException {
		if (!isWorkingCopy()) {
			throw new WorkingCopyException(
					"Please get a workingCopy to perform set");
		} else if (wasDisposed()) {
			throw new WorkingCopyException(
					"This working copy was already disposed.");
		}
	}

	public abstract boolean isDirty();
	

	private static final Set<CommitListener> commitListeners = new LinkedHashSet<CommitListener>();

	public static void addCommitListener(CommitListener listener) {
		commitListeners.add(listener);
	}

	public static void removeCommitListener(CommitListener listener) {
		commitListeners.remove(listener);
	}

	public static void fireCommitListeners(CommitEvent event) {
		for (CommitListener l : commitListeners) {
			l.onCommit(event);
		}
	}
	
	public static interface CommitListener {
		void onCommit(CommitEvent event);
	}
	
	public static class CommitEvent {
		private final IWorkingCopy workingCopy;
		private final Object original;
		
		public CommitEvent(IWorkingCopy workingCopy, Object original) {
			this.workingCopy = workingCopy;
			this.original = original;
		}
		public IWorkingCopy getWorkingCopy() {
			return workingCopy;
		}
		public Object getOriginal() {
			return original;
		}
	}
}
