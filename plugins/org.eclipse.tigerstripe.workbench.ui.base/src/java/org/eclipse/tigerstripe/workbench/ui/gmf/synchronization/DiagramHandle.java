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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * A Diagram handle is a shell object to manipulate GMF diagrams without having
 * to explicitly know their actual type
 * 
 * @author eric
 * @since Bug 936
 */
public class DiagramHandle {

	private IResource diagramResource;

	private IResource modelResource;

	private IClosedDiagramSynchronizer synchronizer;

	private ITigerstripeProject tsProject;

	public DiagramHandle(IResource diagramResource, IResource modelResource,
			ITigerstripeProject tsProject) {
		this.diagramResource = diagramResource;
		this.modelResource = modelResource;
		this.tsProject = tsProject;
	}

	public DiagramHandle(IResource diagramResource) {
		this.diagramResource = diagramResource;
		IProject iProject = diagramResource.getProject();
		this.tsProject = (ITigerstripeProject) EclipsePlugin
				.getITigerstripeProjectFor(iProject);
	}

	public ITigerstripeProject getITigerstripeProject() {
		return this.tsProject;
	}

	public IResource getModelResource() {
		return modelResource;
	}

	public IResource getDiagramResource() {
		return diagramResource;
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
}
