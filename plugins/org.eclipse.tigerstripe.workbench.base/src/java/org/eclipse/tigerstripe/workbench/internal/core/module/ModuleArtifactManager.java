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
package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.IDependency;

/**
 * This is an artifact manager relative to a TS module. In other words, it is
 * not related to a TS project but to to a TS module.
 * 
 * All artifacts are inmutable.
 * 
 * @author Eric Dillon
 * 
 */
public class ModuleArtifactManager extends ArtifactManager {

	private ModuleDescriptorModel moduleModel;
	
	// these are used only during generation of modules and then removed. Never
	// persisted.
	private List<IDependency> temporaryDependencies = new ArrayList<IDependency>();

	// TODO remove local variable for embeddedProject this is now redundant
	// with tsProject being saved in the parent.

	public ModuleArtifactManager(ModuleDescriptorModel moduleModel) {
		super( moduleModel.getEmbeddedProject());
		this.moduleModel = moduleModel;
	}

	public TigerstripeProject getEmbeddedProject() {
		return moduleModel.getEmbeddedProject();
	}
	
	public ModuleDescriptorModel getModuleModel() {
		return this.moduleModel;
	}

	@Override
	public synchronized void profileChanged(IWorkbenchProfile newActiveProfile) {
		// IGNORE for modules
	}

	public void addTemporaryDependency(IDependency dependency)
			throws TigerstripeException {
		temporaryDependencies.add(dependency);
	}

	public void clearTemporaryDependencies(IProgressMonitor monitor) {
		temporaryDependencies.clear();
		updateDependenciesContentCache(monitor);
	}

	protected IDependency[] getTemporaryDependencies() {
		return temporaryDependencies
				.toArray(new IDependency[temporaryDependencies.size()]);
	}

	@Override
	public synchronized IDependency[] getProjectDependencies() {
		return getTemporaryDependencies();
	}

}
