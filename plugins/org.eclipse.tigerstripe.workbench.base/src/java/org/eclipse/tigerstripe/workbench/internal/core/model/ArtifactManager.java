package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.Reader;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import com.thoughtworks.qdox.model.JavaSource;

/**
 * The Artifact Manager provides access to all source artifacts.
 * 
 * The Artifact manager is given the Java source files as input and using QDox
 * it parses all the source files and builds an internal model of the source
 * code including all tigerstripe tags.
 * 
 * Out of the QDox model, Tigerstripe artifacts are "extracted" through the
 * extractArtifact method. The extracted artifacts are based on a list of
 * "discoverable" artifacts, which allows to extend the list of artifact
 * supported by Tigerstripe.
 * 
 * Currently the following artifacts are discoverable: - EventArtifact,
 * DatatypeArtifact, ManagedEntityArtifact, SessionFacadeArtifact
 * 
 * See the AbstractArtifact class for more details.
 * 
 * Any plugin can register additional Artifacts that would be extracted and
 * managed by the ArtifactManager.
 * 
 * Once all the artifacts have been extracted, a semantic validation is
 * performed by calling the resolveReferences() method on each extracted
 * artifact.
 * 
 * @author Eric Dillon
 * 
 */
public interface ArtifactManager {

    public static interface IDisposeListener {
        void onDispose();
    }

    Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypeArtifacts();

    void dispose();

    boolean wasDisposed();

    /**
     * Resets the ArtifactManager and removes any extracted Artifacts.
     * 
     */
    void reset(IProgressMonitor monitor);

    Collection<IAbstractArtifact> getRegisteredArtifacts();

    void updateDependenciesContentCache(IProgressMonitor monitor);

    /**
     * Registers an artifact to be discovered by Tigerstripe when going through
     * the list of resources.
     * 
     * @param artifact
     *            - AbstractArtifact the artifact to register
     * @throws IllegalArgumentException
     *             , if artifact is null or already registered.
     */
    void registerDiscoverableArtifact(IAbstractArtifactInternal artifact)
            throws IllegalArgumentException;

    List<IAbstractArtifact> getArtifactsByModel(
            IAbstractArtifactInternal model, boolean includeDependencies,
            IProgressMonitor monitor);

    List<IAbstractArtifact> getArtifactsByModel(
            IAbstractArtifactInternal model, boolean includeDependencies,
            boolean overridePredicate, IProgressMonitor monitor);

    List<IAbstractArtifact> getArtifactsByModel(
            IAbstractArtifactInternal model, boolean includeDependencies,
            ExecutionContext context);

    List<IAbstractArtifact> getArtifactsByModel(
            IAbstractArtifactInternal model, boolean includeDependencies,
            boolean overridePredicate, ExecutionContext context);

    /**
     * 
     * @param name
     * @return Note: this doesn't go thru the dependencies
     */
    IAbstractArtifactInternal getArtifactByFilename(String filename);

    List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
            boolean isOverridePredicate, IProgressMonitor monitor);

    List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
            ExecutionContext context);

    /**
     * Returns all artifacts in this manager and in all the dependencies
     * 
     * @return
     */
    List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
            boolean isOverridePredicate, ExecutionContext context);

    Collection<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
            IProgressMonitor monitor);

    Collection getModelArtifacts(boolean includeDependencies,
            IProgressMonitor monitor);

    Collection getModelArtifacts(boolean includeDependencies,
            boolean overridePredicate, IProgressMonitor monitor);

    /**
     * @deprecated
     * @param includeDependencies
     * @param monitor
     * @return
     */
    @Deprecated
    Collection getCapabilitiesArtifacts(boolean includeDependencies,
            IProgressMonitor monitor);

    @Deprecated
    Collection getCapabilitiesArtifacts(boolean includeDependencies,
            boolean overridePredicate, IProgressMonitor monitor);

    @Deprecated
    Collection getCapabilitiesArtifacts(boolean includeDependencies,
            boolean overridePredicate, ExecutionContext context);

    IAbstractArtifactInternal getArtifactByFullyQualifiedName(String name,
            boolean includeDependencies, IProgressMonitor monitor);

    IAbstractArtifactInternal getArtifactByFullyQualifiedName(String name,
            boolean includeDependencies, ExecutionContext context);

    IAbstractArtifactInternal getArtifactByFullyQualifiedName(String name,
            boolean includeDependencies, boolean isOverridePredicate,
            IProgressMonitor monitor);

    IAbstractArtifactInternal getArtifactByFullyQualifiedName(String name,
            boolean includeDependencies, boolean isOverridePredicate,
            ExecutionContext context);

    void lock(boolean isLocked);

    boolean isLocked();

    // =======================================================================
    // Upon generation the Artifact mgr needs to change state. In particular,
    // the following should happen:
    // - locking: nothing can be added/removed.
    // - Active facet cannot be ignored: the default behavior when non
    // generate is to ignore facets for lookups/queries.
    void generationStart();

    void generationComplete();

    /**
     * Refreshes the ArtifactManager.
     * 
     * @param forceReload
     *            - if true the ArtifactManager will be fully reloaded from the
     *            project descriptor. If not, only deltas that have been posted
     *            will be applied.
     */
    void refresh(boolean forceReload, IProgressMonitor monitor);

    void refreshReferences(IProgressMonitor monitor);

    void updateCaches(IProgressMonitor monitor);

    /**
     * Add a listener to this Artifact Manager
     */
    void addArtifactManagerListener(IArtifactChangeListener listener);

    /**
     * Add a listener to this Artifact Manager
     */
    void removeArtifactManagerListener(IArtifactChangeListener listener);

    TigerstripeProject getTSProject();

    /**
     * Extracts an Artifact from the given source
     * 
     * @param source
     * @throws TigerstripeException
     * @return
     */
    IAbstractArtifactInternal extractArtifact(JavaSource source,
            IProgressMonitor monitor) throws TigerstripeException;

    /**
     * Extracts an Artifact from the given reader
     * 
     * @param reader
     * @return
     * @throws TigerstripeException
     */
    IAbstractArtifactInternal extractArtifact(Reader reader,
            IProgressMonitor monitor) throws TigerstripeException;

    IAbstractArtifactInternal extractArtifactModel(Reader reader)
            throws TigerstripeException;

    /**
     * Adds an artifact to this manager and updates all the internal tables
     * 
     * Listeners are notified of successful addition.
     * 
     * @param artifact
     *            the artifact to add
     * @throws TigerstripeException
     *             if the artifact cannot be properly added
     */
    void addArtifact(IAbstractArtifact iartifact, IProgressMonitor monitor)
            throws TigerstripeException;

    void removeArtifact(IAbstractArtifact artifact) throws TigerstripeException;

    /**
     * Removes an artifact from this manager and updates all the internal
     * tables.
     * 
     * Listeners are notified of successful removal.
     * 
     * @param artifact
     *            the artifact to remove
     * @throws TigerstripeException
     *             if the artifact cannot be properly removed
     */
    void removeArtifact(IAbstractArtifact artifact,
            Set<ITigerstripeModelProject> ignoreProjects)
            throws TigerstripeException;

    IDependency[] getProjectDependencies();

    Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
            String fqn, IProgressMonitor monitor);

    /**
     * Returns all known artifacts with the given FQN. If multiple definitions
     * are found along the classpath (modules and dependencies), they are
     * returned in the order they are found.
     * 
     * @param fqn
     * @return
     */
    Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
            String fqn, ExecutionContext context);

    Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInModules(
            String fqn, IProgressMonitor monitor);

    Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInModules(
            String fqn, ExecutionContext context);

    Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
            String fqn);

    Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
            String fqn, ExecutionContext context);

    List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
            boolean includeProjectDependencies) throws TigerstripeException;

    List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
            boolean includeProjectDependencies, boolean ignoreFacets)
            throws TigerstripeException;

    List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
            boolean includeProjectDependencies) throws TigerstripeException;

    List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
            boolean includeProjectDependencies, boolean ignoreFacet)
            throws TigerstripeException;

    List<ITigerstripeModelProject> collectReferencedProjectsAndDependencies()
            throws TigerstripeException;

    // This is a backdoor used in the TSDeleteAction to let the Art Mgr know
    // that an artifact was deleted after the fact.
    // Really the Art Mgr should be listenning for Workspace Changes here
    // and figure it out on its own.
    void notifyArtifactDeleted(IAbstractArtifact artifact);

    void notifyArtifactRenamed(IAbstractArtifact artifact, String fromFQN);

    void renameArtifact(IAbstractArtifact artifact, String toFQN,
            IProgressMonitor monitor) throws TigerstripeException;

    /**
     * Add a listener to this Artifact Manager
     */
    void addActiveFacetListener(IActiveFacetChangeListener listener);

    /**
     * Add a listener to this Artifact Manager
     */
    void removeActiveFacetListener(IActiveFacetChangeListener listener);

    IFacetReference getActiveFacet() throws TigerstripeException;

    void resetActiveFacet() throws TigerstripeException;

    void resetActiveFacet(ExecutionContext context) throws TigerstripeException;

    void setActiveFacet(IFacetReference facetRef, IProgressMonitor monitor)
            throws TigerstripeException;

    void setActiveFacet(IFacetReference facetRef, ExecutionContext context)
            throws TigerstripeException;

    /**
     * Returns true if the given artifact is part of the active facet, false
     * otherwise
     * 
     * @param artifact
     * @return
     */
    boolean isInActiveFacet(IAbstractArtifact artifact)
            throws TigerstripeException;

    void resetBroadcastMask();

    void setBroadcastMask(int broadcastMask);

    long getLocalTimeStamp();

    /**
     * Set of methods for TSChangeListener interface
     */

    void annotationChanged(IModelAnnotationChangeDelta[] delta);

    void descriptorChanged(IResource changedDescriptor);

    void modelChanged(IModelChangeDelta[] delta);

    void projectAdded(IAbstractTigerstripeProject project);

    void projectDeleted(String projectName);

    // This will be proviked by changes to .java or .project file changes
    // in the underlying fle system
    void artifactResourceChanged(IResource changedArtifactResource);

    void artifactResourceAdded(IResource addedArtifactResource);

    void artifactResourceRemoved(IResource removedArtifactResource);

    void activeFacetChanged(ITigerstripeModelProject project);

    void addDisposeListener(IDisposeListener listener);

    void removeDisposeListener(IDisposeListener listener);

    void loadPhantomManager();
}