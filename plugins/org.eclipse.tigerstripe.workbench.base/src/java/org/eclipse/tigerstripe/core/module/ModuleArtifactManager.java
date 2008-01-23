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
package org.eclipse.tigerstripe.core.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.api.project.IDependency;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

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

	// these are used only during generation of modules and then removed. Never
	// persisted.
	private List<IDependency> temporaryDependencies = new ArrayList<IDependency>();

	// TODO remove local variable for embeddedProject this is now redundant
	// with tsProject being saved in the parent.

	public ModuleArtifactManager(TigerstripeProject embeddedProject) {
		super(embeddedProject);
		this.embeddedProject = embeddedProject;
	}

	private TigerstripeProject embeddedProject;

	public TigerstripeProject getEmbeddedProject() {
		return this.embeddedProject;
	}

	public void setEmbeddedProject(TigerstripeProject project) {
		this.embeddedProject = project;
	}

	@Override
	public TigerstripeProject getTSProject() {
		return getEmbeddedProject();
	}

	@Override
	public synchronized void profileChanged(IWorkbenchProfile newActiveProfile) {
		// IGNORE for modules
	}

	public void addTemporaryDependency(IDependency dependency)
			throws TigerstripeException {
		temporaryDependencies.add(dependency);
	}

	public void clearTemporaryDependencies(ITigerstripeProgressMonitor monitor) {
		temporaryDependencies.clear();
		updateDependenciesContentCache(monitor);
	}

	protected Collection getTemporaryDependencies() {
		return temporaryDependencies;
	}

	@Override
	public synchronized Collection getProjectDependencies() {
		return getTemporaryDependencies();
	}

}
