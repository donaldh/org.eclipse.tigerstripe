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

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.ModelManager;
import org.eclipse.tigerstripe.workbench.internal.api.project.IImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.api.project.INameProvider;
import org.eclipse.tigerstripe.workbench.internal.api.project.IProjectChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.useCase.UseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.cli.App;
import org.eclipse.tigerstripe.workbench.internal.core.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.ProjectGenerator;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AbstractImportCheckpointHelper;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.IModelManager;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public abstract class TigerstripeProjectHandle extends
		AbstractTigerstripeProjectHandle implements ITigerstripeProject {

	// Fields under control of WorkingCopyDelegate
	private final static int PROJECT_DETAILS = 0;

	private INameProvider nameProvider;

	private ModelManager modelManager;

	private ArtifactManager manager;

	public final static String DESCRIPTOR_FILENAME = ITigerstripeConstants.PROJECT_DESCRIPTOR;

	public String getDescriptorFilename() {
		return DESCRIPTOR_FILENAME;
	}

	/** logger for output */
	private static Logger log = Logger.getLogger(App.class);

	protected ArtifactManagerSessionImpl artifactMgrSession;

	private TigerstripeProject tsProject;

	public TigerstripeProjectHandle(URI projectContainerURI) {
		super(projectContainerURI);
	}

	@Override
	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException {
		assertSet(PROJECT_DETAILS);
		getTSProject().setProjectDetails((ProjectDetails) projectDetails);
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

	@Override
	public IModelManager getModelManager() throws TigerstripeException {
		if (modelManager == null) {
			if (manager == null)
				manager = new ArtifactManager(getTSProject());
			modelManager = new ModelManager(manager);
		}

		// TODO Auto-generated method stub
		return null;
	}

	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException {
		if (artifactMgrSession == null) {
			if (manager == null) {
				manager = new ArtifactManager(getTSProject());
			}
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

	public IPluginConfig[] getPluginConfigs() throws TigerstripeException {

		TigerstripeProject project = getTSProject();
		Collection ref = project.getPluginConfigs();

		IPluginConfig[] result = new IPluginConfig[ref.size()];
		result = (IPluginConfig[]) ref.toArray(new IPluginConfig[ref.size()]);

		// Since 2.1 added reference to ProjectHandle
		for (IPluginConfig pRef : result) {
			pRef.setProjectHandle(this);
		}

		return result;
	}

	public void addPluginConfig(IPluginConfig ref) throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		Collection refs = project.getPluginConfigs();
		refs.add(ref);
	}

	public void removePluginConfig(IPluginConfig ref)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		Collection refs = project.getPluginConfigs();
		refs.remove(ref);
	}

	public IProjectDetails getProjectDetails() throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		try {
			return (IProjectDetails) project.getProjectDetails().clone();
		} catch (CloneNotSupportedException e) {
			// ignore
			return null;
		}
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
		PluginRunStatus[] result = generator.run();

	}

	public void publish(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		log.info("Loading artifacts");
		getTSProject().reload(true);

		log.info("Publishing...");
		IPluginConfig[] refs = getPluginConfigs();
		for (int i = 0; i < refs.length; i++) {
			log.info(" ..." + refs[i].getGroupId() + "/"
					+ refs[i].getPluginId() + "(" + refs[i].getVersion() + ")");
			if (refs[i].getCategory() == IPluginConfig.PUBLISH_CATEGORY)
				refs[i].trigger();
		}
	}

	public IDependency makeDependency(String relativePath)
			throws TigerstripeException {
		return new Dependency(getTSProject(), relativePath);
	}

	public IDependency[] getDependencies() throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		Collection deps = project.getDependencies();

		IDependency[] result = new IDependency[deps.size()];
		return (IDependency[]) deps.toArray(result);
	}

	public void addDependency(IDependency dependency, IProgressMonitor monitor)
			throws TigerstripeException {
		addDependency(dependency, true, monitor);
	}

	public void addDependency(IDependency dependency, boolean updateCache,
			IProgressMonitor monitor) throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.addDependency(dependency);

		if (updateCache)
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
	}

	public void addDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws TigerstripeException {
		addDependencies(dependencies, true, monitor);
	}

	public void addDependencies(IDependency[] dependencies,
			boolean updateCache, IProgressMonitor monitor)
			throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.addDependencies(dependencies);
		if (updateCache)
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
	}

	public void removeDependency(IDependency dependency,
			IProgressMonitor monitor) throws TigerstripeException {
		removeDependency(dependency, true, monitor);
	}

	public void removeDependency(IDependency dependency, boolean updateCache,
			IProgressMonitor monitor) throws TigerstripeException {
		TigerstripeProject project = getTSProject();
		project.removeDependency(dependency);

		if (updateCache)
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
	}

	public void removeDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws TigerstripeException {
		removeDependencies(dependencies, true, monitor);
	}

	public void removeDependencies(IDependency[] dependencies,
			boolean updateCache, IProgressMonitor monitor)
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

	public IFacetReference makeFacetReference(URI facetURI)
			throws TigerstripeException {
		FacetReference ref = new FacetReference(facetURI, this);
		return ref;
	}

	public IFacetReference makeFacetReference(String projectRelativePath)
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

	public IUseCaseReference makeUseCaseReference(String projectRelativePath)
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

	public void setActiveFacet(IFacetReference facet, IProgressMonitor monitor)
			throws TigerstripeException {
		getArtifactManagerSession().setActiveFacet(facet, monitor);
	}

	public ITigerstripeProject[] getIReferencedProjects()
			throws TigerstripeException {
		return getReferencedProjects();
	}

	// ==============================================================================
	// WorkingCopy stuff
	@Override
	public void doCommit(IProgressMonitor monitor) throws TigerstripeException {
		doSave();
	}

	@Override
	public PluginRunStatus[] generate(RunConfig config, IProgressMonitor monitor)
			throws TigerstripeException {
		ProjectGenerator generator = new ProjectGenerator(this, config);
		return generator.run();
	}

	@Override
	public RunConfig makeDefaultRunConfig() {
		return new RunConfig(this);
	}

}
