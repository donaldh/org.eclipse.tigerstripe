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
package org.eclipse.tigerstripe.workbench.internal.contract.predicate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeAnnotationPattern;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.contract.ContractUtils;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;
import org.eclipse.tigerstripe.workbench.internal.core.util.RegExpFQNSetPred;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

/**
 * A predicate that's smart enough to take into account properties of the scope
 * for closure
 * 
 * @author Eric Dillon
 * @since 2.1
 */
public class FacetPredicate implements Predicate, IFacetPredicate {

	// private final static int AEND = 0;
	//
	// private final static int ZEND = 1;
	//
	private IFacetReference facetRef;

	private RegExpFQNSetPred primaryPredicate;

	private RegExpFQNSetPred resolvedPredicate = null;

	private ITigerstripeProject tsProject;

	// This is used to gather inconsistencies that may be found during facet
	// resolution.
	private MultiStatus errors = new MultiStatus(BasePlugin.getPluginId(), 222,
			"Facet Inconsitencies", null);

	public FacetPredicate(IFacetReference facetRef,
			ITigerstripeProject tsProject) {
		this.facetRef = facetRef;
		this.tsProject = tsProject;
	}

	public boolean isConsistent() {
		return getInconsistencies().isOK();
	}

	public IStatus getInconsistencies() {
		return errors;
	}

	protected IFacetReference getFacetRef() {
		return this.facetRef;
	}

	protected boolean isResolved() {
		return resolvedPredicate != null;
	}

	public boolean evaluate(Object obj) {
		if (!isResolved()) {
			try {
				resolve(new TigerstripeNullProgressMonitor());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage("Can't evaluate :" + obj
						+ ": " + e.getMessage(), e);
				return false;
			}
		}
		return resolvedPredicate.evaluate(obj);
	}

	/**
	 * Returns true if the given artifact should be excluded because of the
	 * annotations it carries.
	 * 
	 * @param artifact
	 * @return
	 */
	public boolean isExcludedByAnnotation(IStereotypeCapable capable)
			throws TigerstripeException {

		// Bug 1014
		if (capable == null)
			return false;
		IContractSegment facet = facetRef.resolve();
		Collection<IStereotypeInstance> stereos = capable.getStereotypeInstances();

		for (IStereotypeInstance stereo : stereos) {
			String name = stereo.getName();
			for (ScopeAnnotationPattern pattern : facet.getCombinedScope()
					.getAnnotationPatterns(ISegmentScope.EXCLUDES)) {
				if (name.equals(pattern.annotationName))
					return true;
			}
		}

		return false;
	}

	public void resolve(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		monitor.beginTask("Resolving facet scope",
				ITigerstripeProgressMonitor.UNKNOWN);

//		long start = System.currentTimeMillis();
		errors = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Facet Inconsistencies", null);
		IContractSegment facet = facetRef.resolve();

		// first get the primary predicate strictly based on the scope
		// Since Bug 1016 we need to take into account possible included
		// scopes as well.
		ISegmentScope combinedScope = facet.getCombinedScope();
		primaryPredicate = ContractUtils.getPredicateForScope(combinedScope);

		resolvedPredicate = new RegExpFQNSetPred();

		if (primaryPredicate.isEmptyPredicate()
				&& combinedScope.getAnnotationPatterns(ISegmentScope.EXCLUDES).length == 0)
			return;

		// We do that in 2 passes. First get all the
		// references/associated/dependent
		// artifacts in the primaryPredicate.
		// Then make sure we have all the inherited artifacts for all of
		// that!

		IQueryAllArtifacts query = (IQueryAllArtifacts) tsProject
				.getArtifactManagerSession().makeQuery(
						IQueryAllArtifacts.class.getName());
		query.setIncludeDependencies(true);
		Collection<IAbstractArtifact> artifacts = tsProject
				.getArtifactManagerSession().queryArtifact(query);

		// First get all the base artifacts that have been identified
		// through the scope and their inherited parents
		Set<IAbstractArtifact> baseArtifacts = new HashSet<IAbstractArtifact>();
		monitor.beginTask("Resolving base scope", artifacts.size());
		for (IAbstractArtifact artifact : artifacts) {
			// We only take the "Class Artifacts" not the relationships.
			if (!(artifact instanceof IRelationship)
					&& primaryPredicate.evaluate(artifact)
					&& !isExcludedByAnnotation(artifact)) {
				baseArtifacts.add(artifact);
				// for (IArtifact arti : artifact.getAncestors()) {
				// baseArtifacts.add((IAbstractArtifact) arti);
				// }
			}
			monitor.worked(1);
		}

		// Explore the base artifacts
		Set<IAbstractArtifact> scope = new HashSet<IAbstractArtifact>();
		monitor.beginTask("Walking relationships",
				ITigerstripeProgressMonitor.UNKNOWN);
		for (IAbstractArtifact artifact : baseArtifacts) {
			addRelatedArtifacts(scope, artifact, false, monitor);
		}
		monitor.done();

		for (IAbstractArtifact art : scope) {
			resolvedPredicate.addIsIncludedPattern(ContractUtils
					.mapFromUserPattern(art.getFullyQualifiedName()));
		}

		monitor.done();
		// long end = System.currentTimeMillis();
	}

	/**
	 * Adds recursively all the related artifacts to "artifact" in the given
	 * scope
	 * 
	 * @param scope -
	 *            the scope to add to
	 * @param artifact -
	 *            the artifact to find related artifacts for
	 * @param ignoreParent -
	 *            should the parent of this artifact be ignored for exploration
	 *            or not.
	 */
	protected void addRelatedArtifacts(Set<IAbstractArtifact> scope,
			IAbstractArtifact artifact, boolean ignoreParent,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {

		// First of all ignore all that is excluded
		if (primaryPredicate.isExcluded(artifact)
				|| isExcludedByAnnotation(artifact)) {
			TigerstripeRuntime.logTraceMessage("Excluding "
					+ artifact.getFullyQualifiedName() + " per primary scope.");
			return;
		}

		// stop condition for recursion: if the artifact is already in the scope
		// we stop, or else we add it and explore
		if (scope.contains(artifact))
			return; // already explored
		else {
			scope.add(artifact);
		}

		if (artifact != null)
			monitor.setTaskName(artifact.getFullyQualifiedName());

		// let explore from this artifact on now

		// If this is a Datatype, we add its subtypes and their related
		// artifacts
		if (artifact instanceof IDatatypeArtifact) {
			Collection<IAbstractArtifact> subTypes = artifact.getExtendingArtifacts();
			for (IAbstractArtifact subType : subTypes) {
				if (!scope.contains(subType)) {
					addRelatedArtifacts(scope, subType, true, monitor);
					// note that in this case we don't need to worry about the
					// parent
					// since we're exploring downward.
				}
			}
		}

		// Then handle various cases
		if (artifact instanceof IAssociationArtifact) {
			// If this is an association
			// look at the ends
			IAssociationArtifact assoc = (IAssociationArtifact) artifact;
			addRelatedArtifacts(scope, (IAbstractArtifact) assoc.getAEnd()
					.getType().getIArtifact(), false, monitor);
			addRelatedArtifacts(scope, (IAbstractArtifact) assoc.getZEnd()
					.getType().getIArtifact(), false, monitor);

			if (!(artifact instanceof IAssociationClassArtifact))
				return;
		} else if (artifact instanceof IDependencyArtifact) {
			//
			System.out.println("doing nothing with dependency: "
					+ artifact.getFullyQualifiedName());
		}

		// Take care of the parent first
		if (!ignoreParent && artifact.getExtendedArtifact() != null) {
			IAbstractArtifact parent = artifact.getExtendedArtifact();
			if (primaryPredicate.isExcluded(parent)
					|| isExcludedByAnnotation(parent)) {
				IStatus error = new Status(IStatus.ERROR, BasePlugin
						.getPluginId(),
						"Inconsistent type hierarchy: Parent of "
								+ artifact.getFullyQualifiedName() + " ("
								+ parent.getFullyQualifiedName()
								+ ") is explicitly excluded from facet.");
				errors.add(error);
			}
			addRelatedArtifacts(scope, parent, false, monitor);
		}

		// Explore all valid referenced artis
		Collection<IAbstractArtifact> referencedArtis = getReferencedArtifact(artifact);

		for (IAbstractArtifact arti : referencedArtis) {
			if (arti instanceof IEnumArtifact) {
				scope.add(arti);
				scope.addAll(getAncestors(arti, false));
			} else {
				if (primaryPredicate.isExcluded(arti)
						|| isExcludedByAnnotation(arti)) {
					IStatus error = new Status(IStatus.ERROR, BasePlugin
							.getPluginId(),
							"Inconsistent artifact: referenced artifact in "
									+ artifact.getFullyQualifiedName() + " ("
									+ arti.getFullyQualifiedName()
									+ ") is explicitly excluded from facet.");
					errors.add(error);
				}
				addRelatedArtifacts(scope, arti, false, monitor);
			}
		}

		// Add all associations and explore them
		Collection<IRelationship> assocs = getAssociations(artifact, false);
		for (IRelationship rel : assocs) {
			IAbstractArtifact arti = (IAbstractArtifact) rel;
			if (!arti.isAbstract() && !primaryPredicate.isExcluded(arti))
				addRelatedArtifacts(scope, arti, false, monitor);
		}

		// Finally the OSSJ specific stuff
		IArtifactManagerSession session = tsProject.getArtifactManagerSession();
		Collection<IAbstractArtifact> ossjRelatedArtifacts = getFirstOrderOssjAssociatedArtifacts(
				session, artifact);
		for (IAbstractArtifact arti : ossjRelatedArtifacts) {
			addRelatedArtifacts(scope, arti, false, monitor);
		}
	}

	public void addTempExclude(String tmpPattern) {
		if (!isResolved()) {
			try {
				resolve(new TigerstripeNullProgressMonitor());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(e.getMessage(), e);
			}
		}
		resolvedPredicate.addIsExcludedPattern(tmpPattern);
	}

	/**
	 * Returns a collection of referenced artifacts thru attributes, methods.
	 * This method takes into account the annotationPattern exclusion
	 * 
	 * @param artifact
	 * @return
	 */
	private Collection<IAbstractArtifact> getReferencedArtifact(
			IAbstractArtifact artifact) throws TigerstripeException {

		Set<IAbstractArtifact> result = new HashSet<IAbstractArtifact>();

		for (IField field : artifact.getFields()) {
			if (!isExcludedByAnnotation(field)) {
				IType type = field.getType();
				IAbstractArtifact arti = (IAbstractArtifact) type
						.getIArtifact();
				if (!type.isPrimitive() && arti != null
						&& !(arti instanceof IPrimitiveTypeArtifact)
						&& !isExcludedByAnnotation(arti)
						&& !primaryPredicate.isExcluded(arti)) {
					result.add(arti);
					result.addAll(getAncestors(arti, false));
				}
			}
		}

		for (IMethod method : artifact.getMethods()) {
			if (!isExcludedByAnnotation(method)) {
				// first the return type
				IType returnType = method.getReturnType();
				IAbstractArtifact arti = (IAbstractArtifact) returnType
						.getIArtifact();
				if (!method.isVoid() && !returnType.isPrimitive()
						&& arti != null
						&& !(arti instanceof IPrimitiveTypeArtifact)) {
					if ((isExcludedByAnnotation(arti) || primaryPredicate
							.isExcluded(arti))
							&& !isExcludedByAnnotation(method)) {
						IStatus error = new Status(IStatus.ERROR, BasePlugin
								.getPluginId(),
								"Inconsistent facet: the return type ("
										+ arti.getFullyQualifiedName()
										+ ") of method "
										+ artifact.getFullyQualifiedName()
										+ ":" + method.getName() + "(...)"
										+ " is explicitly excluded from facet.");
						errors.add(error);
					}
					if (!isExcludedByAnnotation(arti)
							&& !primaryPredicate.isExcluded(arti)
							&& !isExcludedByAnnotation(method)) {
						result.add(arti);
						result.addAll(getAncestors(arti, false));
					}
				}

				// then the arguments
				for (IArgument arg : method.getArguments()) {
					IType type = arg.getType();
					IAbstractArtifact argArti = (IAbstractArtifact) type
							.getIArtifact();
					if (!type.isPrimitive() && argArti != null
							&& !(argArti instanceof IPrimitiveTypeArtifact)) {

						// Check for consistency of the facet
						if ((isExcludedByAnnotation(argArti) || primaryPredicate
								.isExcluded(argArti))
								&& !isExcludedByAnnotation(arg)) {
							IStatus error = new Status(
									IStatus.ERROR,
									BasePlugin.getPluginId(),
									"Inconsistent facet: the type ("
											+ argArti.getFullyQualifiedName()
											+ ") of argument '"
											+ arg.getName()
											+ "' in method "
											+ artifact.getFullyQualifiedName()
											+ ":"
											+ method.getName()
											+ "(...)"
											+ " is explicitly excluded from facet.");
							errors.add(error);
						}
						if (!isExcludedByAnnotation(argArti)
								&& !primaryPredicate.isExcluded(argArti)
								&& !isExcludedByAnnotation(arg)) {
							result.add(argArti);
							result.addAll(getAncestors(argArti, false));
						}
					}
				}

				// finally the exceptions
				for (IException exc : method.getExceptions()) {
					IAbstractArtifact excArti = tsProject
							.getArtifactManagerSession()
							.getArtifactByFullyQualifiedName(
									exc.getFullyQualifiedName());
					if (isExcludedByAnnotation(excArti)
							|| primaryPredicate.isExcluded(excArti)) {
						IStatus error = new Status(
								IStatus.ERROR,
								BasePlugin.getPluginId(),
								"Inconsistent facet: the exception of method "
										+ artifact.getFullyQualifiedName()
										+ ":"
										+ method.getName()
										+ "(...)"
										+ " ("
										+ arti.getFullyQualifiedName()
										+ ") is explicitly excluded from facet.");
						errors.add(error);
					}
					if (!isExcludedByAnnotation(excArti)
							&& !primaryPredicate.isExcluded(excArti)) {
						result.add(excArti);
						result.addAll(getAncestors(excArti, false));
					}
				}
			}
		}

		return result;
	}

	private Collection<IAbstractArtifact> getAncestors(
			IAbstractArtifact artifact, boolean excludeAbstract) {
		Collection<IAbstractArtifact> ancestors = artifact.getAncestors();
		Set<IAbstractArtifact> result = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact arti : ancestors) {
			if (!arti.isAbstract())
				result.add((IAbstractArtifact) arti);
			else if (!excludeAbstract) {
				result.add((IAbstractArtifact) arti);
			} else {
				break;
			}
		}

		return result;
	}

	/**
	 * Returns all OSSJ associated artifacts at the first order.
	 * 
	 * @param session
	 * @param artifact
	 * @return
	 * @throws TigerstripeException
	 */
	private Collection<IAbstractArtifact> getFirstOrderOssjAssociatedArtifacts(
			IArtifactManagerSession session, IAbstractArtifact artifact)
			throws TigerstripeException {

		Set<IAbstractArtifact> result = new HashSet<IAbstractArtifact>();

		if (isExcludedByAnnotation(artifact)) {
			TigerstripeRuntime.logTraceMessage("Excluding "
					+ artifact.getFullyQualifiedName() + " by annotation.");
			return result;
		}

		if (artifact instanceof ISessionArtifact) {
			ISessionArtifact sessionArt = (ISessionArtifact) artifact;

			// Emitted events
			for (IEmittedEvent emittedEvent : sessionArt.getEmittedEvents()) {
				String fqn = emittedEvent.getFullyQualifiedName();
				IAbstractArtifact arti = session
						.getArtifactByFullyQualifiedName(fqn);
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByAnnotation(arti)) {
					result.add(arti);
					for (IAbstractArtifact a : arti.getAncestors()) {
						result.add((IAbstractArtifact) a);
					}
				}
			}

			// Named queries
			for (INamedQuery namedQuery : sessionArt.getNamedQueries()) {
				String fqn = namedQuery.getFullyQualifiedName();
				IAbstractArtifact arti = session
						.getArtifactByFullyQualifiedName(fqn);
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByAnnotation(arti)) {
					result.add(arti);
					for (IAbstractArtifact a : arti.getAncestors()) {
						result.add((IAbstractArtifact) a);
					}
				}
			}

			// Exposed update procs
			for (IExposedUpdateProcedure updateProc : sessionArt
					.getExposedUpdateProcedures()) {
				String fqn = updateProc.getFullyQualifiedName();
				IAbstractArtifact arti = session
						.getArtifactByFullyQualifiedName(fqn);
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByAnnotation(arti)) {
					result.add(arti);
					for (IAbstractArtifact a : arti.getAncestors()) {
						result.add((IAbstractArtifact) a);
					}
				}
			}

			// Managed Entities
			for (IManagedEntityDetails managedEntity : sessionArt
					.getManagedEntityDetails()) {
				String fqn = managedEntity.getFullyQualifiedName();
				IAbstractArtifact arti = session
						.getArtifactByFullyQualifiedName(fqn);
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByAnnotation(arti)) {
					result.add(arti);
					for (IAbstractArtifact a : arti.getAncestors()) {
						result.add((IAbstractArtifact) a);
					}
				}
			}

		} else if (artifact instanceof IQueryArtifact) {
			IQueryArtifact query = (IQueryArtifact) artifact;
			result.add((IAbstractArtifact) query.getReturnedType()
					.getIArtifact());
		}

		return result;
	}

	private Collection<IRelationship> getAssociations(
			IAbstractArtifact artifact, boolean ignoreNavigability)
			throws TigerstripeException {

		IArtifactManagerSession session = tsProject.getArtifactManagerSession();

		Set<IRelationship> nextRelSet = new HashSet<IRelationship>();
		if (!(artifact instanceof IRelationship)) {

			if (ignoreNavigability) {
				// in this case just add everything, we don't care
				nextRelSet.addAll(session.getOriginatingRelationshipForFQN(
						artifact.getFullyQualifiedName(), true));
				nextRelSet.addAll(session.getTerminatingRelationshipForFQN(
						artifact.getFullyQualifiedName(), true));
			} else {
				// need to go thru the results 1 by 1
				Collection<IRelationship> originating = session
						.getOriginatingRelationshipForFQN(artifact
								.getFullyQualifiedName(), true);
				for (IRelationship rel : originating) {
					if (rel instanceof IAssociationArtifact) {
						IAssociationArtifact assoc = (IAssociationArtifact) rel;
						if (assoc.getZEnd().isNavigable()) {
							nextRelSet.add(rel);
						}
					}
				}

				Collection<IRelationship> terminating = session
						.getTerminatingRelationshipForFQN(artifact
								.getFullyQualifiedName(), true);
				for (IRelationship rel : terminating) {
					if (rel instanceof IAssociationArtifact) {
						IAssociationArtifact assoc = (IAssociationArtifact) rel;
						if (assoc.getAEnd().isNavigable()) {
							nextRelSet.add(rel);
						}
					}
				}

			}
		}

		return nextRelSet;
	}
}
