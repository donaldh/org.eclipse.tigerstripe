package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.ContextualModelProject;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import com.thoughtworks.qdox.model.JavaSource;

public class ContextualArtifactManager implements ArtifactManager {

	private final ArtifactManager mgr;
	private final ITigerstripeModelProject context;
	
	public ContextualArtifactManager(ArtifactManager mgr,
			ITigerstripeModelProject context) {
		this.mgr = mgr;
		this.context = context;
	}

	public <T extends IModelComponent> List<T> wrapList(List<T> artifacts) {
		return WrapHelper.wrapList(context, artifacts);
	}

	public <T extends IModelComponent> Collection<T> wrapCollection(Collection<T> artifacts) {
		return WrapHelper.wrapCollection(context, artifacts);
	}

	private <T extends IModelComponent> T wrap(T art) {
		return WrapHelper.wrap(context, art);
	}

	public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypeArtifacts() {
		return mgr.getReservedPrimitiveTypeArtifacts();
	}

	public void dispose() {
		mgr.dispose();
	}

	public boolean wasDisposed() {
		return mgr.wasDisposed();
	}

	public void reset(IProgressMonitor monitor) {
		mgr.reset(monitor);
	}

	public Collection<IAbstractArtifact> getRegisteredArtifacts() {
		return mgr.getRegisteredArtifacts();
	}

	public void updateDependenciesContentCache(IProgressMonitor monitor) {
		mgr.updateDependenciesContentCache(monitor);
	}

	public void registerDiscoverableArtifact(IAbstractArtifactInternal artifact)
			throws IllegalArgumentException {
		mgr.registerDiscoverableArtifact(artifact);
	}

	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model, boolean includeDependencies,
			IProgressMonitor monitor) {
		return wrapList(mgr.getArtifactsByModel(model, includeDependencies, monitor));
	}

	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model, boolean includeDependencies,
			boolean overridePredicate, IProgressMonitor monitor) {
		return wrapList(mgr.getArtifactsByModel(model, includeDependencies,
				overridePredicate, monitor));
	}

	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model, boolean includeDependencies,
			ExecutionContext context) {
		return wrapList(mgr.getArtifactsByModel(model, includeDependencies, context));
	}

	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model, boolean includeDependencies,
			boolean overridePredicate, ExecutionContext context) {
		return wrapList(mgr.getArtifactsByModel(model, includeDependencies,
				overridePredicate, context));
	}

	public IAbstractArtifactInternal getArtifactByFilename(String filename) {
		return wrap(mgr.getArtifactByFilename(filename));
	}

	public List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
			boolean isOverridePredicate, IProgressMonitor monitor) {
		return wrapList(mgr.getAllArtifacts(includeDependencies, isOverridePredicate,
				monitor));
	}

	public List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
			ExecutionContext context) {
		return wrapList(mgr.getAllArtifacts(includeDependencies, context));
	}

	public List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
			boolean isOverridePredicate, ExecutionContext context) {
		return wrapList(mgr.getAllArtifacts(includeDependencies, isOverridePredicate,
				context));
	}

	public Collection<IAbstractArtifact> getAllArtifacts(
			boolean includeDependencies, IProgressMonitor monitor) {
		return wrapCollection(mgr.getAllArtifacts(includeDependencies, monitor));
	}

	public Collection getModelArtifacts(boolean includeDependencies,
			IProgressMonitor monitor) {
		return wrapCollection(mgr.getModelArtifacts(includeDependencies, monitor));
	}

	public Collection getModelArtifacts(boolean includeDependencies,
			boolean overridePredicate, IProgressMonitor monitor) {
		return wrapCollection(mgr.getModelArtifacts(includeDependencies, overridePredicate,
				monitor));
	}

	public Collection getCapabilitiesArtifacts(boolean includeDependencies,
			IProgressMonitor monitor) {
		return wrapCollection(mgr.getCapabilitiesArtifacts(includeDependencies, monitor));
	}

	public Collection getCapabilitiesArtifacts(boolean includeDependencies,
			boolean overridePredicate, IProgressMonitor monitor) {
		return wrapCollection(mgr.getCapabilitiesArtifacts(includeDependencies,
				overridePredicate, monitor));
	}

	public Collection getCapabilitiesArtifacts(boolean includeDependencies,
			boolean overridePredicate, ExecutionContext context) {
		return wrapCollection(mgr.getCapabilitiesArtifacts(includeDependencies,
				overridePredicate, context));
	}

	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name, boolean includeDependencies, IProgressMonitor monitor) {
		return wrap(mgr.getArtifactByFullyQualifiedName(name, includeDependencies,
				monitor));
	}

	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name, boolean includeDependencies, ExecutionContext context) {
		return wrap(mgr.getArtifactByFullyQualifiedName(name, includeDependencies,
				context));
	}

	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name, boolean includeDependencies,
			boolean isOverridePredicate, IProgressMonitor monitor) {
		return wrap(mgr.getArtifactByFullyQualifiedName(name, includeDependencies,
				isOverridePredicate, monitor));
	}

	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name, boolean includeDependencies,
			boolean isOverridePredicate, ExecutionContext context) {
		return wrap(mgr.getArtifactByFullyQualifiedName(name, includeDependencies,
				isOverridePredicate, context));
	}

	public void lock(boolean isLocked) {
		mgr.lock(isLocked);
	}

	public boolean isLocked() {
		return mgr.isLocked();
	}

	public void generationStart() {
		mgr.generationStart();
	}

	public void generationComplete() {
		mgr.generationComplete();
	}

	public void refresh(boolean forceReload, IProgressMonitor monitor) {
		mgr.refresh(forceReload, monitor);
	}

	public void refreshReferences(IProgressMonitor monitor) {
		mgr.refreshReferences(monitor);
	}

	public void updateCaches(IProgressMonitor monitor) {
		mgr.updateCaches(monitor);
	}

	public void addArtifactManagerListener(IArtifactChangeListener listener) {
		mgr.addArtifactManagerListener(listener);
	}

	public void removeArtifactManagerListener(IArtifactChangeListener listener) {
		mgr.removeArtifactManagerListener(listener);
	}

	public TigerstripeProject getTSProject() {
		return mgr.getTSProject();
	}

	public IAbstractArtifactInternal extractArtifact(JavaSource source,
			IProgressMonitor monitor) throws TigerstripeException {
		return wrap(mgr.extractArtifact(source, monitor));
	}

	public IAbstractArtifactInternal extractArtifact(Reader reader,
			IProgressMonitor monitor) throws TigerstripeException {
		return wrap(mgr.extractArtifact(reader, monitor));
	}

	public IAbstractArtifactInternal extractArtifactModel(Reader reader)
			throws TigerstripeException {
		return wrap(mgr.extractArtifactModel(reader));
	}

	public void addArtifact(IAbstractArtifact iartifact,
			IProgressMonitor monitor) throws TigerstripeException {
		mgr.addArtifact(iartifact, monitor);
	}

	public void removeArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		mgr.removeArtifact(artifact);
	}

	public void removeArtifact(IAbstractArtifact artifact,
			Set<ITigerstripeModelProject> ignoreProjects)
			throws TigerstripeException {
		mgr.removeArtifact(artifact, ignoreProjects);
	}

	public IDependency[] getProjectDependencies() {
		return mgr.getProjectDependencies();
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn, IProgressMonitor monitor) {
		return wrapCollection(mgr.getAllKnownArtifactsByFullyQualifiedName(fqn, monitor));
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn, ExecutionContext context) {
		return wrapCollection(mgr.getAllKnownArtifactsByFullyQualifiedName(fqn, context));
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInModules(
			String fqn, IProgressMonitor monitor) {
		return wrapCollection(mgr.getAllKnownArtifactsByFullyQualifiedNameInModules(fqn,
				monitor));
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInModules(
			String fqn, ExecutionContext context) {
		return wrapCollection(mgr.getAllKnownArtifactsByFullyQualifiedNameInModules(fqn,
				context));
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
			String fqn) {
		return wrapCollection(mgr
				.getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(fqn));
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
			String fqn, ExecutionContext context) {
		return wrapCollection(mgr
				.getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
						fqn, context));
	}

	public List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		return wrapList(mgr.getOriginatingRelationshipForFQN(fqn,
				includeProjectDependencies));
	}

	public List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies, boolean ignoreFacets)
			throws TigerstripeException {
		return wrapList(mgr.getOriginatingRelationshipForFQN(fqn,
				includeProjectDependencies, ignoreFacets));
	}

	public List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		return wrapList(mgr.getTerminatingRelationshipForFQN(fqn,
				includeProjectDependencies));
	}

	public List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies, boolean ignoreFacet)
			throws TigerstripeException {
		return wrapList(mgr.getTerminatingRelationshipForFQN(fqn,
				includeProjectDependencies, ignoreFacet));
	}

	public List<ITigerstripeModelProject> collectReferencedProjectsAndDependencies()
			throws TigerstripeException {
		List<ITigerstripeModelProject> refs = mgr.collectReferencedProjectsAndDependencies();
		List<ITigerstripeModelProject> wrapped = new ArrayList<ITigerstripeModelProject>(refs.size());
		for (ITigerstripeModelProject proj : refs) {
			wrapped.add(new ContextualModelProject(proj, context));
		}
		return wrapped; 
	}

	public void notifyArtifactDeleted(IAbstractArtifact artifact) {
		mgr.notifyArtifactDeleted(artifact);
	}

	public void notifyArtifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		mgr.notifyArtifactRenamed(artifact, fromFQN);
	}

	public void renameArtifact(IAbstractArtifact artifact, String toFQN,
			IProgressMonitor monitor) throws TigerstripeException {
		mgr.renameArtifact(artifact, toFQN, monitor);
	}

	public void addActiveFacetListener(IActiveFacetChangeListener listener) {
		mgr.addActiveFacetListener(listener);
	}

	public void removeActiveFacetListener(IActiveFacetChangeListener listener) {
		mgr.removeActiveFacetListener(listener);
	}

	public IFacetReference getActiveFacet() throws TigerstripeException {
		return mgr.getActiveFacet();
	}

	public void resetActiveFacet() throws TigerstripeException {
		mgr.resetActiveFacet();
	}

	public void setActiveFacet(IFacetReference facetRef,
			IProgressMonitor monitor) throws TigerstripeException {
		mgr.setActiveFacet(facetRef, monitor);
	}

	public boolean isInActiveFacet(IAbstractArtifact artifact)
			throws TigerstripeException {
		return mgr.isInActiveFacet(artifact);
	}

	public void resetBroadcastMask() {
		mgr.resetBroadcastMask();
	}

	public void setBroadcastMask(int broadcastMask) {
		mgr.setBroadcastMask(broadcastMask);
	}

	public long getLocalTimeStamp() {
		return mgr.getLocalTimeStamp();
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		mgr.annotationChanged(delta);
	}

	public void descriptorChanged(IResource changedDescriptor) {
		mgr.descriptorChanged(changedDescriptor);
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		mgr.modelChanged(delta);
	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		mgr.projectAdded(project);
	}

	public void projectDeleted(String projectName) {
		mgr.projectDeleted(projectName);
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
		mgr.artifactResourceChanged(changedArtifactResource);
	}

	public void artifactResourceAdded(IResource addedArtifactResource) {
		mgr.artifactResourceAdded(addedArtifactResource);
	}

	public void artifactResourceRemoved(IResource removedArtifactResource) {
		mgr.artifactResourceRemoved(removedArtifactResource);
	}

	public void activeFacetChanged(ITigerstripeModelProject project) {
		mgr.activeFacetChanged(project);
	}

	public void addDisposeListener(IDisposeListener listener) {
		mgr.addDisposeListener(listener);
	}

	public void removeDisposeListener(IDisposeListener listener) {
		mgr.removeDisposeListener(listener);
	}
}
