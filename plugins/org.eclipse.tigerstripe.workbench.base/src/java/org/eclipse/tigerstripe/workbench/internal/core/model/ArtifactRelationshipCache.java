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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.RelationshipPredicateFilter;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * This holds a cache allowing to access all relationships using their
 * starting/ending ends as a key.
 * 
 * In other words, it allows to efficiently respond to queries such as: "get all
 * Associations originating from artifact identified with FQN such and such..."
 * 
 * There is such a cache in every artifact manager, and it is populated each
 * time a relationship (IAssociationArtifact, IDependencyArtifact) is
 * created/modified within that Artifact Mgr.
 * 
 * This cache is used to implement queries available from the
 * ArtifactMgrSession: -
 * 
 * @see IQueryAllRelationshipsBetween
 * 
 *      and is used to implement accessor on AbstractArtifact: -
 * @see AbstractArtifact#getOriginatingRelationships -
 * @see AbstractArtifact#getTerminatingRelationships
 * 
 *      Bug 928: need to be facet-aware. See
 *      {@link #setActiveFacet(IFacetReference)}
 * 
 * @author Eric Dillon
 * 
 */
public class ArtifactRelationshipCache {

	public final static int ORIGINATING = 0;

	public final static int TERMINATING = 1;

	private final ArtifactManager artifactManager;

	// Bug 928: whenever a facet is active, all returns are filtered with the
	// predicate
	private RelationshipFilter relationshipFilter = new RelationshipNoFilter();

	private final HashMap<String, List<IRelationship>> originatingRelationships = new HashMap<String, List<IRelationship>>();

	private final HashMap<String, List<IRelationship>> terminatingRelationships = new HashMap<String, List<IRelationship>>();

	public ArtifactRelationshipCache(ArtifactManager artifactManager) {
		this.artifactManager = artifactManager;
	}

	// Bug 928
	/* package */void setActiveFacet(IFacetReference facetRef) {
		this.relationshipFilter = new RelationshipPredicateFilter(
				facetRef.getFacetPredicate());
	}

	// Bug 928
	/* package */void resetActiveFacet() {
		this.relationshipFilter = new RelationshipNoFilter();
	}

	/**
	 * Populates the cache with current relationship found in artifact mgr
	 * 
	 */
	public void updateCache(IProgressMonitor monitor) {
		clearCache();
		List<IAbstractArtifact> rels = new ArrayList<IAbstractArtifact>();

		rels.addAll(artifactManager.getArtifactsByModel(
				AssociationArtifact.MODEL, false, monitor));
		rels.addAll(artifactManager.getArtifactsByModel(
				DependencyArtifact.MODEL, false, monitor));

		for (IAbstractArtifact artifact : rels) {
			IRelationship rel = (IRelationship) artifact;
			String aEndFQN = null;
			String zEndFQN = null;

			IRelationshipEnd aEnd = rel.getRelationshipAEnd();
			if (aEnd != null && aEnd.getType() != null) {
				aEndFQN = aEnd.getType().getFullyQualifiedName();
			}

			IRelationshipEnd zEnd = rel.getRelationshipZEnd();
			if (zEnd != null && zEnd.getType() != null) {
				zEndFQN = zEnd.getType().getFullyQualifiedName();
			}

			if (aEndFQN != null) {
				addRelationshipForFQN(aEndFQN, rel,
						ArtifactRelationshipCache.ORIGINATING);
			}

			if (zEndFQN != null) {
				addRelationshipForFQN(zEndFQN, rel,
						ArtifactRelationshipCache.TERMINATING);
			}

		}
	}

	public void clearCache() {
		originatingRelationships.clear();
		terminatingRelationships.clear();
	}

	/**
	 * Returns all IRelationships originating from the given FQN
	 * 
	 * @param fullyQualifiedName
	 * @return
	 * @throws TigerstripeException
	 *             if FQN cannot be found
	 */
	public List<IRelationship> getRelationshipsOriginatingFromFQN(
			String fullyQualifiedName) throws TigerstripeException {
		return RelationshipFilter
				.filter(internalGetRelationshipsForFQN(fullyQualifiedName,
						ORIGINATING), relationshipFilter);
	}

	public List<IRelationship> getRelationshipsOriginatingFromFQN(
			String fullyQualifiedName, boolean ignoreFacets)
			throws TigerstripeException {
		if (ignoreFacets)
			return RelationshipFilter.filter(
					internalGetRelationshipsForFQN(fullyQualifiedName,
							ORIGINATING), RelationshipNoFilter.INSTANCE);
		else
			return getRelationshipsOriginatingFromFQN(fullyQualifiedName);
	}

	/**
	 * Returns all IRelationships originating from the given FQN
	 * 
	 * @param fullyQualifiedName
	 * @return
	 * @throws TigerstripeException
	 *             if FQN cannot be found
	 */
	public List<IRelationship> getRelationshipsTerminatingInFQN(
			String fullyQualifiedName) throws TigerstripeException {
		return RelationshipFilter
				.filter(internalGetRelationshipsForFQN(fullyQualifiedName,
						TERMINATING), relationshipFilter);
	}

	public List<IRelationship> getRelationshipsTerminatingInFQN(
			String fullyQualifiedName, boolean ignoreFacet)
			throws TigerstripeException {
		if (ignoreFacet) {
			return RelationshipFilter.filter(
					internalGetRelationshipsForFQN(fullyQualifiedName,
							TERMINATING), RelationshipNoFilter.INSTANCE);
		} else {
			return getRelationshipsTerminatingInFQN(fullyQualifiedName);
		}
	}

	/**
	 * Internal implementation for get methods
	 * 
	 * @param fullyQualifiedName
	 * @param source
	 *            either ORIGINATING or TERMINATING
	 * @return
	 * @throws TigerstripeException
	 */
	protected List<IRelationship> internalGetRelationshipsForFQN(
			String fullyQualifiedName, int source) throws TigerstripeException {

		List<IRelationship> result = null;

		switch (source) {
		case ORIGINATING:
			result = originatingRelationships.get(fullyQualifiedName);
			break;
		case TERMINATING:
			result = terminatingRelationships.get(fullyQualifiedName);
			break;
		}

		if (result == null)
			return new ArrayList<IRelationship>();

		return result;
	}

	private void addRelationshipEndDelta(List<IModelChangeDelta> deltas,
			IAbstractArtifact artifact, String oldRelationshipFQN,
			IRelationship newRelationship, int type) {
		ModelChangeDelta delta = null;

		if (artifact != null) {
			if (newRelationship == null) {
				delta = new ModelChangeDelta(IModelChangeDelta.REMOVE);
			} else {
				delta = new ModelChangeDelta(IModelChangeDelta.ADD);
			}
			delta.setFeature(IModelChangeDelta.RELATIONSHIP_END);
			delta.setAffectedModelComponentURI((URI) artifact
					.getAdapter(URI.class));
			delta.setOldValue(oldRelationshipFQN);
			delta.setNewValue(newRelationship);
			delta.setSource(this);

			deltas.add(delta);
		}
	}

	private void flushDeltas(List<IModelChangeDelta> deltas) {
		TigerstripeWorkspaceNotifier.INSTANCE.signalModelChange(deltas
				.toArray(new IModelChangeDelta[deltas.size()]));
	}

	/**
	 * Adds a relationship to the cache. If it is replacing a relationship and
	 * we known which one is to be replaced, it should passed as oldRelationship
	 * 
	 * @param relationship
	 * @param oldRelationship
	 */
	public void addRelationship(IRelationship relationship,
			IRelationship oldRelationship) {
		List<IModelChangeDelta> deltas = new ArrayList<IModelChangeDelta>();
		if (oldRelationship != null) {
			// Bug 969: with a DnD of an assoc end on a class diagram, the new
			// endtype is changed in place. So, the cache needs to be updated
			// differently than the case when the oldRel is replaced by a new
			// rel

			if (oldRelationship != relationship) {
				// let's remove all entries about this old one
				
				{
					IRelationshipEnd aEnd = oldRelationship
							.getRelationshipAEnd();
					if (aEnd != null && aEnd.getType() != null) {

						String oldAEnd = aEnd.getType().getFullyQualifiedName();
						removeRelationshipForFQN(oldAEnd, oldRelationship,
								ORIGINATING);
						addRelationshipEndDelta(deltas, aEnd.getType()
								.getArtifact(),
								oldRelationship.getFullyQualifiedName(), null,
								ORIGINATING);
					}
				}
				
				{
					IRelationshipEnd zEnd = oldRelationship
							.getRelationshipZEnd();
					if (zEnd != null && zEnd.getType() != null) {
						String oldZEnd = zEnd.getType().getFullyQualifiedName();

						removeRelationshipForFQN(oldZEnd, oldRelationship,
								TERMINATING);

						addRelationshipEndDelta(deltas, zEnd.getType()
								.getArtifact(),
								oldRelationship.getFullyQualifiedName(), null,
								TERMINATING);
					}
				}
			} else {
				// Bug 969: either end may have been changed in place.
				// We simply remove it from the cache as it would be improperly
				// indexed
				// It will be re-added below with the proper indexes.

				// In order to generate the right "REMOVE" model changes, we
				// need
				// to find where the anchors where from the cache before we
				// change
				// it

				IAbstractArtifact oldAEndArtifact = findEndForRelationshipInCache(
						oldRelationship, ORIGINATING);
				IAbstractArtifact oldZEndArtifact = findEndForRelationshipInCache(
						oldRelationship, TERMINATING);

				removeRelationshipFromCache(oldRelationship);
				addRelationshipEndDelta(deltas, oldAEndArtifact,
						oldRelationship.getFullyQualifiedName(), null,
						ORIGINATING);
				addRelationshipEndDelta(deltas, oldZEndArtifact,
						oldRelationship.getFullyQualifiedName(), null,
						TERMINATING);

			}
		}

		IType aEndType = relationship.getRelationshipAEnd().getType();
		IType zEndType = relationship.getRelationshipZEnd().getType();
		String aEndFQN = null;
		String zEndFQN = null;
		if (aEndType != null) {
			aEndFQN = aEndType.getFullyQualifiedName();
		}

		if (zEndType != null) {
			zEndFQN = zEndType.getFullyQualifiedName();
		}

		if (aEndFQN != null && zEndFQN != null) {
			addRelationshipForFQN(aEndFQN, relationship, ORIGINATING);
			addRelationshipForFQN(zEndFQN, relationship, TERMINATING);
			addRelationshipEndDelta(deltas, relationship.getRelationshipAEnd()
					.getType().getArtifact(), null, relationship, ORIGINATING);
			addRelationshipEndDelta(deltas, relationship.getRelationshipZEnd()
					.getType().getArtifact(), null, relationship, TERMINATING);
		}

		flushDeltas(deltas);
	}

	public void removeRelationship(IRelationship relationship, Set<ITigerstripeModelProject> ignoreProjects) {
		List<IModelChangeDelta> deltas = new ArrayList<IModelChangeDelta>();

		
		if (relationship != null) {
			// let's remove all entries about this old one
			IRelationshipEnd aEnd = relationship.getRelationshipAEnd();
			if (aEnd != null) {
				IType aType = aEnd.getType();
				if (aType != null) {
					ArtifactManager aMgr = aType.getArtifactManager();
					if (aType != null && !aMgr.wasDisposed() && !ignoreProjects.contains(aMgr.getTSProject())) {
						String oldAEnd = aType.getFullyQualifiedName();
						removeRelationshipForFQN(oldAEnd, relationship, ORIGINATING);
						IAbstractArtifact artifact = aType.getArtifact();
						addRelationshipEndDelta(deltas, artifact,
								relationship.getFullyQualifiedName(), null, ORIGINATING);
					}
				}
			}
			IRelationshipEnd zEnd = relationship.getRelationshipZEnd();
			if (zEnd != null) {
				IType zType = zEnd.getType();
				if (zType != null) {
    				ArtifactManager aMgr = zType.getArtifactManager();
    				if (!aMgr.wasDisposed() && !ignoreProjects.contains(aMgr.getTSProject())) {
    					String oldZEnd = zType.getFullyQualifiedName();
    					removeRelationshipForFQN(oldZEnd, relationship, TERMINATING);
    					addRelationshipEndDelta(deltas, zType.getArtifact(),
    							relationship.getFullyQualifiedName(), null, TERMINATING);
    				}
				}
			}
		}
		flushDeltas(deltas);
	}

	/**
	 * Adds a new relationship for the given fqn in the cache
	 * 
	 * @param fqn
	 * @param relationship
	 * @param type
	 *            either ORIGINATING or TERMINATING
	 */
	private void addRelationshipForFQN(String fqn, IRelationship relationship,
			int type) {

		List<IRelationship> targetList = null;

		switch (type) {
		case ORIGINATING:
			targetList = originatingRelationships.get(fqn);
			if (targetList == null) {
				targetList = new ArrayList<IRelationship>();
				originatingRelationships.put(fqn, targetList);
			}
			break;
		case TERMINATING:
			targetList = terminatingRelationships.get(fqn);
			if (targetList == null) {
				targetList = new ArrayList<IRelationship>();
				terminatingRelationships.put(fqn, targetList);
			}
		}

		if (!targetList.contains(relationship)) {
			// TigerstripeRuntime.logInfoMessage(" Adding " + relationship + "
			// to " + targetList);
			targetList.add(relationship);
		}
	}

	private void removeRelationshipForFQN(String fqn,
			IRelationship relationship, int type) {

		List<IRelationship> targetList = null;

		switch (type) {
		case ORIGINATING:
			targetList = originatingRelationships.get(fqn);
			break;
		case TERMINATING:
			targetList = terminatingRelationships.get(fqn);
		}

		if (targetList != null) {
			// Bug 747: Ideally we should have a equals() defined that would
			// compare the FQNs
			// but this seems to involved to change now. Doing it properly here
			// tho.
			for (Iterator<IRelationship> iter = targetList.iterator(); iter
					.hasNext();) {
				IAbstractArtifact rel = (IAbstractArtifact) iter.next();
				if (rel.getFullyQualifiedName().equals(
						((IAbstractArtifact) relationship)
								.getFullyQualifiedName())) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * Removes all occurences of the given object from both originating and
	 * terminating hash maps.
	 * 
	 * NOTE: this method is different from "removeRelationship" in that it
	 * doesn't follow the indexes, it simply walks through all lists. This is
	 * necessary when we know the indexs are out-of-sync, but this is a CPU
	 * intensive method. To use with caution.
	 * 
	 * @param rel
	 */
	private void removeRelationshipFromCache(IRelationship rel) {

		// Go through all originating lists
		for (List<IRelationship> origList : originatingRelationships.values()) {
			for (Iterator<IRelationship> iter = origList.iterator(); iter
					.hasNext();) {
				IRelationship value = iter.next();
				if (value == rel) {
					iter.remove();
				}
			}
		}

		// Go through all terminating lists
		for (List<IRelationship> termList : terminatingRelationships.values()) {
			for (Iterator<IRelationship> iter = termList.iterator(); iter
					.hasNext();) {
				IRelationship value = iter.next();
				if (value == rel)
					iter.remove();
			}
		}
	}

	private IAbstractArtifact findEndForRelationshipInCache(IRelationship rel,
			int type) {

		HashMap<String, List<IRelationship>> endingRelationships = null;

		switch (type) {
		case TERMINATING:
			endingRelationships = terminatingRelationships;
			break;
		case ORIGINATING:
			endingRelationships = originatingRelationships;
			break;
		}
		String foundKey = null;
		for (String key : endingRelationships.keySet()) {
			List<IRelationship> rels = endingRelationships.get(key);
			if (rels != null && rels.contains(rel)) {
				foundKey = key;
			}
		}

		if (foundKey != null) {
			// A bit tricky here to look it up
			if (rel instanceof IAbstractArtifactInternal) {
				IAbstractArtifactInternal art = (IAbstractArtifactInternal) rel;
				return art.getArtifactManager()
						.getArtifactByFullyQualifiedName(foundKey, true,
								new NullProgressMonitor());
			}
		}
		return null;
	}
}
