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
import java.io.StringReader;
import java.net.URI;
import java.util.Arrays;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.project.IImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.api.project.INameProvider;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.useCase.UseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1Generator;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManagerImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AbstractImportCheckpointHelper;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ProjectModelManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyChangeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class TigerstripeProjectHandle extends
		AbstractTigerstripeProjectHandle implements ITigerstripeModelProject {

	private ListenerList projectChangeListeners = new ListenerList();

	private INameProvider nameProvider;

	private ArtifactManager manager;

	private boolean dependenciesCacheNeedsRefresh = false;

	public final static String DESCRIPTOR_FILENAME = ITigerstripeConstants.PROJECT_DESCRIPTOR;

	private boolean wasDisposed = false;

	public String getDescriptorFilename() {
		return DESCRIPTOR_FILENAME;
	}

	protected ArtifactManagerSessionImpl artifactMgrSession;

	private TigerstripeProject tsProject;

	public TigerstripeProjectHandle(URI projectContainerURI) {
		super(projectContainerURI);
	}

	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
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

	public ProjectModelManager getModelManager() throws TigerstripeException {
		ProjectModelManager manager = new ProjectModelManager(this);
		return manager;
	}

	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException {
		if (artifactMgrSession == null) {
			if (manager == null) {
				manager = new ArtifactManagerImpl(getTSProject());
			}
			setArtifactManagerSession(new ArtifactManagerSessionImpl(manager));

		}

		return artifactMgrSession;
	}

	public boolean wasDisposed() {
		return super.wasDisposed() && wasDisposed;
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
			File baseDir = new File(this.projectContainerURI);
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
		return getTSProject().getPluginConfigs();
	}

	public void addPluginConfig(IPluginConfig ref) throws TigerstripeException {
		assertSet();
		getTSProject().addPluginConfig(ref);
	}

	public void removePluginConfig(IPluginConfig ref)
			throws TigerstripeException {
		assertSet();
		getTSProject().removePluginConfig(ref);
	}

	public IProjectDetails getProjectDetails() throws TigerstripeException {
		return getTSProject().getIProjectDetails();
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		getTSProject().reload(true);
		getTSProject().validate(visitor);
	}

	public IDependency makeDependency(String relativePath)
			throws TigerstripeException {
		return new Dependency(getTSProject(), relativePath);
	}

	public IDependency[] getDependencies() throws TigerstripeException {
		return getTSProject().getDependencies();
	}

	public void addDependency(IDependency dependency, IProgressMonitor monitor)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().addDependency(dependency);
		IPath path = new Path(dependency.getPath());
		ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
				this, IProjectDependencyDelta.PROJECT_DEPENDENCY_ADDED, path, dependency);
		broadcastProjectDependencyChange(delta);
	}

	public void addDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().addDependencies(dependencies);
		for (IDependency dep : dependencies) {
			IPath path = new Path(dep.getPath());
			ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
					this, IProjectDependencyDelta.PROJECT_DEPENDENCY_ADDED,
					path, dep);
			broadcastProjectDependencyChange(delta);
		}
	}

	public void removeDependency(IDependency dependency,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().removeDependency(dependency);
		IPath path = new Path(dependency.getPath());
		ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
				this, IProjectDependencyDelta.PROJECT_DEPENDENCY_REMOVED, path, dependency);
		broadcastProjectDependencyChange(delta);
	}

	public void removeDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().removeDependencies(dependencies);
		for (IDependency dep : dependencies) {
			IPath path = new Path(dep.getPath());
			ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
					this, IProjectDependencyDelta.PROJECT_DEPENDENCY_REMOVED,
					path, dep);
			broadcastProjectDependencyChange(delta);
		}
	}

	public boolean hasDependency(IDependency dep) throws TigerstripeException {
		if (dep == null || !dep.isValid())
			throw new TigerstripeException("Invalid dependency");
		TigerstripeProject project = getTSProject();
		return Arrays.asList(project.getDependencies()).contains(dep);
	}

	public ITigerstripeModelProject[] getReferencedProjects()
			throws WorkingCopyException, TigerstripeException {
		return getTSProject().getReferencedProjects();
	}

	public void removeModelReference(ModelReference modelRef)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().removeModelReference(modelRef);
		ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
				this, IProjectDependencyDelta.PROJECT_REFERENCE_REMOVED,
				modelRef.isResolved() ? modelRef.getResolvedModel()
						.getFullPath() : null, modelRef);
		broadcastProjectDependencyChange(delta);
	}

	public void removeModelReferences(ModelReference[] modelRefs)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().removeModelReferences(modelRefs);
		for (ModelReference modelRef : modelRefs) {
			ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
					this, IProjectDependencyDelta.PROJECT_REFERENCE_REMOVED,
					modelRef.isResolved() ? modelRef.getResolvedModel()
							.getFullPath() : null, modelRef);
			broadcastProjectDependencyChange(delta);
		}
	}

	public void addModelReference(ModelReference modelRef)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().addModelReference(modelRef);
		if (modelRef.isResolved()) {
			ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
					this, IProjectDependencyDelta.PROJECT_REFERENCE_ADDED,
					modelRef.getResolvedModel().getFullPath(), modelRef);
			broadcastProjectDependencyChange(delta);
		}
	}

	public void addModelReferences(ModelReference[] modelRefs)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		dependenciesCacheNeedsRefresh = true;
		getTSProject().addModelReferences(modelRefs);
		for (ModelReference modelRef : modelRefs) {
			if (modelRef.isResolved()) {
				ProjectDependencyChangeDelta delta = new ProjectDependencyChangeDelta(
						this, IProjectDependencyDelta.PROJECT_REFERENCE_ADDED,
						modelRef.getResolvedModel().getFullPath(), modelRef);
				broadcastProjectDependencyChange(delta);
			}
		}
	}

	public boolean hasReference(ITigerstripeModelProject project)
			throws TigerstripeException {
		return getTSProject().hasReference(project);
	}

	public String getAdvancedProperty(String property)
			throws TigerstripeException {
		return getTSProject().getAdvancedProperty(property);
	}

	public String getAdvancedProperty(String property, String defaultValue)
			throws TigerstripeException {
		return getTSProject().getAdvancedProperty(property, defaultValue);
	}

	public void setAdvancedProperty(String property, String value)
			throws TigerstripeException {
		assertSet();
		getTSProject().setAdvancedProperty(property, value);
	}

	public boolean requiresDescriptorUpgrade() throws TigerstripeException {
		return getTSProject().requiresDescriptorUpgrade();
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
				project.doSave(null);
				project.clearDirty();
				return;
			}
		} catch (TigerstripeException e) {
			if (tsProject != null) {
				// This means we are saving on an empty directory, and the
				// descriptor needs to be created now
				tsProject.doSave(null);
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
		assertSet();
		getTSProject().addFacetReference(facetRef);
	}

	public void removeFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		assertSet();
		getTSProject().removeFacetReference(facetRef);
	}

	public IFacetReference[] getFacetReferences() throws TigerstripeException {
		return getTSProject().getFacetReferences();
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

	public ITigerstripeModelProject[] getIReferencedProjects()
			throws TigerstripeException {
		return getReferencedProjects();
	}

	// ==========================================================================
	// ====
	// WorkingCopy stuff
	@Override
	public void doCommit(IProgressMonitor monitor) throws TigerstripeException {
		// doSave();
		//
		// TigerstripeProjectHandle original = (TigerstripeProjectHandle)
		// getOriginal();
		// original.getTSProject().reload(true); // this will force a reload.
		//
		// // Rebuild the cache if dependencies were added
		// if (dependenciesCacheNeedsRefresh) {
		// ((ArtifactManagerSessionImpl) original.getArtifactManagerSession())
		// .getArtifactManager().updateDependenciesContentCache(
		// monitor);
		// dependenciesCacheNeedsRefresh = false;
		// }

		TigerstripeProjectHandle original = (TigerstripeProjectHandle) getOriginal();
		original.reloadFrom(getTSProject(), dependenciesCacheNeedsRefresh,
				monitor);
		original.doSave();

		getTSProject().clearDirty();
		dependenciesCacheNeedsRefresh = false;
	}

	public PluginRunStatus[] generate(IM1RunConfig config,
			IProgressMonitor monitor) throws TigerstripeException {

		M1Generator generator = new M1Generator(this, (M1RunConfig) config);
		if (monitor == null)
			return generator.run();
		else
			return generator.run(monitor);
	}

	// ========================================
	// Project dependency change listeners stuff
	public void addProjectDependencyChangeListener(
			IProjectDependencyChangeListener listener) {
		if (isWorkingCopy()) {
			TigerstripeProjectHandle original = (TigerstripeProjectHandle) getOriginal();
			original.addProjectDependencyChangeListener(listener);
		} else
			projectChangeListeners.add(listener);
	}

	public void removeProjectDependencyChangeListener(
			IProjectDependencyChangeListener listener) {
		if (isWorkingCopy()) {
			TigerstripeProjectHandle original = (TigerstripeProjectHandle) getOriginal();
			if (original != null) {
				original.removeProjectDependencyChangeListener(listener);
			}
		} else
			projectChangeListeners.remove(listener);
	}

	public void broadcastProjectDependencyChange(
			final IProjectDependencyDelta delta) {

		if (isWorkingCopy()) {
			TigerstripeProjectHandle original = (TigerstripeProjectHandle) getOriginal();
			original.broadcastProjectDependencyChange(delta);
		} else {

			Object[] objects = projectChangeListeners.getListeners();
			for (Object obj : objects) {
				final IProjectDependencyChangeListener listener = (IProjectDependencyChangeListener) obj;
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						listener.projectDependenciesChanged(delta);
					}
				});
			}
		}
	}

	public void reloadFrom(TigerstripeProject descriptor,
			boolean dependenciesCacheNeedsRefresh, IProgressMonitor monitor)
			throws TigerstripeException {
		StringReader reader = new StringReader(descriptor.asText());
		getTSProject().reloadFrom(reader);

		if (dependenciesCacheNeedsRefresh) {
			// Rebuild the cache if dependencies were added
			((ArtifactManagerSessionImpl) getArtifactManagerSession())
					.getArtifactManager().updateDependenciesContentCache(
							monitor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject#getModelId
	 * ()
	 */
	public String getModelId() throws TigerstripeException {
		return getProjectDetails().getModelId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject#setModelId
	 * (java.lang.String)
	 */
	public void setModelId(String modelId) throws WorkingCopyException,
			TigerstripeException {
		IProjectDetails details = getProjectDetails();
		details.setModelId(modelId);
		setProjectDetails(details);
	}

	public ModelReference[] getModelReferences() throws TigerstripeException {
		return getTSProject().getModelReferences();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (manager != null) {
			manager.dispose();
		}
		if (tsProject != null) {
			tsProject.dispose();
		}
		wasDisposed = true;
	}

}
