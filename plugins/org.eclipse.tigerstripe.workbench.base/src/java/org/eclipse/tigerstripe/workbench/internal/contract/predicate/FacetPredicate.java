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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeAnnotationPattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeStereotypePattern;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.contract.ContractUtils;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;
import org.eclipse.tigerstripe.workbench.internal.core.util.RegExpFQNSetPred;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

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
	private final IFacetReference facetRef;

	private RegExpFQNSetPred primaryPredicate;

	private RegExpFQNSetPred resolvedPredicate = null;

	private final ITigerstripeModelProject tsProject;

	// This is used to gather inconsistencies that may be found during facet
	// resolution.
	private MultiStatus errors = new MultiStatus(BasePlugin.getPluginId(), 222,
			"Facet Inconsitencies", null);

	public FacetPredicate(IFacetReference facetRef,
			ITigerstripeModelProject tsProject) {
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
				resolve(new NullProgressMonitor());
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
	public boolean isExcludedByStereotype(IStereotypeCapable capable)
			throws TigerstripeException {

		// Bug 1014
		if (capable == null)
			return false;
		IContractSegment facet = facetRef.resolve();
		Collection<IStereotypeInstance> stereos = capable
				.getStereotypeInstances();

		for (IStereotypeInstance stereo : stereos) {
			String name = stereo.getName();
			for (ScopeStereotypePattern pattern : facet.getCombinedScope()
					.getStereotypePatterns(ISegmentScope.EXCLUDES)) {
				if (name.equals(pattern.stereotypeName))
					return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the given artifact should be excluded because of the
	 * annotations it carries.
	 * 
	 * @param capable
	 * @return
	 */
	public boolean isExcludedByAnnotation(IAnnotationCapable capable)
			throws TigerstripeException {
		
		if (capable == null) {
			return false;
		}
		
		IContractSegment facet = facetRef.resolve();

		IAnnotationManager manager = AnnotationPlugin.getManager();
		Annotation[] annotations = manager.doGetAnnotations(capable, true);
		if (annotations == null || annotations.length == 0) {
			return false;
		}

		HashSet<String> annotationSet = new HashSet<String>(annotations.length);
		for (int i = 0; i < annotations.length; i++) {
			AnnotationType type = manager.getType(annotations[i]);
			annotationSet.add(type.getId());
		}

		for (ScopeAnnotationPattern pattern : facet.getCombinedScope()
				.getAnnotationPatterns(ISegmentScope.EXCLUDES)) {
			if (annotationSet.contains(pattern.annotationID))
				return true;
		}

		return false;
	}

	public void resolve(IProgressMonitor monitor) throws TigerstripeException {
		monitor.beginTask("Resolving facet scope", IProgressMonitor.UNKNOWN);

		// long start = System.currentTimeMillis();
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
				&& combinedScope.getStereotypePatterns(ISegmentScope.EXCLUDES).length == 0)
			return;

		// We do that in 2 passes. First get all the
		// references/associated/dependent
		// artifacts in the primaryPredicate.
		// Then make sure we have all the inherited artifacts for all of
		// that!

		// Here we need to bypass the IArtifactManagerSession to ensure we are
		// not going to hit an active facet
		Collection<IAbstractArtifact> artifacts = ((ArtifactManagerSessionImpl) tsProject
				.getArtifactManagerSession()).getArtifactManager()
				.getAllArtifacts(true, true, monitor);

		// First get all the base artifacts that have been identified
		// through the scope and their inherited parents
		Set<IAbstractArtifact> baseArtifacts = new HashSet<IAbstractArtifact>();
		Set<IDependencyArtifact> dependencies = new HashSet<IDependencyArtifact>();
		monitor.beginTask("Resolving base scope", artifacts.size());
		for (IAbstractArtifact artifact : artifacts) {
			// We only take the "Class Artifacts" not the relationships.
			if (!(artifact instanceof IRelationship && !(artifact instanceof IDependencyArtifact))
					&& primaryPredicate.evaluate(artifact)
					&& !isExcludedByStereotype(artifact)) {
				if (artifact instanceof IDependencyArtifact) {
					dependencies.add((IDependencyArtifact) artifact);
				} else {
					baseArtifacts.add(artifact);
				}
			}
			monitor.worked(1);
		}

		// Explore the base artifacts
		Set<IAbstractArtifact> scope = new HashSet<IAbstractArtifact>();
		monitor.beginTask("Walking relationships", IProgressMonitor.UNKNOWN);
		for (IAbstractArtifact artifact : baseArtifacts) {
			// System.out.println("================================");
			addRelatedArtifacts(scope, artifact, false, monitor);
		}
		monitor.done();

		scope.addAll(dependencies);

		if (scope.isEmpty()) {
			// If there's no artifacts in the scope we should add exclude rule
			resolvedPredicate.addIsExcludedPattern(".*");
		} else {
			for (IAbstractArtifact art : scope) {
				resolvedPredicate.addIsIncludedPattern(ContractUtils
						.mapFromUserPattern(art.getFullyQualifiedName()));
			}
		}

		monitor.done();
		// long end = System.currentTimeMillis();
	}

	/**
	 * Adds recursively all the related artifacts to "artifact" in the given
	 * scope
	 * 
	 * @param scope
	 *            - the scope to add to
	 * @param artifact
	 *            - the artifact to find related artifacts for
	 * @param ignoreParent
	 *            - should the parent of this artifact be ignored for
	 *            exploration or not.
	 */
	protected void addRelatedArtifacts(Set<IAbstractArtifact> scope,
			IAbstractArtifact artifact, boolean ignoreParent,
			IProgressMonitor monitor) throws TigerstripeException {

		if (artifact != null) {
			monitor.setTaskName(artifact.getFullyQualifiedName());
		} else {
			TigerstripeRuntime
					.logTraceMessage("Stopped addRelated Artifacts due to null artifact");
			return;
		}

		// First of all ignore all that is excluded
		if (primaryPredicate.isExcluded(artifact)
				|| isExcludedByStereotype(artifact)) {
			TigerstripeRuntime.logTraceMessage("Excluding "
					+ artifact.getFullyQualifiedName() + " per primary scope.");
			return;
		}

		// Before adding this association, we need to check if the ends "exist"
		// / are in scope
		if (artifact instanceof IAssociationArtifact) {
			// If this is an association
			// look at the ends
			IAssociationArtifact assoc = (IAssociationArtifact) artifact;

			addRelatedArtifacts(scope, (IAbstractArtifact) assoc.getAEnd()
					.getType().getArtifact(), false, monitor);

			addRelatedArtifacts(scope, (IAbstractArtifact) assoc.getZEnd()
					.getType().getArtifact(), false, monitor);
			if (scope.contains(assoc.getAEnd().getType().getArtifact())
					&& scope.contains(assoc.getZEnd().getType().getArtifact())) {
				// The ends are both ok..
				// os carry on
			} else {
				IStatus warn = new Status(IStatus.WARNING, BasePlugin
						.getPluginId(), "Excluding "
						+ artifact.getFullyQualifiedName()
						+ " because one or more ends are out of scope.");
				errors.add(warn);
				return;
			}
		}

		// stop condition for recursion: if the artifact is already in the scope
		// we stop, or else we add it and explore
		if (scope.contains(artifact)) {
			// System.out.println("Already in scope "+artifact.getName());
			return; // already explored

		} else {
			// System.out.println("Adding to scope "+artifact.getName());
			scope.add(artifact);
		}

		// Look upwards through the package list -

		IModelComponent container = artifact.getContainingModelComponent();
		if (container instanceof IAbstractArtifact) {
			addRelatedArtifacts(scope, (IAbstractArtifact) container, true,
					monitor);
		}

		// let explore from this artifact on now
		if (artifact instanceof IDependencyArtifact) {
			//
			TigerstripeRuntime
					.logTraceMessage("Doing nothing with dependency: "
							+ artifact.getFullyQualifiedName());
		}

		// If this is a Datatype, we add its subtypes and their related
		// artifacts
		if (artifact instanceof IDatatypeArtifact) {
			Collection<IAbstractArtifact> subTypes = artifact
					.getExtendingArtifacts();
			for (IAbstractArtifact subType : subTypes) {
				if (!scope.contains(subType)) {
					addRelatedArtifacts(scope, subType, true, monitor);

					// note that in this case we don't need to worry about the
					// parent
					// since we're exploring downward.
				}
			}
		}

		if (artifact instanceof IAssociationArtifact
				&& !(artifact instanceof IAssociationClassArtifact)) {
			// don't look at parents of associations
			return;
		}

		// Take care of the parent first
		if (!ignoreParent && artifact.getExtendedArtifact() != null) {
			IAbstractArtifact parent = artifact.getExtendedArtifact();
			if (primaryPredicate.isExcluded(parent)
					|| isExcludedByStereotype(parent)) {
				IStatus error = new Status(IStatus.WARNING, BasePlugin
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
						|| isExcludedByStereotype(arti)) {
					IStatus error = new Status(IStatus.WARNING, BasePlugin
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
			if (arti instanceof IAssociationClassArtifact
					&& !primaryPredicate.isExcluded(arti)) {
				addRelatedArtifacts(scope, arti, false, monitor);
			} else if (!arti.isAbstract() && !primaryPredicate.isExcluded(arti))
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
				resolve(new NullProgressMonitor());
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
			if (!isExcludedByStereotype(field) && !isExcludedByAnnotation(field)) {
				IType type = field.getType();
				IAbstractArtifact arti = (IAbstractArtifact) type.getArtifact();
				if (!type.isPrimitive() && arti != null
						&& !(arti instanceof IPrimitiveTypeArtifact)
						&& !isExcludedByStereotype(arti)
						&& !primaryPredicate.isExcluded(arti)) {
					result.add(arti);
					result.addAll(getAncestors(arti, false));
				}
			}
		}

		for (IMethod method : artifact.getMethods()) {
			if (!isExcludedByStereotype(method) && !isExcludedByAnnotation(method)) {
				// first the return type
				IType returnType = method.getReturnType();
				IAbstractArtifact arti = (IAbstractArtifact) returnType
						.getArtifact();
				if (!method.isVoid() && !returnType.isPrimitive()
						&& arti != null
						&& !(arti instanceof IPrimitiveTypeArtifact)) {
					if ((isExcludedByStereotype(arti) || primaryPredicate
							.isExcluded(arti))
							&& !isExcludedByStereotype(method)) {
						IStatus error = new Status(IStatus.WARNING, BasePlugin
								.getPluginId(),
								"Inconsistent facet: the return type ("
										+ arti.getFullyQualifiedName()
										+ ") of method "
										+ artifact.getFullyQualifiedName()
										+ ":" + method.getName() + "(...)"
										+ " is explicitly excluded from facet.");
						errors.add(error);
					}
					if (!isExcludedByStereotype(arti)
							&& !primaryPredicate.isExcluded(arti)
							&& !isExcludedByStereotype(method)) {
						result.add(arti);
						result.addAll(getAncestors(arti, false));
					}
				}

				// then the arguments
				for (IArgument arg : method.getArguments()) {
					IType type = arg.getType();
					IAbstractArtifact argArti = (IAbstractArtifact) type
							.getArtifact();
					if (!type.isPrimitive() && argArti != null
							&& !(argArti instanceof IPrimitiveTypeArtifact)) {

						// Check for consistency of the facet
						if ((isExcludedByStereotype(argArti) || primaryPredicate
								.isExcluded(argArti))
								&& !isExcludedByStereotype(arg)) {
							IStatus error = new Status(
									IStatus.WARNING,
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
						if (!isExcludedByStereotype(argArti)
								&& !primaryPredicate.isExcluded(argArti)
								&& !isExcludedByStereotype(arg)) {
							result.add(argArti);
							result.addAll(getAncestors(argArti, false));
						}
					}
				}

				// finally the exceptions
				for (IException exc : method.getExceptions()) {
					IAbstractArtifact excArti = ((ArtifactManagerSessionImpl) tsProject
							.getArtifactManagerSession()).getArtifactManager()
							.getArtifactByFullyQualifiedName(
									exc.getFullyQualifiedName(), true, true,
									new NullProgressMonitor());
					if (isExcludedByStereotype(excArti)
							|| primaryPredicate.isExcluded(excArti)) {
						String returnTypeName = arti == null? "void" : arti.getFullyQualifiedName();
						IStatus error = new Status(IStatus.WARNING, BasePlugin
								.getPluginId(), "Inconsistent facet: the "
								+ ArtifactMetadataFactory.INSTANCE.getMetadata(
										IExceptionArtifactImpl.class.getName())
										.getLabel(excArti) + " of method "
								+ artifact.getFullyQualifiedName() + ":"
								+ method.getName() + "(...)" + " (" + returnTypeName
								+ ") is explicitly excluded from facet.");
						errors.add(error);
					}
					if (!isExcludedByStereotype(excArti)
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

		ArtifactManager mgr = ((ArtifactManagerSessionImpl) session)
				.getArtifactManager();

		if (isExcludedByStereotype(artifact)) {
			TigerstripeRuntime.logTraceMessage("Excluding "
					+ artifact.getFullyQualifiedName() + " by annotation.");
			return result;
		}

		if (artifact instanceof ISessionArtifact) {
			ISessionArtifact sessionArt = (ISessionArtifact) artifact;

			// Emitted events
			for (IEmittedEvent emittedEvent : sessionArt.getEmittedEvents()) {
				String fqn = emittedEvent.getFullyQualifiedName();
				IAbstractArtifact arti = mgr.getArtifactByFullyQualifiedName(
						fqn, true, true, new NullProgressMonitor());
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByStereotype(arti)) {
					result.add(arti);
					for (IAbstractArtifact a : arti.getAncestors()) {
						result.add((IAbstractArtifact) a);
					}
				}
			}

			// Named queries
			for (INamedQuery namedQuery : sessionArt.getNamedQueries()) {
				String fqn = namedQuery.getFullyQualifiedName();
				IAbstractArtifact arti = mgr.getArtifactByFullyQualifiedName(
						fqn, true, true, new NullProgressMonitor());
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByStereotype(arti)) {
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
				IAbstractArtifact arti = mgr.getArtifactByFullyQualifiedName(
						fqn, true, true, new NullProgressMonitor());
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByStereotype(arti)) {
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
				IAbstractArtifact arti = mgr.getArtifactByFullyQualifiedName(
						fqn, true, true, new NullProgressMonitor());
				if (arti != null && !primaryPredicate.isExcluded(arti)
						&& !isExcludedByStereotype(arti)) {
					result.add(arti);
					for (IAbstractArtifact a : arti.getAncestors()) {
						result.add((IAbstractArtifact) a);
					}
				}
			}

		} else if (artifact instanceof IQueryArtifact) {
			IQueryArtifact query = (IQueryArtifact) artifact;
			if (query.getReturnedType() != null){
				if (query.getReturnedType()
						.getArtifact() != null){
					result.add((IAbstractArtifact) query.getReturnedType()
							.getArtifact());
				}
			}
		}

		return result;
	}

	private Collection<IRelationship> getAssociations(
			IAbstractArtifact artifact, boolean ignoreNavigability)
			throws TigerstripeException {

		IArtifactManagerSession session = tsProject.getArtifactManagerSession();

		ArtifactManager mgr = ((ArtifactManagerSessionImpl) session)
				.getArtifactManager();

		Set<IRelationship> nextRelSet = new HashSet<IRelationship>();
		if (!(artifact instanceof IRelationship)
				|| artifact instanceof IAssociationClassArtifact) {

			if (ignoreNavigability) {
				// in this case just add everything, we don't care
				nextRelSet.addAll(mgr.getOriginatingRelationshipForFQN(artifact
						.getFullyQualifiedName(), true, true));
				nextRelSet.addAll(mgr.getTerminatingRelationshipForFQN(artifact
						.getFullyQualifiedName(), true, true));
			} else {
				// need to go thru the results 1 by 1
				Collection<IRelationship> originating = mgr
						.getOriginatingRelationshipForFQN(artifact
								.getFullyQualifiedName(), true, true);
				for (IRelationship rel : originating) {
					if (rel instanceof IAssociationArtifact) {
						IAssociationArtifact assoc = (IAssociationArtifact) rel;
						if (assoc.getZEnd().isNavigable()) {
							nextRelSet.add(rel);
						}
					}
				}

				Collection<IRelationship> terminating = mgr
						.getTerminatingRelationshipForFQN(artifact
								.getFullyQualifiedName(), true, true);
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
