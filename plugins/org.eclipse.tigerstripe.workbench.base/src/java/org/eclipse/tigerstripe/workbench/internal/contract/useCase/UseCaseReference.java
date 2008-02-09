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
package org.eclipse.tigerstripe.workbench.internal.contract.useCase;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class UseCaseReference implements IUseCaseReference {

	private URI useCaseURI;

	private TigerstripeProject project;

	private ITigerstripeModelProject tsProject;

	private ITigerstripeModelProject contextProject;

	private String projectLabel;

	private String projectRelativePath;

	public UseCaseReference(URI useCaseURI, ITigerstripeModelProject tsProject) {
		this.useCaseURI = useCaseURI;
		this.project = null;
		this.projectRelativePath = null;
		this.tsProject = tsProject;
	}

	public UseCaseReference(String projectRelativePath, String projectLabel,
			ITigerstripeModelProject contextProject) {
		this.projectLabel = projectLabel;
		this.projectRelativePath = projectRelativePath;
		this.contextProject = contextProject;
	}

	public UseCaseReference(String projectRelativePath,
			TigerstripeProject project) {
		this.project = project;
		this.projectRelativePath = projectRelativePath;
	}

	public boolean isAbsolute() {
		return project == null && projectLabel == null;
	}

	public URI getURI() throws TigerstripeException {
		if (isAbsolute())
			return useCaseURI;
		else {
			File baseDir = null;
			if (project == null && projectLabel != null) {
				IProjectLocator locator = (IProjectLocator) InternalTigerstripeCore
						.getFacility(InternalTigerstripeCore.PROJECT_LOCATOR_FACILITY);
				URI uri = locator.locate(contextProject, projectLabel);
				baseDir = new File(uri);
			} else {
				baseDir = project.getBaseDir();
			}

			if (baseDir != null) {
				String path = baseDir.getAbsolutePath() + File.separator
						+ projectRelativePath;
				File file = new File(path);
				return file.toURI();
			}
			throw new TigerstripeException("Can't get URI for facet "
					+ projectRelativePath);
		}
	}

	public IUseCase resolve() throws TigerstripeException {
		if (getURI() != null) {
			File target = new File(getURI());
			if (target.exists() && target.canRead()) {
				IUseCase result = InternalTigerstripeCore.getIContractSession()
						.makeIUseCase(getURI());
				return result;
			} else
				return null;
		}
		throw new TigerstripeException("Invalid UseCase: " + useCaseURI);
	}

	public boolean canResolve() {
		try {
			return resolve() != null;
		} catch (TigerstripeException e) {

		}
		return false;
	}

	public String getProjectRelativePath() {
		return projectRelativePath;
	}

	@Override
	public String toString() {
		try {
			return "Facet Reference(" + canResolve() + "): "
					+ getURI().toASCIIString();
		} catch (TigerstripeException e) {
			return "Facet Reference(" + canResolve() + "): <unknown>";
		}
	}

	public ITigerstripeModelProject getContainingProject() {
		return getTSProject();
	}

	protected ITigerstripeModelProject getTSProject() {

		if (tsProject != null)
			return tsProject;

		try {
			IAbstractTigerstripeProject aProject = null;
			if (project == null && projectLabel != null) {
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IResource res = workspace.getRoot().findMember(projectLabel);
				TigerstripeCore.findProject(res.getFullPath());
			} else {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				File file = project.getBaseDir();
				IPath path = new Path(file.getAbsolutePath());
				IContainer container = root.getContainerForLocation(path);
				aProject = TigerstripeCore.findProject(container.getFullPath());
			}
			if (aProject instanceof ITigerstripeModelProject)
				return (ITigerstripeModelProject) aProject;
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IUseCaseReference) {
			IUseCaseReference other = (IUseCaseReference) obj;
			try {
				return other.getURI().equals(getURI());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
				return false;
			}
		}
		return false;
	}

}
