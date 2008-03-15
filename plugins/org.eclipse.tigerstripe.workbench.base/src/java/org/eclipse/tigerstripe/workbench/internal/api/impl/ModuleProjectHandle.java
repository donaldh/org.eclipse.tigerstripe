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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleRef;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IDependency;

public class ModuleProjectHandle extends TigerstripeProjectHandle implements
		ITigerstripeModuleProject {

	private ModuleRef moduleRef;

	/**
	 * 
	 * @param projectContainerURI,
	 *            The URI of the containing project (where the module is)
	 * @param dependency,
	 *            the dependency to create the module project for.
	 */
	public ModuleProjectHandle(URI projectContainerURI, Dependency dependency) {
		super(projectContainerURI);
		if (dependency.isValid()) {
			this.moduleRef = dependency.getModuleRef();
		}
	}

	@Override
	public TigerstripeProject getTSProject() throws TigerstripeException {
		return moduleRef.getEmbeddedProject();
	}

	public IModulePackager getPackager() {
		return null;
	}

	@Override
	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException {
		if (artifactMgrSession == null) {
			setArtifactManagerSession(new ArtifactManagerSessionImpl(moduleRef
					.getArtifactManager()));
		}

		return artifactMgrSession;
	}

	protected ModuleArtifactManager getModuleManager() {
		return (ModuleArtifactManager) moduleRef.getArtifactManager();
	}

	public void updateDependenciesContentCache(IProgressMonitor monitor) {
		getModuleManager().updateDependenciesContentCache(monitor);
	}

	public void addTemporaryDependency(IDependency dependency)
			throws TigerstripeException {
		getModuleManager().addTemporaryDependency(dependency);
	}

	public void clearTemporaryDependencies(IProgressMonitor monitor) {
		getModuleManager().clearTemporaryDependencies(monitor);
	}

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		throw new TigerstripeException("Operation not supported.");
	}

	@Override
	public boolean isDirty() {
		return false;
	}
}
