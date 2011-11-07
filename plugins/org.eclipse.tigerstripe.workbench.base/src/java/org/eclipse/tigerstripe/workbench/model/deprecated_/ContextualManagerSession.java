package org.eclipse.tigerstripe.workbench.model.deprecated_;

import static org.eclipse.tigerstripe.workbench.internal.core.model.WrapHelper.wrap;

import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ContextualArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

public class ContextualManagerSession implements IArtifactManagerSession {

	private final IArtifactManagerSession session;
	private ITigerstripeModelProject context;

	public ContextualManagerSession(IArtifactManagerSession session,
			ITigerstripeModelProject context) {
		this.session = session;
	}
	
	public Collection<Class<?>> getSupportedArtifactClasses() {
		return session.getSupportedArtifactClasses();
	}

	public String[] getSupportedQueries() {
		return session.getSupportedQueries();
	}

	public IAbstractArtifact makeArtifact(String artifactType)
			throws IllegalArgumentException {
		return wrap(context, session.makeArtifact(artifactType));
	}

	public IAbstractArtifact makeArtifact(IAbstractArtifact model) {
		return wrap(context, session.makeArtifact(model));
	}

	public void addArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		session.addArtifact(artifact);
	}

	public void removeArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		session.removeArtifact(artifact);
	}

	public IAbstractArtifact getArtifactByFullyQualifiedName(String fqn)
			throws TigerstripeException {
		return session.getArtifactByFullyQualifiedName(fqn);
	}

	public IAbstractArtifact getArtifactByFullyQualifiedName(String fqn,
			ExecutionContext context) throws TigerstripeException {
		return session.getArtifactByFullyQualifiedName(fqn, context);
	}

	public IAbstractArtifact getArtifactByFullyQualifiedName(String fqn,
			boolean includeDependencies) {
		return session
				.getArtifactByFullyQualifiedName(fqn, includeDependencies);
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn) {
		return session.getAllKnownArtifactsByFullyQualifiedName(fqn);
	}

	public IAbstractArtifact extractArtifact(Reader reader,
			IProgressMonitor monitor) throws TigerstripeException {
		return session.extractArtifact(reader, monitor);
	}

	public IAbstractArtifact extractArtifactModel(Reader reader)
			throws TigerstripeException {
		return session.extractArtifactModel(reader);
	}

	public void refresh(IProgressMonitor monitor) throws TigerstripeException {
		session.refresh(monitor);
	}

	public void refresh(boolean forceReload, IProgressMonitor monitor)
			throws TigerstripeException {
		session.refresh(forceReload, monitor);
	}

	public void refreshReferences(IProgressMonitor monitor)
			throws TigerstripeException {
		session.refreshReferences(monitor);
	}

	public void refreshAll(IProgressMonitor monitor)
			throws TigerstripeException {
		session.refreshAll(monitor);
	}

	public void refreshAll(boolean forceReload, IProgressMonitor monitor)
			throws TigerstripeException {
		session.refreshAll(forceReload, monitor);
	}

	public IAbstractArtifact makeArtifact(IAbstractArtifact model,
			IAbstractArtifact orig) throws TigerstripeException {
		return wrap(context, session.makeArtifact(model, orig));
	}

	public void addArtifactChangeListener(IArtifactChangeListener listener) {
		session.addArtifactChangeListener(listener);
	}

	public void removeArtifactChangeListener(IArtifactChangeListener listener) {
		session.removeArtifactChangeListener(listener);
	}

	public IModelUpdater getIModelUpdater() {
		return session.getIModelUpdater();
	}

	public void updateCaches(IProgressMonitor monitor)
			throws TigerstripeException {
		session.updateCaches(monitor);
	}

	public void setActiveFacet(IFacetReference facet, IProgressMonitor monitor)
			throws TigerstripeException {
		session.setActiveFacet(facet, monitor);
	}

	public void resetActiveFacet() throws TigerstripeException {
		session.resetActiveFacet();
	}

	public IFacetReference getActiveFacet() throws TigerstripeException {
		return session.getActiveFacet();
	}

	public void addActiveFacetListener(IActiveFacetChangeListener listener) {
		session.addActiveFacetListener(listener);
	}

	public void removeActiveFacetListener(IActiveFacetChangeListener listener) {
		session.removeActiveFacetListener(listener);
	}

	public Set<IRelationship> removePackageContent(String packageName) {
		return session.removePackageContent(packageName);
	}

	public void renamePackageContent(String fromPackageName,
			String toPackageName) {
		session.renamePackageContent(fromPackageName, toPackageName);
	}

	public void renameArtifact(IAbstractArtifact artifact, String toFQN)
			throws TigerstripeException {
		session.renameArtifact(artifact, toFQN);
	}

	public void generationStart() {
		session.generationStart();
	}

	public void generationComplete() {
		session.generationComplete();
	}

	public List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		return session.getOriginatingRelationshipForFQN(fqn,
				includeProjectDependencies);
	}

	public List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		return session.getTerminatingRelationshipForFQN(fqn,
				includeProjectDependencies);
	}

	public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypes()
			throws TigerstripeException {
		return session.getReservedPrimitiveTypes();
	}

	public void setBroadcastMask(int broadcastMask) throws TigerstripeException {
		session.setBroadcastMask(broadcastMask);
	}

	public void resetBroadcastMask() throws TigerstripeException {
		session.resetBroadcastMask();
	}

	public Collection<String> getSupportedArtifacts() {
		return session.getSupportedArtifacts();
	}

	public IArtifactQuery makeQuery(String queryType)
			throws IllegalArgumentException {
		return session.makeQuery(queryType);
	}

	public Collection<IAbstractArtifact> queryArtifact(IArtifactQuery query)
			throws IllegalArgumentException, TigerstripeException {
		return session.queryArtifact(query);
	}

	public long getLocalTimeStamp() throws TigerstripeException {
		return session.getLocalTimeStamp();
	}

	public ArtifactManager getArtifactManager() {
		ArtifactManager mgr = session.getArtifactManager();
		
		if (mgr instanceof ContextualArtifactManager) {
			return mgr;
		} else {
			return new ContextualArtifactManager(mgr, context);
		}
	}
}
