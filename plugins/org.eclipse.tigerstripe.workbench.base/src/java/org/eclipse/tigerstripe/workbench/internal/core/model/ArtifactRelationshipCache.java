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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.RelationshipPredicateFilter;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

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
 * and is used to implement accessor on AbstractArtifact: -
 * @see AbstractArtifact#getOriginatingRelationships -
 * @see AbstractArtifact#getTerminatingRelationships
 * 
 * Bug 928: need to be facet-aware. See {@link #setActiveFacet(IFacetReference)}
 * 
 * @author Eric Dillon
 * 
 */
public class ArtifactRelationshipCache {

	public final static int ORIGINATING = 0;

	public final static int TERMINATING = 1;

	private ArtifactManager artifactManager;

	// Bug 928: whenever a facet is active, all returns are filtered with the
	// predicate
	private RelationshipFilter relationshipFilter = new RelationshipNoFilter();

	private HashMap<String, List<IRelationship>> originatingRelationships = new HashMap<String, List<IRelationship>>();

	private HashMap<String, List<IRelationship>> terminatingRelationships = new HashMap<String, List<IRelationship>>();

	public ArtifactRelationshipCache(ArtifactManager artifactManager) {
		this.artifactManager = artifactManager;
	}

	// Bug 928
	/* package */void setActiveFacet(IFacetReference facetRef) {
		this.relationshipFilter = new RelationshipPredicateFilter(facetRef
				.getFacetPredicate());
	}

	// Bug 928
	/* package */void resetActiveFacet() {
		this.relationshipFilter = new RelationshipNoFilter();
	}

	/**
	 * Populates the cache with current relationship found in artifact mgr
	 * 
	 */
	public void updateCache(ITigerstripeProgressMonitor monitor) {
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
		return RelationshipFilter.filter(internalGetRelationshipsForFQN(
				fullyQualifiedName, ORIGINATING), relationshipFilter);
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
		return RelationshipFilter.filter(internalGetRelationshipsForFQN(
				fullyQualifiedName, TERMINATING), relationshipFilter);
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

	/**
	 * Adds a relationship to the cache. If it is replacing a relationship and
	 * we known which one is to be replaced, it should passed as oldRelationship
	 * 
	 * @param relationship
	 * @param oldRelationship
	 */
	public void addRelationship(IRelationship relationship,
			IRelationship oldRelationship) {
		if (oldRelationship != null) {
			// Bug 969: with a DnD of an assoc end on a class diagram, the new
			// endtype
			// is changed in place. So, the cache needs to be updated
			// differently
			// than the case when the oldRel is replaced by a new rel
			if (oldRelationship != relationship) {
				// let's remove all entries about this old one
				String oldAEnd = oldRelationship.getRelationshipAEnd()
						.getType().getFullyQualifiedName();
				String oldZEnd = oldRelationship.getRelationshipZEnd()
						.getType().getFullyQualifiedName();

				removeRelationshipForFQN(oldAEnd, oldRelationship, ORIGINATING);
				removeRelationshipForFQN(oldZEnd, oldRelationship, TERMINATING);
			} else {
				// Bug 969: either end may have been changed in place.
				// We simply remove it from the cache as it would be improperly
				// indexed
				// It will be re-added below with the proper indexes.
				removeRelationshipFromCache(oldRelationship);
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
		}
	}

	public void removeRelationship(IRelationship relationship) {
		if (relationship != null) {
			// let's remove all entries about this old one
			if (relationship.getRelationshipAEnd() != null
					&& relationship.getRelationshipAEnd().getType() != null) {
				String oldAEnd = relationship.getRelationshipAEnd().getType()
						.getFullyQualifiedName();
				removeRelationshipForFQN(oldAEnd, relationship, ORIGINATING);
			}
			if (relationship.getRelationshipZEnd() != null
					&& relationship.getRelationshipZEnd().getType() != null) {
				String oldZEnd = relationship.getRelationshipZEnd().getType()
						.getFullyQualifiedName();
				removeRelationshipForFQN(oldZEnd, relationship, TERMINATING);
			}
		}
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
				if (value == rel)
					iter.remove();
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
}
