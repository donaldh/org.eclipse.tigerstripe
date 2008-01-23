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
package org.eclipse.tigerstripe.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.contract.predicate.PredicateFilter;
import org.eclipse.tigerstripe.core.project.Dependency;

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

	private HashMap<IAbstractArtifact, List<IAbstractArtifact>> artifactsByModel;

	private List<IAbstractArtifact> allArtifacts;

	private HashMap artifactsByFqn;

	// bug 928: need to filter result being aware of potential active facet
	private ArtifactFilter artifactFilter = new ArtifactNoFilter();

	private HashMap<String, List<IAbstractArtifact>> allKnownArtifactsByFqn;

	private boolean isInitialized = false;

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
		artifactsByModel = new HashMap();
		allArtifacts = new ArrayList();
		artifactsByFqn = new HashMap();
		allKnownArtifactsByFqn = new HashMap<String, List<IAbstractArtifact>>();
	}

	public synchronized void updateCache(ITigerstripeProgressMonitor monitor) {

		cleanCache();

		updateArtifactsByModel(monitor);
		updateAllArtifacts(monitor);
		updateArtifactsByFqn(monitor);
		updateAllKnownArtifactsByFqn(monitor);
		isInitialized = true;
	}

	private void updateArtifactsByModel(ITigerstripeProgressMonitor monitor) {
		Collection registeredArtifacts = manager.getRegisteredArtifacts();
		monitor.beginTask("Cache update - Pass 1", registeredArtifacts.size());
		for (Iterator iterModel = registeredArtifacts.iterator(); iterModel
				.hasNext();) {
			IAbstractArtifact model = (IAbstractArtifact) iterModel.next();
			List<IAbstractArtifact> list = new ArrayList<IAbstractArtifact>();
			for (Iterator iter = manager.getProjectDependencies().iterator(); iter
					.hasNext();) {
				Dependency dep = (Dependency) iter.next();
				if (dep != null && dep.getArtifactManager(monitor) != null)
					list.addAll(dep.getArtifactManager(monitor)
							.getArtifactsByModel((AbstractArtifact) model,
									false, monitor));
			}

			artifactsByModel.put(model, list);
			monitor.worked(1);
		}
		monitor.done();
	}

	private void updateAllArtifacts(ITigerstripeProgressMonitor monitor) {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		for (Iterator iter = manager.getProjectDependencies().iterator(); iter
				.hasNext();) {
			Dependency dep = (Dependency) iter.next();
			if (dep != null && dep.getArtifactManager(monitor) != null)
				result.addAll(dep.getArtifactManager(monitor).getAllArtifacts(
						true, monitor));
		}

		allArtifacts = result;
	}

	// TODO: really this could be replaced now with by extracting the first item
	// in the allKnownArtifactsByFqn...
	private void updateArtifactsByFqn(ITigerstripeProgressMonitor monitor) {
		artifactsByFqn = new HashMap();
		for (Iterator iter = allArtifacts.iterator(); iter.hasNext();) {
			AbstractArtifact artifact = (AbstractArtifact) iter.next();
			String fqn = artifact.getFullyQualifiedName();
			artifactsByFqn.put(fqn, artifact);
		}
	}

	private void updateAllKnownArtifactsByFqn(
			ITigerstripeProgressMonitor monitor) {
		allKnownArtifactsByFqn = new HashMap();
		for (Iterator iter = allArtifacts.iterator(); iter.hasNext();) {
			IAbstractArtifact artifact = (IAbstractArtifact) iter.next();
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

	public synchronized List<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn, ITigerstripeProgressMonitor monitor) {
		if (!isInitialized) {
			updateCache(monitor);
		}

		if (allKnownArtifactsByFqn.containsKey(fqn))
			return ArtifactFilter.filter(allKnownArtifactsByFqn.get(fqn),
					artifactFilter);
		else
			return new ArrayList<IAbstractArtifact>();
	}

	public synchronized List<IAbstractArtifact> getArtifactsByModelInChained(
			AbstractArtifact model, ITigerstripeProgressMonitor monitor) {
		if (!isInitialized)
			updateCache(monitor);

		return ArtifactFilter.filter(artifactsByModel.get(model),
				artifactFilter);
	}

	public synchronized List<IAbstractArtifact> getAllChainedArtifacts(
			ITigerstripeProgressMonitor monitor) {
		if (!isInitialized)
			updateCache(monitor);

		return ArtifactFilter.filter(allArtifacts, artifactFilter);
	}

	public synchronized AbstractArtifact getArtifactByFullyQualifiedNameInChained(
			String name, ITigerstripeProgressMonitor monitor) {
		if (!isInitialized)
			updateCache(monitor);

		AbstractArtifact potentialResult = (AbstractArtifact) artifactsByFqn
				.get(name);
		if (potentialResult != null && artifactFilter.select(potentialResult))
			return potentialResult;

		return null;
	}
}
