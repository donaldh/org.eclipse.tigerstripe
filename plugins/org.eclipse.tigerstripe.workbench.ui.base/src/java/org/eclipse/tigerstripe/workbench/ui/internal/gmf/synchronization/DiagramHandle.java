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
package org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization;

import org.eclipse.core.resources.IResource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.HeadlessDiagramHandle;

/**
 * A Diagram handle is a shell object to manipulate GMF diagrams without having
 * to explicitly know their actual type
 * 
 * @author eric
 * @since Bug 936
 */
public class DiagramHandle extends HeadlessDiagramHandle {

	private IClosedDiagramSynchronizer synchronizer;

	public DiagramHandle(IResource diagramResource, IResource modelResource,
			ITigerstripeModelProject tsProject) {
		super(diagramResource, modelResource, tsProject);
	}

	public DiagramHandle(IResource diagramResource) {
		super(diagramResource);
	}

	protected IClosedDiagramSynchronizer getSynchronizer()
			throws TigerstripeException {
		if (synchronizer == null || synchronizer.isOutofDate()) {
			synchronizer = DiagramSynchronizerFactory.make(this);
		}
		return synchronizer;
	}

	public Diagram getDiagram() throws TigerstripeException {
		IClosedDiagramSynchronizer synchronizer = getSynchronizer();
		return synchronizer.getDiagram();
	}

	public void updateForRename(String oldFQN, String newFQN)
			throws TigerstripeException {
		try {
			getSynchronizer().artifactRenamed(oldFQN, newFQN);
		} catch (RemovedDiagramException e) {
			// ignore this one silently. The handle has been removed, and this
			// won't be called anymore
		}
	}

	public void updateForRemove(String targetFQN) throws TigerstripeException {
		try {
			getSynchronizer().artifactRemoved(targetFQN);
		} catch (RemovedDiagramException e) {
			// ignore this one silently. The handle has been removed, and this
			// won't be called anymore
		}
	}

	public void updateForChange(IAbstractArtifact artifact)
			throws TigerstripeException {
		try {
			getSynchronizer().artifactChanged(artifact);
		} catch (RemovedDiagramException e) {
			// ignore this one silently. The handle has been removed, and this
			// won't be called anymore
		}
	}

	@Override
	public int hashCode() {
		return getDiagramResource().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof DiagramHandle) {
			DiagramHandle otherDiag = (DiagramHandle) other;
			return getDiagramResource().equals(otherDiag.getDiagramResource());
		}
		return false;
	}

}
