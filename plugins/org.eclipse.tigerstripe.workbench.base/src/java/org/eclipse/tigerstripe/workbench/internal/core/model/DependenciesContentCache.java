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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.PredicateFilter;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IDependency;

/**
 * This implements a cache for all the dependencies attached to an Artifact
 * Manager.
 * 
 * Without this cache, as soon as the models becomes too big, performance drops
 * significantly!
 * 
 * Bug 928: the cache needs to be facet-aware, i.e. whenever a facet is active
 * in a project, it is propagated to all the referenced projects and
 * dependencies. Propagating to dependencies really mean propagating to this
 * cache. See {@link #setActiveFacet(IFacetReference)}
 * 
 * @author Eric Dillon
 * 
 */
public class DependenciesContentCache {

	private ArtifactManager manager;

	private HashMap<IAbstractArtifact, Set<IAbstractArtifact>> artifactsByModel;

	private Set<IAbstractArtifact> allArtifacts;

	private HashMap<String, IAbstractArtifact> artifactsByFqn;

	// bug 928: need to filter result being aware of potential active facet
	private ArtifactFilter artifactFilter = new ArtifactNoFilter();

	private HashMap<String, List<IAbstractArtifact>> allKnownArtifactsByFqn;

	private boolean isInitialized = false;

	private boolean inStateOfUpdate;

	public DependenciesContentCache(ArtifactManager manager) {
		this.manager = manager;
	}

	// Bug 928
	/* package */void setActiveFacet(IFacetReference facetRef) {
		this.artifactFilter = new PredicateFilter(facetRef.getFacetPredicate());
	}

	// Bug 928
	/* package */void resetActiveFacet() {
		this.artifactFilter = new ArtifactNoFilter();
	}

	private void cleanCache() {
		artifactsByModel = new HashMap<IAbstractArtifact, Set<IAbstractArtifact>>();
		allArtifacts = new HashSet<IAbstractArtifact>();
		artifactsByFqn = new HashMap<String, IAbstractArtifact>();
		allKnownArtifactsByFqn = new HashMap<String, List<IAbstractArtifact>>();
	}

	public synchronized void updateCache(ExecutionContext context) {
		if (inStateOfUpdate) {
			return;
		}

		boolean wasLocked = manager.isLocked();
		inStateOfUpdate = true;
		try {
			manager.lock(true);
			cleanCache();

			updateArtifactsByModel(context);
			updateAllArtifacts(context);
			updateArtifactsByFqn(context);
			updateAllKnownArtifactsByFqn(context);
			isInitialized = true;
		} finally {
			inStateOfUpdate = false;
			manager.lock(wasLocked);
		}
	}

	private void updateArtifactsByModel(ExecutionContext context) {
		Collection<IAbstractArtifact> registeredArtifacts = manager
				.getRegisteredArtifacts();
		context.getMonitor().beginTask("Cache update - Pass 1",
				registeredArtifacts.size());
		for (IAbstractArtifact model : registeredArtifacts) {
			Set<IAbstractArtifact> list = new HashSet<IAbstractArtifact>();
			for (IDependency dependency : manager.getProjectDependencies()) {
				Dependency dep = (Dependency) dependency;
				ArtifactManager artifactManager = dep
						.getArtifactManager(context.getMonitor());
				if (dep != null && artifactManager != null)
					list.addAll(artifactManager.getArtifactsByModel(
							(AbstractArtifact) model, true, context));
			}

			artifactsByModel.put(model, list);
			context.getMonitor().worked(1);
		}
		context.getMonitor().done();
	}

	private void updateAllArtifacts(ExecutionContext context) {
		Set<IAbstractArtifact> result = new HashSet<IAbstractArtifact>();
		for (IDependency dependency : manager.getProjectDependencies()) {
			Dependency dep = (Dependency) dependency;
			ArtifactManager artifactManager = dep.getArtifactManager(context
					.getMonitor());
			if (dep != null && artifactManager != null)
				result.addAll(artifactManager.getAllArtifacts(true, context));
		}

		allArtifacts = result;
	}

	// TODO: really this could be replaced now with by extracting the first item
	// in the allKnownArtifactsByFqn...
	private void updateArtifactsByFqn(ExecutionContext context) {
		artifactsByFqn = new HashMap<String, IAbstractArtifact>();
		for (IAbstractArtifact artifact : allArtifacts) {
			String fqn = artifact.getFullyQualifiedName();
			artifactsByFqn.put(fqn, artifact);
		}
	}

	private void updateAllKnownArtifactsByFqn(ExecutionContext context) {
		allKnownArtifactsByFqn = new HashMap<String, List<IAbstractArtifact>>();
		for (IAbstractArtifact artifact : allArtifacts) {
			String fqn = artifact.getFullyQualifiedName();
			if (allKnownArtifactsByFqn.containsKey(fqn)) {
				ArrayList<IAbstractArtifact> list = (ArrayList<IAbstractArtifact>) allKnownArtifactsByFqn
						.get(fqn);
				list.add(artifact);
			} else {
				ArrayList<IAbstractArtifact> list = new ArrayList<IAbstractArtifact>();
				list.add(artifact);
				allKnownArtifactsByFqn.put(fqn, list);
			}
		}
	}

	public synchronized Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn, ExecutionContext context) {
		if (!isInitialized) {
			updateCache(context);
		}

		if (allKnownArtifactsByFqn.containsKey(fqn))
			return ArtifactFilter.filter(allKnownArtifactsByFqn.get(fqn),
					artifactFilter);
		else
			return new ArrayList<IAbstractArtifact>();
	}

	public synchronized Collection<IAbstractArtifact> getArtifactsByModelInChained(
			AbstractArtifact model, ExecutionContext context) {
		if (!isInitialized)
			updateCache(context);

		return ArtifactFilter.filter(artifactsByModel.get(model),
				artifactFilter);
	}

	public synchronized Collection<IAbstractArtifact> getAllChainedArtifacts(
			ExecutionContext context) {
		if (!isInitialized)
			updateCache(context);

		return ArtifactFilter.filter(allArtifacts, artifactFilter);
	}

	public synchronized AbstractArtifact getArtifactByFullyQualifiedNameInChained(
			String name, ExecutionContext context) {
		if (!isInitialized)
			updateCache(context);

		AbstractArtifact potentialResult = (AbstractArtifact) artifactsByFqn
				.get(name);
		if (potentialResult != null && artifactFilter.select(potentialResult))
			return potentialResult;

		return null;
	}
}
