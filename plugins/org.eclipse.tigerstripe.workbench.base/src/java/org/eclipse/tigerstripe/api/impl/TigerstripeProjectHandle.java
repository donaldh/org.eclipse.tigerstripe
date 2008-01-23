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

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.project.IDependency;
import org.eclipse.tigerstripe.api.project.IImportCheckpoint;
import org.eclipse.tigerstripe.api.project.INameProvider;
import org.eclipse.tigerstripe.api.project.IProjectChangeListener;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.contract.segment.FacetReference;
import org.eclipse.tigerstripe.contract.useCase.UseCaseReference;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.cli.App;
import org.eclipse.tigerstripe.core.generation.PluginRunResult;
import org.eclipse.tigerstripe.core.generation.ProjectGenerator;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.importing.AbstractImportCheckpointHelper;
import org.eclipse.tigerstripe.core.project.Dependency;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public abstract class TigerstripeProjectHandle extends
		AbstractTigerstripeProjectHandle implements ITigerstripeProject {

	private INameProvider nameProvider;

	public final static String DESCRIPTOR_FILENAME = ITigerstripeConstants.PROJECT_DESCRIPTOR;

	public String getDescriptorFilename() {
		return DESCRIPTOR_FILENAME;
	}

	@Override
	public String getProjectLabel() {
		try {
			return getTSProject().getProjectLabel();
		} catch (TigerstripeException e) {
			// Ignore for now
		}
		return "unknown";
	}

	/** logger for output */
	private static Logger log = Logger.getLogger(App.class);

	protected ArtifactManagerSessionImpl artifactMgrSession;

	private TigerstripeProject tsProject;

	public TigerstripeProjectHandle(URI projectContainerURI) {
		super(projectContainerURI);
	}

	public INameProvider getNameProvider() {
		if (nameProvider == null) {
			nameProvider = new NameProviderImpl(this);
		}

		return nameProvider;
	}

	public String getBaseRepository() throws TigerstripeException {
		if (getTSProject() != null)
			return getTSProject().getBaseRepository();
		throw new TigerstripeException("Invalid project handle.");
	}

	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException {
		if (artifactMgrSession == null) {
			ArtifactManager manager = new ArtifactManager(getTSProject());
			setArtifactManagerSession(new ArtifactManagerSessionImpl(manager));
		}

		return artifactMgrSession;
	}

	protected void setArtifactManagerSession(ArtifactManagerSessionImpl session) {
		artifactMgrSession = session;
	}

	protected void setTSProject(TigerstripeProject tsProject) {
		this.tsProject = tsProject;
	}

	public TigerstripeProject getTSProject() throws TigerstripeException {
		if (tsProject == null) {
			// try and create a project for the URI
			log.debug("URI =" + this.projectContainerURI.toString());
			File baseDir = new File(this.projectContainerURI);
			log.debug("baseDir " + baseDir.toString());
			if (baseDir.isDirectory()) {
				tsProject = new TigerstripeProject(baseDir);
			} else
				throw new TigerstripeException("Invalid project "
						+ baseDir.toString());
		}

		tsProject.reload(false);
		return this.tsProject;
	}

	@Override
	public boolean exists() {
		boolean result = false;
		// check that a descriptor can be found and that it is valid
		if (findProjectDescriptor()) {
			try {
				getTSProject();
				result = true;
			} catch (TigerstripeException e) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Tries and locate the project descriptor for this project
	 * 
	 * @return
	 */
	@Override
	protected boolean findProjectDescriptor() {
		projectContainer = new File(projectContainerURI);
		FilenameFilter filter = new FilenameFilter() {

			public boolean accept(File arg0, String arg1) {
				return "tigerstripe.xml".equals(arg1);
			}

		};

		String[] desc = projectContainer.list(filter);

		return desc != null && desc.length == 1;
	}

	@Override
	public File getProjectDescriptorFile() {
		return projectContainer;
	}

	public IPluginReference[] getPluginReferences() throws TigerstripeException {

		TigerstripeProject project = getTSProject();
		Collection ref = project.getPluginReferences();

		IPluginReference[] result = new IPluginReference[ref.size()];
		result = (IPluginReference[]) ref.toArray(new IPluginReference[ref
				.size()]);

		// Since 2.1 added reference to ProjectHandle
		for (IPluginReference pRef : result) {
			pRef.setProjectHandle(this);
		}

		return result;
	}

	public void addPluginReference(IPluginReference ref)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		Collection refs = project.getPluginReferences();
		refs.add(ref);
	}

	public void removePluginReference(IPluginReference ref)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		Collection refs = project.getPluginReferences();
		refs.remove(ref);
	}

	public IProjectDetails getProjectDetails() throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		return project.getProjectDetails();
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		getTSProject().reload(true);
		getTSProject().validate(visitor);
	}

	public void generate(ITigerstripeVisitor visitor)
			throws TigerstripeException {

		ProjectGenerator generator = new ProjectGenerator(this);
		PluginRunResult[] result = generator.run();

	}

	public void publish(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		log.info("Loading artifacts");
		getTSProject().reload(true);

		log.info("Publishing...");
		IPluginReference[] refs = getPluginReferences();
		for (int i = 0; i < refs.length; i++) {
			log.info(" ..." + refs[i].getGroupId() + "/"
					+ refs[i].getPluginId() + "(" + refs[i].getVersion() + ")");
			if (refs[i].getCategory() == IPluginReference.PUBLISH_CATEGORY)
				refs[i].trigger();
		}
	}

	public IDependency makeIDependency(String relativePath)
			throws TigerstripeException {
		return new Dependency(getTSProject(), relativePath);
	}

	public IDependency[] getDependencies() throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		Collection deps = project.getDependencies();

		IDependency[] result = new IDependency[deps.size()];
		return (IDependency[]) deps.toArray(result);
	}

	public void addDependency(IDependency dependency,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		addDependency(dependency, true, monitor);
	}

	public void addDependency(IDependency dependency, boolean updateCache,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.addDependency(dependency);

		if (updateCache)
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
	}

	public void addDependencies(IDependency[] dependencies,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		addDependencies(dependencies, true, monitor);
	}

	public void addDependencies(IDependency[] dependencies,
			boolean updateCache, ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.addDependencies(dependencies);
		if (updateCache)
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
	}

	public void removeDependency(IDependency dependency,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		removeDependency(dependency, true, monitor);
	}

	public void removeDependency(IDependency dependency, boolean updateCache,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.removeDependency(dependency);

		if (updateCache)
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
	}

	public void removeDependencies(IDependency[] dependencies,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		removeDependencies(dependencies, true, monitor);
	}

	public void removeDependencies(IDependency[] dependencies,
			boolean updateCache, ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.removeDependencies(dependencies);

		if (updateCache)
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
	}

	public void addProjectChangeListener(IProjectChangeListener listener) {
		try {
			getTSProject().addProjectChangeListener(listener);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	public void removeProjectChangeListener(IProjectChangeListener listener) {
		try {
			getTSProject().removeProjectChangeListener(listener);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	public boolean hasDependency(IDependency dep) throws TigerstripeException {
		if (dep == null || !dep.isValid())
			throw new TigerstripeException("Invalid dependency");
		TigerstripeProject project = getTSProject();
		return project.getDependencies().contains(dep);
	}

	// Shipped core dependencies don't exist anymore @see #299
	// public void checkDefaultCoreModelDependency() throws
	// TigerstripeException,
	// NoCoreModelException, MismatchedCoreModelException {
	// TigerstripeProject project = getTSProject();
	// project.checkDefaultCoreModelDependency();
	// }
	//
	// public void attachDefaultCoreModelDependency(boolean forceOverwrite)
	// throws TigerstripeException {
	// TigerstripeProject project = getTSProject();
	// project.attachDefaultCoreModelDependency(forceOverwrite);
	// }
	//
	public void addReferencedProject(ITigerstripeProject project)
			throws TigerstripeException {
		getTSProject().addReferencedProject(project);
	}

	public void addReferencedProjects(ITigerstripeProject[] projects)
			throws TigerstripeException {
		getTSProject().addReferencedProjects(projects);
	}

	public ITigerstripeProject[] getReferencedProjects()
			throws TigerstripeException {
		return getTSProject().getReferencedProjects();
	}

	public void removeReferencedProject(ITigerstripeProject project)
			throws TigerstripeException {
		getTSProject().removeReferencedProject(project);
	}

	public void removeReferencedProjects(ITigerstripeProject[] projects)
			throws TigerstripeException {
		getTSProject().removeReferencedProjects(projects);
	}

	public boolean hasReference(ITigerstripeProject project)
			throws TigerstripeException {
		return getTSProject().hasReference(project);
	}

	public String getAdvancedProperty(String property)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		return project.getAdvancedProperty(property);
	}

	public String getAdvancedProperty(String property, String defaultValue)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		return project.getAdvancedProperty(property, defaultValue);
	}

	public void setAdvancedProperty(String property, String value)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.setAdvancedProperty(property, value);
	}

	public boolean requiresDescriptorUpgrade() throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		return project.requiresDescriptorUpgrade();
	}

	/**
	 * Returns the IImportCheckpoint for this project
	 * 
	 */
	public IImportCheckpoint getImportCheckpoint() throws TigerstripeException {

		AbstractImportCheckpointHelper cpHelper = new AbstractImportCheckpointHelper(
				this);
		IImportCheckpoint result = cpHelper.readCheckpoint();
		return result;
	}

	public IArtifactManagerSession getIArtifactManagerSession()
			throws TigerstripeException {
		return getArtifactManagerSession();
	}

	

	public void doSave() throws TigerstripeException {
		try {
			TigerstripeProject project = getTSProject();
			if (project != null) {
				project.doSave();
				return;
			}
		} catch (TigerstripeException e) {
			if (tsProject != null) {
				// This means we are saving on an empty directory, and the
				// descriptor needs to be created now
				tsProject.doSave();
				return;
			}
		}

		throw new TigerstripeException("Invalid project, cannot save.");
	}

	public IFacetReference makeIFacetReference(URI facetURI)
			throws TigerstripeException {
		FacetReference ref = new FacetReference(facetURI, this);
		return ref;
	}

	public IFacetReference makeIFacetReference(String projectRelativePath)
			throws TigerstripeException {
		FacetReference ref = new FacetReference(projectRelativePath,
				getTSProject());
		return ref;
	}

	public IUseCaseReference makeIUseCaseReference(URI useCaseURI)
			throws TigerstripeException {
		UseCaseReference ref = new UseCaseReference(useCaseURI, this);
		return ref;
	}

	public IUseCaseReference makeIUseCaseReference(String projectRelativePath)
			throws TigerstripeException {
		UseCaseReference ref = new UseCaseReference(projectRelativePath,
				getTSProject());
		return ref;
	}

	public void addFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		if (project != null) {
			project.addFacetReference(facetRef);
			return;
		}
		throw new TigerstripeException(
				"Invalid project, cannot add facet reference.");
	}

	public void removeFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		if (project != null) {
			project.removeFacetReference(facetRef);
			return;
		}
		throw new TigerstripeException(
				"Invalid project, cannot remove facet reference.");
	}

	public IFacetReference[] getFacetReferences() throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		if (project != null) {
			List<IFacetReference> refs = project.getFacetReferences();
			return refs.toArray(new IFacetReference[refs.size()]);
		}
		throw new TigerstripeException(
				"Invalid project, cannot remove facet reference.");
	}

	public IFacetReference getActiveFacet() throws TigerstripeException {
		return getArtifactManagerSession().getActiveFacet();
	}

	public void resetActiveFacet() throws TigerstripeException {
		getArtifactManagerSession().resetActiveFacet();
	}

	public void setActiveFacet(IFacetReference facet,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		getArtifactManagerSession().setActiveFacet(facet, monitor);
	}

	public IPluginReference[] getIPluginReferences()
	throws TigerstripeException {
		return getPluginReferences();
	}

	public ITigerstripeProject[] getIReferencedProjects()
	throws TigerstripeException {
		return getReferencedProjects();
	}

	public IProjectDetails getIProjectDetails()
	throws TigerstripeException {
		return getProjectDetails();
	}

}
