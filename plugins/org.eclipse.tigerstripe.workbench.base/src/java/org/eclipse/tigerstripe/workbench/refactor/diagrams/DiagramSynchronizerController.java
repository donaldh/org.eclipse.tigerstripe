/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.refactor.diagrams;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * This interface is implemented by the DiagramSynchronizationManager (in
 * ui.base) and allows to give control from the base plugin on the
 * synchronization.
 * 
 * 
 * 
 * @author erdillon
 * 
 */
public class DiagramSynchronizerController {

	private static DiagramSynchronizerController realControler = null;

	public final static void registerController(
			DiagramSynchronizerController realControlerInstance) {
		realControler = realControlerInstance;
	}

	public final static DiagramSynchronizerController getController() {
		if (realControler != null)
			return realControler;

		return new DiagramSynchronizerController();
	}

	/**
	 * Calling this method will cause the diagram synchronization to be held,
	 * i.e. all requests for changes will be enqueued, but none will be applied.
	 * 
	 */
	public void holdSynchronization() {
		if (realControler != null)
			realControler.holdSynchronization();
	}

	/**
	 * This causes to flush out all requests currently enqueued in the current
	 * thread.
	 * 
	 * @param applyRequests
	 *            - whether the request should be applied or simply "dropped"!
	 */
	public void flushSynchronizationRequests(boolean applyRequests,
			IProgressMonitor monitor) {

		if (monitor == null)
			monitor = new NullProgressMonitor();

		if (realControler != null)
			realControler.flushSynchronizationRequests(applyRequests, monitor);
	}

	/**
	 * Restart the normal synchronization process. If any requests are still
	 * enqueued, they will be processed by the regular batched synchronization.
	 */
	public void restartSynchronization() {
		if (realControler != null)
			realControler.restartSynchronization();
	}
}
