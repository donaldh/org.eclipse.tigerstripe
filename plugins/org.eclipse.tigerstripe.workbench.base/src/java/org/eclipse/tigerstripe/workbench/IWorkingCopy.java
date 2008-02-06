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
package org.eclipse.tigerstripe.workbench;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Generic interface used to provide a way to edit an object in a "working copy"
 * and commit all changes in a
 * 
 * @author erdillon
 * 
 */
public interface IWorkingCopy {

	public boolean isWorkingCopy();

	public boolean isDirty();

	/**
	 * Creates a deep-clone of the original object to return a "working copy"
	 * that can be used for editing.
	 * 
	 * If called on a "working copy" returns a new deep-cloned version of the
	 * original. Note that if the original had changed, the original in is
	 * current state is used to perform the deep-clone, i.e. it is equivalent to
	 * getOriginal().makeWorkingCopy(...)
	 * 
	 * @param monitor
	 * @return
	 * @throws TigerstripeException
	 */
	public IWorkingCopy makeWorkingCopy(IProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * Returns the original object that was used to create this working copy
	 * Note that this is the original object in its current state, not in
	 * necessarily in the state it was at the time the working copy was created
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public Object getOriginal() throws TigerstripeException;

	/**
	 * This call-back mechanism can be used by the client to be notified when
	 * the original object has changed.
	 * 
	 * @param listener -
	 *            the listener to register, if already registered it will be
	 *            ignored
	 */
	public void addOriginalChangeListener(IOriginalChangeListener listener);

	/**
	 * Removes a listener from the list. If not in the list, this method call is
	 * ignored.
	 * 
	 * @param listener
	 */
	public void removeOriginalChangeListener(IOriginalChangeListener listener);

	/**
	 * Commits the changes in this "working copy" to the original. This will
	 * perform all the necessary steps to persist the values in the "working
	 * copy" to the original.
	 * 
	 * If
	 * 
	 * @param monitor
	 * @throws TigerstripeException
	 */
	public void commit(IProgressMonitor monitor) throws TigerstripeException;

	/**
	 * Disposes of this working copy.
	 * 
	 * Clients MUST call this method when they are done using a working copy to
	 * avoid that these copies all remain in memory
	 */
	public void dispose();
}
