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
package org.eclipse.tigerstripe.workbench.project;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.project.IProjectChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.model.IModelManager;

/**
 * Handle on a Tigerstripe Project
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface ITigerstripeProject extends IAbstractTigerstripeProject {

	public IModelManager getModelManager() throws TigerstripeException;

	/**
	 * Returns the artifact manager session for this Tigerstripe project.
	 * 
	 * @return
	 */
	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException;

	public RunConfig makeDefaultRunConfig();

	public PluginRunStatus[] generate(RunConfig config, IProgressMonitor monitor)
			throws TigerstripeException;

	// public void generate(ITigerstripeVisitor visitor)
	// throws TigerstripeException;

	public IDependency[] getDependencies() throws TigerstripeException;

	public void removeDependency(IDependency dependency, boolean updateCache,
			IProgressMonitor monitor) throws TigerstripeException;

	public void removeDependencies(IDependency[] dependencies,
			boolean updateCache, IProgressMonitor monitor)
			throws TigerstripeException;

	public void addDependency(IDependency dependency, boolean updateCache,
			IProgressMonitor monitor) throws TigerstripeException;

	public void addDependencies(IDependency[] dependencies,
			boolean updateCache, IProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * This is equivalent to removeDependency(dependency, true)
	 * 
	 * @param dependency
	 * @throws TigerstripeException
	 */
	public void removeDependency(IDependency dependency,
			IProgressMonitor monitor) throws TigerstripeException;

	/**
	 * This is equivalent to removeDependencies(dependencies, true)
	 * 
	 * @param dependencies
	 * @throws TigerstripeException
	 */
	public void removeDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws TigerstripeException;

	/**
	 * This is equivalent to addDependency(dependency, true)
	 * 
	 * @param dependency
	 * @throws TigerstripeException
	 */
	public void addDependency(IDependency dependency,
			IProgressMonitor monitor) throws TigerstripeException;

	/**
	 * This is equivalent to addDependencies( dependencies, true )
	 * 
	 * @param dependencies
	 * @throws TigerstripeException
	 */
	public void addDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws TigerstripeException;

	public IDependency makeDependency(String relativePath)
			throws TigerstripeException;

	public IFacetReference makeFacetReference(URI facetURI)
			throws TigerstripeException;

	public IFacetReference makeFacetReference(String projectRelativePath)
			throws TigerstripeException;

	public IUseCaseReference makeUseCaseReference(String projectRelativePath)
			throws TigerstripeException;

	public void addFacetReference(IFacetReference facetRef)
			throws TigerstripeException;

	public void removeFacetReference(IFacetReference facetRef)
			throws TigerstripeException;

	public IFacetReference[] getFacetReferences() throws TigerstripeException;

	public void addProjectChangeListener(IProjectChangeListener listener);

	public void removeProjectChangeListener(IProjectChangeListener listener);

	/**
	 * Returns true if this project has the given dependency in its dependencies
	 * list
	 * 
	 * @param dep
	 * @return
	 * @throws TigerstripeException
	 *             if the dep is invalid or the project is invalid.
	 */
	public boolean hasDependency(IDependency dep) throws TigerstripeException;

	public void addReferencedProject(ITigerstripeProject project)
			throws TigerstripeException;

	public void addReferencedProjects(ITigerstripeProject[] project)
			throws TigerstripeException;

	public void removeReferencedProject(ITigerstripeProject project)
			throws TigerstripeException;

	public void removeReferencedProjects(ITigerstripeProject[] project)
			throws TigerstripeException;

	public boolean hasReference(ITigerstripeProject project)
			throws TigerstripeException;

	public void setAdvancedProperty(String property, String value)
			throws TigerstripeException;

	/**
	 * Returns the Plugin references defined in this project
	 * 
	 * @return
	 */
	public IPluginConfig[] getPluginConfigs() throws TigerstripeException;

	// ==========================================
	// Project references are direct references to other projects that are
	// required
	// for the local project to build properly.
	public ITigerstripeProject[] getReferencedProjects()
			throws TigerstripeException;

	public void setActiveFacet(IFacetReference facet, IProgressMonitor monitor)
			throws TigerstripeException;

	public void resetActiveFacet() throws TigerstripeException;

	public IFacetReference getActiveFacet() throws TigerstripeException;

	// ==========================================
	// Facility for Advanced Properties access/configuration
	public String getAdvancedProperty(String property)
			throws TigerstripeException;

	public String getAdvancedProperty(String property, String defaultValue)
			throws TigerstripeException;

}
