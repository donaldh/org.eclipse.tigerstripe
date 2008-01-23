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
package org.eclipse.tigerstripe.api.impl;

import java.net.URI;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.modules.IModulePackager;
import org.eclipse.tigerstripe.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.api.project.IDependency;
import org.eclipse.tigerstripe.api.publish.IProjectCSVCreator;
import org.eclipse.tigerstripe.api.publish.IProjectPublisher;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.core.module.ModuleRef;
import org.eclipse.tigerstripe.core.project.Dependency;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

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

	public IProjectCSVCreator getCSVCreator() {
		return null;
	}

	public IModulePackager getPackager() {
		return null;
	}

	public IProjectPublisher getPublisher() {
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

	public void updateDependenciesContentCache(
			ITigerstripeProgressMonitor monitor) {
		getModuleManager().updateDependenciesContentCache(monitor);
	}

	public void addTemporaryDependency(IDependency dependency)
			throws TigerstripeException {
		getModuleManager().addTemporaryDependency(dependency);
	}

	public void clearTemporaryDependencies(ITigerstripeProgressMonitor monitor) {
		getModuleManager().clearTemporaryDependencies(monitor);
	}
}
