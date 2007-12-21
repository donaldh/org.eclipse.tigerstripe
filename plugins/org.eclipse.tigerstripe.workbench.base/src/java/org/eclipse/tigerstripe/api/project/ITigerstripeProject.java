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
package org.eclipse.tigerstripe.api.project;

import java.net.URI;

import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.project.IextTigerstripeProject;
import org.eclipse.tigerstripe.api.modules.IModulePackager;
import org.eclipse.tigerstripe.api.publish.IProjectCSVCreator;
import org.eclipse.tigerstripe.api.publish.IProjectPublisher;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;

/**
 * Handle on a Tigerstripe Project
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface ITigerstripeProject extends IAbstractTigerstripeProject,
		IextTigerstripeProject {

	/**
	 * Returns the artifact manager session for this Tigerstripe project.
	 * 
	 * @return
	 */
	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException;

	public IProjectDetails getProjectDetails() throws TigerstripeException;

	public void generate(ITigerstripeVisitor visitor)
			throws TigerstripeException;

	public IModulePackager getPackager();

	public IProjectPublisher getPublisher();

	public IProjectCSVCreator getCSVCreator();

	public IDependency[] getDependencies() throws TigerstripeException;

	public void removeDependency(IDependency dependency, boolean updateCache,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	public void removeDependencies(IDependency[] dependencies,
			boolean updateCache, ITigerstripeProgressMonitor monitor)
			throws TigerstripeException;

	public void addDependency(IDependency dependency, boolean updateCache,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	public void addDependencies(IDependency[] dependencies,
			boolean updateCache, ITigerstripeProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * This is equivalent to removeDependency(dependency, true)
	 * 
	 * @param dependency
	 * @throws TigerstripeException
	 */
	public void removeDependency(IDependency dependency,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	/**
	 * This is equivalent to removeDependencies(dependencies, true)
	 * 
	 * @param dependencies
	 * @throws TigerstripeException
	 */
	public void removeDependencies(IDependency[] dependencies,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	/**
	 * This is equivalent to addDependency(dependency, true)
	 * 
	 * @param dependency
	 * @throws TigerstripeException
	 */
	public void addDependency(IDependency dependency,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	/**
	 * This is equivalent to addDependencies( dependencies, true )
	 * 
	 * @param dependencies
	 * @throws TigerstripeException
	 */
	public void addDependencies(IDependency[] dependencies,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	public IDependency makeIDependency(String relativePath)
			throws TigerstripeException;

	public IFacetReference makeIFacetReference(URI facetURI)
			throws TigerstripeException;

	public IFacetReference makeIFacetReference(String projectRelativePath)
			throws TigerstripeException;

	public IUseCaseReference makeIUseCaseReference(String projectRelativePath)
			throws TigerstripeException;

	public void addFacetReference(IFacetReference facetRef)
			throws TigerstripeException;

	public void removeFacetReference(IFacetReference facetRef)
			throws TigerstripeException;

	public IFacetReference[] getFacetReferences() throws TigerstripeException;

	// Core dependencies don't exist anymore @see #299
	// /**
	// *
	// * @see org.eclipse.tigerstripe.core.project.TigerstripeProject
	// * @throws NoCoreModelException
	// * @throws MismatchedCoreModelException
	// * @since 1.0.3
	// */
	// public void checkDefaultCoreModelDependency() throws
	// TigerstripeException,
	// NoCoreModelException, MismatchedCoreModelException;
	//
	// /**
	// * Adds the default code model as a dependency
	// *
	// * @param forceOverwrite
	// * @throws TigerstripeException
	// * @since 1.0.3
	// */
	// public void attachDefaultCoreModelDependency(boolean forceOverwrite)
	// throws TigerstripeException;

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
	 * Whether the descriptor needs to be upgraded to the correct compatibility
	 * level (and default values set properly).
	 * 
	 * @return
	 */
	public boolean requiresDescriptorUpgrade() throws TigerstripeException;

	/**
	 * Returns the IImportCheckpoint for this project if it exists.
	 * 
	 * If no import was performed in this project, no IImportCheckpoint would
	 * exist and an exception would be thrown.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IImportCheckpoint getImportCheckpoint() throws TigerstripeException;

	/**
	 * Returns the Plugin references defined in this project
	 * 
	 * @return
	 */
	public IPluginReference[] getPluginReferences() throws TigerstripeException;

	// ==========================================
	// Project references are direct references to other projects that are
	// required
	// for the local project to build properly.
	public ITigerstripeProject[] getReferencedProjects()
			throws TigerstripeException;

	/**
	 * Provides a convenient default unique name provider for all artifacts
	 * being created.
	 * 
	 */
	public INameProvider getNameProvider();

	public String getBaseRepository() throws TigerstripeException;

	public void setActiveFacet(IFacetReference facet,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	public void resetActiveFacet() throws TigerstripeException;

	public IFacetReference getActiveFacet() throws TigerstripeException;
}
