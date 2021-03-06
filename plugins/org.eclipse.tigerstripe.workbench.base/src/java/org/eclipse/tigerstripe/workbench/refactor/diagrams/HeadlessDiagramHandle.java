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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A handle on a Tigerstripe Diagram, without dealing with GMF.
 * 
 * @author erdillon
 * 
 */
public class HeadlessDiagramHandle {

	private IResource diagramResource;

	private IResource modelResource;

	private ITigerstripeModelProject tsProject;

	public HeadlessDiagramHandle(IResource diagramResource,
			IResource modelResource, ITigerstripeModelProject tsProject) {
		this.diagramResource = diagramResource;
		this.modelResource = modelResource;
		this.tsProject = tsProject;
	}

	public HeadlessDiagramHandle(IResource diagramResource) {
		this.diagramResource = diagramResource;
		IProject iProject = diagramResource.getProject();
		this.tsProject = (ITigerstripeModelProject) iProject
				.getAdapter(ITigerstripeModelProject.class);
		;
	}

	public ITigerstripeModelProject getITigerstripeProject() {
		return this.tsProject;
	}

	public IResource getModelResource() {
		return modelResource;
	}

	public IResource getDiagramResource() {
		return diagramResource;
	}

	public IResource[] getUnderlyingResources() {
		return new IResource[] { modelResource, diagramResource };
	}

	public boolean isValid() {
		return modelResource.exists() && diagramResource.exists();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HeadlessDiagramHandle) {
			HeadlessDiagramHandle other = (HeadlessDiagramHandle) obj;
			if (other.modelResource == null || other.diagramResource == null) {
				return false;
			}
			return other.modelResource.equals(modelResource)
					&& other.diagramResource.equals(diagramResource);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		if (modelResource != null) {
			hashCode += modelResource.hashCode();
		}
		if (diagramResource != null) {
			hashCode += 31 * diagramResource.hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		return getModelResource().getFullPath().removeFileExtension()
				.toPortableString();
	}
}
