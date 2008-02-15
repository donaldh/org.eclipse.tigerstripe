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

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

/**
 * 
 * A Synchronizer capable of updating a diagram for a number of scerarios
 * 
 * @author eric
 * @since Bug 936
 */
public interface IClosedDiagramSynchronizer {

	/**
	 * This method is called right after the constructor to pass the
	 * DiagramHandle for the diagram to act upon
	 * 
	 * @param handle
	 * @throws TigerstripeException
	 *             if the handle is incompatible with the synchronizer
	 */
	public void setDiagramHandle(DiagramHandle handle)
			throws TigerstripeException;

	public void artifactRenamed(String oldFQN, String newFQN)
			throws TigerstripeException;

	public void artifactRemoved(String targetFQN) throws TigerstripeException;

	public void artifactChanged(IAbstractArtifact artifact)
			throws TigerstripeException;

	public String getSupportedDiagramExtension();

	/**
	 * Returns true if the synchronizer has determined it is out of date with
	 * the content of the files on disk.
	 * 
	 * @return
	 */
	public boolean isOutofDate();

	public Diagram getDiagram() throws TigerstripeException;
}
