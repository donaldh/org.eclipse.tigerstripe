package org.eclipse.tigerstripe.workbench.project;

import java.net.URI;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IOriginalChangeListener;
import org.eclipse.tigerstripe.workbench.IWorkingCopy;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.ContextualArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ProjectModelManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;

public class ContextualModelProject implements ITigerstripeModelProject {

	private final ITigerstripeModelProject project;
	private final ITigerstripeModelProject context;
	
	public ContextualModelProject(ITigerstripeModelProject project,
			ITigerstripeModelProject context) {
		this.project = project;
		this.context = context;
	}

	public boolean isWorkingCopy() {
		return project.isWorkingCopy();
	}

	public boolean isDirty() {
		return project.isDirty();
	}

	public IWorkingCopy makeWorkingCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		return project.makeWorkingCopy(monitor);
	}

	public String getName() {
		return project.getName();
	}

	public IProjectDetails getProjectDetails() throws TigerstripeException {
		return project.getProjectDetails();
	}

	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException {
		project.setProjectDetails(projectDetails);
	}

	public Object getAdapter(Class adapter) {
		return project.getAdapter(adapter);
	}

	public boolean exists() {
		return project.exists();
	}

	public Object getOriginal() throws TigerstripeException {
		return project.getOriginal();
	}

	public ProjectModelManager getModelManager() throws TigerstripeException {
		return project.getModelManager();
	}

	public IPath getLocation() {
		return project.getLocation();
	}

	public String getModelId() throws TigerstripeException {
		return project.getModelId();
	}

	public IPath getFullPath() {
		return project.getFullPath();
	}

	public void addOriginalChangeListener(IOriginalChangeListener listener) {
		project.addOriginalChangeListener(listener);
	}

	public void setModelId(String modelId) throws WorkingCopyException,
			TigerstripeException {
		project.setModelId(modelId);
	}

	public void delete(boolean force, IProgressMonitor monitor)
			throws TigerstripeException {
		project.delete(force, monitor);
	}

	public boolean containsErrors() {
		return project.containsErrors();
	}

	public IArtifactManagerSession getArtifactManagerSession()
			throws TigerstripeException {
		ContextualArtifactManager ctxMgr = new ContextualArtifactManager(
				project.getArtifactManagerSession().getArtifactManager(),
				context);
		return new ArtifactManagerSessionImpl(ctxMgr);
	}

	public void removeOriginalChangeListener(IOriginalChangeListener listener) {
		project.removeOriginalChangeListener(listener);
	}

	public PluginRunStatus[] generate(IM1RunConfig config,
			IProgressMonitor monitor) throws TigerstripeException {
		return project.generate(config, monitor);
	}

	public IDependency[] getDependencies() throws TigerstripeException {
		return project.getDependencies();
	}

	public void commit(IProgressMonitor monitor) throws TigerstripeException {
		project.commit(monitor);
	}

	public void removeDependency(IDependency dependency,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException {
		project.removeDependency(dependency, monitor);
	}

	public void removeDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException {
		project.removeDependencies(dependencies, monitor);
	}

	public void dispose() {
		project.dispose();
	}

	public void addDependency(IDependency dependency, IProgressMonitor monitor)
			throws WorkingCopyException, TigerstripeException {
		project.addDependency(dependency, monitor);
	}

	public void addDependencies(IDependency[] dependencies,
			IProgressMonitor monitor) throws WorkingCopyException,
			TigerstripeException {
		project.addDependencies(dependencies, monitor);
	}

	public IDependency makeDependency(String relativePath)
			throws TigerstripeException {
		return project.makeDependency(relativePath);
	}

	public IFacetReference makeFacetReference(URI facetURI)
			throws TigerstripeException {
		return project.makeFacetReference(facetURI);
	}

	public IFacetReference makeFacetReference(String projectRelativePath)
			throws TigerstripeException {
		return project.makeFacetReference(projectRelativePath);
	}

	public IUseCaseReference makeUseCaseReference(String projectRelativePath)
			throws TigerstripeException {
		return project.makeUseCaseReference(projectRelativePath);
	}

	public void addFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		project.addFacetReference(facetRef);
	}

	public void removeFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		project.removeFacetReference(facetRef);
	}

	public IFacetReference[] getFacetReferences() throws TigerstripeException {
		return project.getFacetReferences();
	}

	public boolean hasDependency(IDependency dep) throws TigerstripeException {
		return project.hasDependency(dep);
	}

	public void addModelReference(ModelReference modelRef)
			throws WorkingCopyException, TigerstripeException {
		project.addModelReference(modelRef);
	}

	public void addModelReferences(ModelReference[] modelRefs)
			throws WorkingCopyException, TigerstripeException {
		project.addModelReferences(modelRefs);
	}

	public void removeModelReference(ModelReference modelRef)
			throws WorkingCopyException, TigerstripeException {
		project.removeModelReference(modelRef);
	}

	public void removeModelReferences(ModelReference[] modelRefs)
			throws WorkingCopyException, TigerstripeException {
		project.removeModelReferences(modelRefs);
	}

	public boolean hasReference(ITigerstripeModelProject project)
			throws WorkingCopyException, TigerstripeException {
		return project.hasReference(project);
	}

	public void setAdvancedProperty(String property, String value)
			throws WorkingCopyException, TigerstripeException {
		project.setAdvancedProperty(property, value);
	}

	public IPluginConfig[] getPluginConfigs() throws TigerstripeException {
		return project.getPluginConfigs();
	}

	public void addPluginConfig(IPluginConfig config)
			throws TigerstripeException {
		project.addPluginConfig(config);
	}

	public void removePluginConfig(IPluginConfig config)
			throws TigerstripeException {
		project.removePluginConfig(config);
	}

	public ITigerstripeModelProject[] getReferencedProjects()
			throws TigerstripeException {
		ITigerstripeModelProject[] real = project.getReferencedProjects();
		ITigerstripeModelProject[] viewed = new ITigerstripeModelProject[real.length]; 
		for (int i = 0; i < real.length; ++i) {
			if (real[i] != null) {
				viewed[i] = new ContextualModelProject(real[i], context);
			}
		}
		return viewed;
	}

	private ModelReference[] wrapRefs(ModelReference[] real) {
		ModelReference[] viewed = new ModelReference[real.length]; 
		for (int i = 0; i < real.length; ++i) {
			if (real[i] != null) {
				ITigerstripeModelProject proj = real[i].getProjectContext();
				if (proj == null) {
					viewed[i] = real[i];
				} else {
					viewed[i] = new ModelReference(new ContextualModelProject(
							proj, context), real[i].getToModelId());
				}
			}
		}
		return viewed;
	}
	
	public ModelReference[] getModelReferences() throws TigerstripeException {
		return wrapRefs(project.getModelReferences());
	}

	public ModelReference[] getReferencingModels(int level)
			throws TigerstripeException {
		return wrapRefs(project.getReferencingModels(level));
	}

	public void setActiveFacet(IFacetReference facet, IProgressMonitor monitor)
			throws TigerstripeException {
		project.setActiveFacet(facet, monitor);
	}

	public void resetActiveFacet() throws TigerstripeException {
		project.resetActiveFacet();
	}

	public IFacetReference getActiveFacet() throws TigerstripeException {
		return project.getActiveFacet();
	}

	public String getAdvancedProperty(String property)
			throws TigerstripeException {
		return project.getAdvancedProperty(property);
	}

	public String getAdvancedProperty(String property, String defaultValue)
			throws TigerstripeException {
		return project.getAdvancedProperty(property, defaultValue);
	}

	public void addProjectDependencyChangeListener(
			IProjectDependencyChangeListener listener) {
		project.addProjectDependencyChangeListener(listener);
	}

	public void removeProjectDependencyChangeListener(
			IProjectDependencyChangeListener listener) {
		project.removeProjectDependencyChangeListener(listener);
	}

	public boolean wasDisposed() {
		return project.wasDisposed();
	}
}
