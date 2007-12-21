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
package org.eclipse.tigerstripe.workbench.ui.gmf.synchronization;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;

/**
 * This is an index for diagram handles indexed by FQN. At any given time, this
 * index can be used by a ProjectDiagramSynchronizer to figure out which diagram
 * to update based on a targetFQN
 * 
 * @author Eric Dillon
 * 
 */
/* package */class DiagramHandleIndex {

	private HashMap<String, Set<DiagramHandle>> handlesByFQN = new HashMap<String, Set<DiagramHandle>>();

	private HashMap<DiagramHandle, Set<String>> handleContent = new HashMap<DiagramHandle, Set<String>>();

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private ReadLock readLock = lock.readLock();

	private WriteLock writeLock = lock.writeLock();

	/* package */DiagramHandleIndex(ProjectDiagramsSynchronizer synchronizer) {
	}

	/**
	 * Adds a new DiagramHandle entry indexed by FQN
	 * 
	 * @param fqn
	 * @param handle
	 */
	protected void put(String fqn, DiagramHandle handle) {
		Set<DiagramHandle> handleList = handlesByFQN.get(fqn);
		if (handleList == null) {
			handleList = new HashSet<DiagramHandle>();
			handlesByFQN.put(fqn, handleList);
		}

		handleList.add(handle);
	}

	/**
	 * A callback method to update the index based on the content of the given
	 * diagram that has just been saved
	 * 
	 * @param handle
	 * @throws TigerstripeException
	 */
	public void diagramSaved(DiagramHandle handle) throws TigerstripeException {
		try {
			writeLock.lock();
			// un-index the previous content if it is known
			diagramDeleted(handle);

			Set<String> fqns = getDiagramContent(handle);
			for (String fqn : fqns) {
				put(fqn, handle);
			}
			handleContent.put(handle, fqns);
			// System.out.println("Indexed: " +
			// handle.getDiagramResource().getFullPath());
		} catch (TigerstripeException e) {
			throw e;
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * A callback method to update the index based on the fact that a Diagram
	 * was deleted
	 * 
	 * @param handle
	 * @throws TigerstripeException
	 */
	public void diagramDeleted(DiagramHandle handle)
			throws TigerstripeException {
		try {
			writeLock.lock();
			// un-index the previous content if it is known
			Set<String> previousFQNs = handleContent.get(handle);
			if (previousFQNs != null) {
				for (String fqn : previousFQNs) {
					Set<DiagramHandle> handles = handlesByFQN.get(fqn);
					if (handles == null) {
						// the index is corrupted, should get here
						TigerstripeRuntime
								.logWarnMessage("Diagram index is corrupted: "
										+ this);
					} else {
						handles.remove(handle);
					}
				}
				// System.out.println("Un-indexed: " +
				// handle.getDiagramResource().getFullPath());
			}
		} catch (Exception e) {
			throw new TigerstripeException("Error: " + e.getMessage(), e);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * Get all the diagram entries for the given FQN
	 * 
	 * @param fqn
	 * @return
	 */
	public Set<DiagramHandle> get(String fqn) {
		try {
			readLock.lock();
			Set<DiagramHandle> result = handlesByFQN.get(fqn);
			if (result == null) {
				result = new HashSet<DiagramHandle>();
			}

			return Collections.unmodifiableSet(result);
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * Returns a set of all the FQNs contained in the given diagram.
	 * 
	 * @param handle
	 * @return
	 * @throws TigerstripeException
	 */
	private Set<String> getDiagramContent(DiagramHandle handle)
			throws TigerstripeException {
		IModelFileContentReader reader = ModelFileContentReaderFactory
				.make(handle);
		if (reader != null)
			return reader.getFQNs(handle);

		throw new TigerstripeException("Couldn't create reader for "
				+ handle.getModelResource().getFullPath() + " content.");
	}
}
