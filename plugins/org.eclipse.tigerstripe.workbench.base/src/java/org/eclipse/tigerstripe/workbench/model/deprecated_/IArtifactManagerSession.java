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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

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
import org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

/**
 * Interface to an Artifact Manager
 * 
 * This interface controls access to artifacts for a Tigerstripe project.
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IArtifactManagerSession {

    /**
     * Returns a list of all supported Artifact Types
     * 
     */
    public Collection<Class<?>> getSupportedArtifactClasses();

    /**
     * Returns a list of supported QueryArtifacts
     * 
     */
    public String[] getSupportedQueries();

    /**
     * Makes a new Artifact of the given type. Valid types are returned by
     * getSupportedArtifacts()
     * 
     * @param artifactType
     *            - String the fully qualified name of the artifact to make.
     *            Valid fully qualified artifact types are given by
     *            getSupportedArtifacts().
     * @see #getSupportedArtifacts()
     */
    public IAbstractArtifact makeArtifact(String artifactType)
            throws IllegalArgumentException;

    /**
     * Makes a new Artifact of similar type.
     * 
     * This method creates a new Artifact of the same type as the one provided
     * as parameter. No values are duplicated from the model.
     * 
     * @param model
     *            - IAbstractArtifact the artifact of the type to be made
     */
    public IAbstractArtifact makeArtifact(IAbstractArtifact model);

    /**
     * Adds the artifact to the session manager.
     * 
     * @param artifact
     *            - the artifact to add
     * @throws TigerstripeException
     *             if the artifact cannot be added
     */
    public void addArtifact(IAbstractArtifact artifact)
            throws TigerstripeException;

    /**
     * Removes the artifact from the session manager.
     * 
     * @param artifact
     *            - the artifact to remove
     * @throws TigerstripeException
     *             if the artifact cannot be removed
     */
    public void removeArtifact(IAbstractArtifact artifact)
            throws TigerstripeException;

    public IAbstractArtifact getArtifactByFullyQualifiedName(String fqn)
            throws TigerstripeException;

    public IAbstractArtifact getArtifactByFullyQualifiedName(String fqn,
            ExecutionContext context) throws TigerstripeException;

    public IAbstractArtifact getArtifactByFullyQualifiedName(String fqn,
            boolean includeDependencies);

    /**
     * Returns all known artifacts with the given FQN. If multiple definitions
     * are found along the classpath (modules and dependencies), they are
     * returned in the order they are found.
     * 
     * @param fqn
     * @return
     */
    public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
            String fqn);

    /**
     * Extracts an artifact from the given reader
     * 
     * @param reader
     * @param source TODO
     * @return
     * @throws TigerstripeException
     */
    public IAbstractArtifact extractArtifact(Reader reader,
            String source, IProgressMonitor monitor) throws TigerstripeException;

    public IAbstractArtifact extractArtifactModel(Reader reader)
            throws TigerstripeException;

    /**
     * Refreshes the session by re-reading all artifacts Equivalent to
     * refresh(false)
     * 
     * @throws TigerstripeException
     */
    public void refresh(IProgressMonitor monitor) throws TigerstripeException;

    /**
     * Refreshes the session by re-reading all artifacts
     * 
     * @param forceReload
     *            , it true, POJOs will be reparsed, even if they haven't
     *            changed.
     * @throws TigerstripeException
     */
    public void refresh(boolean forceReload, IProgressMonitor monitor)
            throws TigerstripeException;

    /**
     * Refreshes all the references to other projects
     */
    public void refreshReferences(IProgressMonitor monitor)
            throws TigerstripeException;

    /**
     * Refreshes everything about this session (local artifacts & references)
     * Equivalent to refreshAll(false)
     */
    public void refreshAll(IProgressMonitor monitor)
            throws TigerstripeException;

    /**
     * Refreshes everything about this session (local artifacts & references)
     */
    public void refreshAll(boolean forceReload, IProgressMonitor monitor)
            throws TigerstripeException;

    /**
     * Makes a new Artifact of type "model" based on the providing artifact,
     * i.e. trying to map as much as possible from the original artifact into
     * the new one.
     */
    public IAbstractArtifact makeArtifact(IAbstractArtifact model,
            IAbstractArtifact orig) throws TigerstripeException;

    public void addArtifactChangeListener(IArtifactChangeListener listener);

    public void removeArtifactChangeListener(IArtifactChangeListener listener);

    /**
     * Returns the IModelUpdater for this session
     * 
     * @return
     */
    public IModelUpdater getIModelUpdater();

    public void updateCaches(IProgressMonitor monitor)
            throws TigerstripeException;

    // =================================================================
    // Facet Management
    public void setActiveFacet(IFacetReference facet, IProgressMonitor monitor)
            throws TigerstripeException;

    public void setActiveFacet(IFacetReference facet, ExecutionContext context)
            throws TigerstripeException;

    public void resetActiveFacet() throws TigerstripeException;

    public void resetActiveFacet(ExecutionContext context)
            throws TigerstripeException;

    public IFacetReference getActiveFacet() throws TigerstripeException;

    public void addActiveFacetListener(IActiveFacetChangeListener listener);

    public void removeActiveFacetListener(IActiveFacetChangeListener listener);

    // ============================

    /**
     * Removes all the artifacts contained in the given package
     * 
     * NOTE: this doesn't recursively delete the content of sub-packages
     * 
     * @param packageName
     * @return a collection of IRelationship to potentially cascade delete
     */
    public Set<IRelationship> removePackageContent(String packageName);

    /**
     * Removes all the artifacts contained in the given package
     * 
     * NOTE: this doesn't recursively delete the content of sub-packages
     * 
     * @param packageName
     */
    public void renamePackageContent(String fromPackageName,
            String toPackageName);

    public void renameArtifact(IAbstractArtifact artifact, String toFQN)
            throws TigerstripeException;

    public void generationStart();

    public void generationComplete();

    public List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
            boolean includeProjectDependencies) throws TigerstripeException;

    public List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
            boolean includeProjectDependencies) throws TigerstripeException;

    public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypes()
            throws TigerstripeException;

    /**
     * This mask will condition what is and what is not broadcast by the
     * underlying manager
     * 
     * @param broadcastMask
     *            - valid values are a combination of
     *            {@link IArtifactChangeListener#NOTIFY_ADDED} etc..
     */
    public void setBroadcastMask(int broadcastMask) throws TigerstripeException;

    public void resetBroadcastMask() throws TigerstripeException;

    /**
     * Returns a list of all supported Artifact Types
     * 
     */
    public Collection<String> getSupportedArtifacts();

    /**
     * Makes a new Artifact Query
     */
    public IArtifactQuery makeQuery(String queryType)
            throws IllegalArgumentException;

    /**
     * Query artifacts based on the given query Object
     * 
     * @param query
     *            - ArtifactQuery the query to execute
     */
    public Collection<IAbstractArtifact> queryArtifact(IArtifactQuery query)
            throws IllegalArgumentException, TigerstripeException;

    /**
     * Returns the last time of modification on the model, whatever the change
     * is
     * 
     * @return
     */
    public long getLocalTimeStamp() throws TigerstripeException;

    public ArtifactManager getArtifactManager();
}
