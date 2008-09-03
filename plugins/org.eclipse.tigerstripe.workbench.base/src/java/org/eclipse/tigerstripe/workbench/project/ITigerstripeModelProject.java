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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ProjectModelManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;

/**
 * Handle on a Tigerstripe Project
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface ITigerstripeModelProject extends IAbstractTigerstripeProject {

	public ProjectModelManager getModelManager() throws TigerstripeException;

	/**
	 * Returns the artifact manager session for this Tigerstripe project.
	 * 
	 * @return
	 */
	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException;

	public PluginRunStatus[] generate(IM1RunConfig config,
			IProgressMonitor monitor) throws TigerstripeException;

	public IDependency[] getDependencies() throws TigerstripeException;

	/**
	 * 
	 * @param dependency
	 * @throws TigerstripeException
	 */
	public void removeDependency(IDependency dependency,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException;

	/**
	 * 
	 * @param dependencies
	 * @throws TigerstripeException
	 */
	public void removeDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException;

	/**
	 * 
	 * @param dependency
	 * @throws TigerstripeException
	 */
	public void addDependency(IDependency dependency, IProgressMonitor monitor)
			throws WorkingCopyException, TigerstripeException;

	/**
	 * 
	 * @param dependencies
	 * @throws TigerstripeException
	 */
	public void addDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException;

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

	public void addReferencedProject(ITigerstripeModelProject project)
			throws WorkingCopyException, TigerstripeException;

	public void addReferencedProjects(ITigerstripeModelProject[] project)
			throws WorkingCopyException, TigerstripeException;

	public void removeReferencedProject(ITigerstripeModelProject project)
			throws WorkingCopyException, TigerstripeException;

	public void removeReferencedProjects(ITigerstripeModelProject[] project)
			throws WorkingCopyException, TigerstripeException;

	public boolean hasReference(ITigerstripeModelProject project)
			throws WorkingCopyException, TigerstripeException;

	public void setAdvancedProperty(String property, String value)
			throws WorkingCopyException, TigerstripeException;

	/**
	 * Returns the Plugin references defined in this project
	 * 
	 * @return
	 */
	public IPluginConfig[] getPluginConfigs() throws TigerstripeException;

	public void addPluginConfig(IPluginConfig config)
			throws TigerstripeException;

	public void removePluginConfig(IPluginConfig config)
			throws TigerstripeException;

	// ==========================================
	// Project references are direct references to other projects that are
	// required
	// for the local project to build properly.
	public ITigerstripeModelProject[] getReferencedProjects()
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
