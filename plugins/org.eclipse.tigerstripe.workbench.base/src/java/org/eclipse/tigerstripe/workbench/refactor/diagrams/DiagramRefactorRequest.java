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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.refactor.RefactorRequest;

public class DiagramRefactorRequest extends RefactorRequest {

	private HeadlessDiagramHandle originalDiagramHandle = null;
	private IResource destinationContainer = null;

	/**
	 * The new container for the diagram.
	 * 
	 * @param container
	 */
	public void setDestination(IResource container) {
		this.destinationContainer = container;
	}

	public IResource getDestination() {
		return this.destinationContainer;
	}

	public void setOriginalDiagramHandle(HeadlessDiagramHandle diagramHandle) {
		this.originalDiagramHandle = diagramHandle;
	}

	public HeadlessDiagramHandle getOriginalDiagramHandle() {
		return this.originalDiagramHandle;
	}

	@Override
	public IStatus isValid() {

		if (!originalDiagramHandle.isValid()) {
			Status status = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
					"Invalid diagram");
			return status;
		}

		if (destinationContainer.exists()
				&& destinationContainer instanceof IContainer) {
			IContainer container = (IContainer) destinationContainer;
			if (container.exists(new Path(originalDiagramHandle
					.getModelResource().getName()))
					|| container.exists(new Path(originalDiagramHandle
							.getDiagramResource().getName()))) {
				Status status = new Status(IStatus.ERROR, BasePlugin
						.getPluginId(),
						"Destination already contains matching files.");
				return status;
			}
		}

		return Status.OK_STATUS;
	}

	@Override
	public String toString() {
		return "DiagramRefactorRequest: "
				+ originalDiagramHandle.getDiagramResource() + " to "
				+ destinationContainer;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DiagramRefactorRequest) {
			DiagramRefactorRequest other = (DiagramRefactorRequest) obj;
			return other.destinationContainer.equals(destinationContainer)
					&& other.originalDiagramHandle
							.equals(originalDiagramHandle);
		}
		return false;
	}
}
