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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;

public class VisualeditorRelationshipUtils {

	public static Object[] getPossibleRelationshipsForArtifact(
			ITigerstripeProject project, Map map, IAbstractArtifact artifact)
			throws TigerstripeException {
		IArtifactManagerSession artifactMgrSession = project
				.getArtifactManagerSession();
		EList artifacts = map.getArtifacts();
		// put together a list of all of the relationships that can be built
		// from/to the selected artifact to/from all of the other artifacts
		// that are currently in the map; to do this, first put together the
		// sets of relationships that can be built from/to the selected
		// artifact
		Set<IRelationship> artifactAsSource = new HashSet<IRelationship>();
		Set<IRelationship> artifactAsTarget = new HashSet<IRelationship>();
		List<IRelationship> relList = null;
		String artifactFQN = artifact.getFullyQualifiedName();
		relList = artifactMgrSession.getOriginatingRelationshipForFQN(
				artifactFQN, false);
		if (relList != null && relList.size() > 0)
			artifactAsSource.addAll(relList);
		relList = artifactMgrSession.getTerminatingRelationshipForFQN(
				artifactFQN, false);
		if (relList != null && relList.size() > 0)
			artifactAsTarget.addAll(relList);
		// then loop through the artifacts in the diagram
		Set<IRelationship> relSet = null;
		Set<IRelationship> possibleRelationships = new HashSet<IRelationship>();
		HashMap<String, QualifiedNamedElement> nodesInMap = new HashMap<String, QualifiedNamedElement>();
		for (Object art : artifacts) {
			String fqn = ((AbstractArtifact) art).getFullyQualifiedName();
			nodesInMap.put(fqn, (QualifiedNamedElement) art);
			// if relationships can be build from the selected artifact to other
			// artifacts
			if (artifactAsSource != null && artifactAsSource.size() > 0) {
				// get the list of relationships that can be built with this
				// artifact as their target; the intersection of that set with
				// the set where the selected artifact is a source is a set of
				// possible relationships in this diagram with the selected
				// artifact as a source and this artifact as a target
				relList = artifactMgrSession.getTerminatingRelationshipForFQN(
						fqn, false);
				if (relList != null && relList.size() > 0) {
					relSet = new HashSet<IRelationship>();
					relSet.addAll(relList);
					boolean isChanged = relSet.retainAll(artifactAsSource);
					if (relSet != null && relSet.size() > 0)
						possibleRelationships.addAll(relSet);
				}
			}
			// if relationships can be build to the selected artifact from other
			// artifacts
			if (artifactAsTarget != null && artifactAsTarget.size() > 0) {
				// get the list of relationships that can be built with this
				// artifact as their source; the intersection of that set with
				// the set where the selected artifact is a target is a set of
				// possible relationships in this diagram with the selected
				// artifact as a target and this artifact as a source
				relList = artifactMgrSession.getOriginatingRelationshipForFQN(
						fqn, false);
				if (relList != null && relList.size() > 0) {
					relSet = new HashSet<IRelationship>();
					relSet.addAll(relList);
					boolean isChanged = relSet.retainAll(artifactAsTarget);
					if (relSet != null && relSet.size() > 0)
						possibleRelationships.addAll(relSet);
				}
			}
		}
		HashMap<String, QualifiedNamedElement> associationsInMap = new HashMap<String, QualifiedNamedElement>();
		EList associations = map.getAssociations();
		for (Object thisObj : associations) {
			QualifiedNamedElement elem = (QualifiedNamedElement) thisObj;
			associationsInMap.put(elem.getFullyQualifiedName(), elem);
		}
		HashMap<String, QualifiedNamedElement> dependenciesInMap = new HashMap<String, QualifiedNamedElement>();
		EList dependencies = map.getDependencies();
		for (Object thisObj : dependencies) {
			QualifiedNamedElement elem = (QualifiedNamedElement) thisObj;
			dependenciesInMap.put(elem.getFullyQualifiedName(), elem);
		}
		return new Object[] { possibleRelationships, associationsInMap,
				dependenciesInMap, nodesInMap };
	}

	public static Object[] getPossibleRelationshipsForMap(
			ITigerstripeProject project, Map map) throws TigerstripeException {
		IArtifactManagerSession artifactMgrSession = project
				.getArtifactManagerSession();
		EList artifacts = map.getArtifacts();
		// put together a list of all of the relationships that can be built
		// from any of the artifacts in the map to any of the other artifacts
		// in the map; to do this, first put together a list of all of the
		// available associations based on the sources and targets available
		// in the map
		HashMap<String, Set<IRelationship>> srcRelMap = new HashMap<String, Set<IRelationship>>();
		HashMap<String, Set<IRelationship>> dstRelMap = new HashMap<String, Set<IRelationship>>();
		List<IRelationship> relList = null;
		Set<IRelationship> relSet = null;
		HashMap<String, QualifiedNamedElement> nodesInMap = new HashMap<String, QualifiedNamedElement>();
		for (Object art : artifacts) {
			String fqn = ((AbstractArtifact) art).getFullyQualifiedName();
			nodesInMap.put(fqn, (QualifiedNamedElement) art);
			relList = artifactMgrSession.getOriginatingRelationshipForFQN(fqn,
					false);
			if (relList != null && relList.size() > 0) {
				relSet = new HashSet<IRelationship>();
				relSet.addAll(relList);
				srcRelMap.put(fqn, relSet);
			}
			relList = artifactMgrSession.getTerminatingRelationshipForFQN(fqn,
					false);
			if (relList != null && relList.size() > 0) {
				relSet = new HashSet<IRelationship>();
				relSet.addAll(relList);
				dstRelMap.put(fqn, relSet);
			}
		}
		// then loop through the list of sources
		Set<IRelationship> possibleRelationships = new HashSet<IRelationship>();
		for (Object sourceObj : artifacts) {
			String fqn1 = ((AbstractArtifact) sourceObj)
					.getFullyQualifiedName();
			Set<IRelationship> srcRelationshipSet = srcRelMap.get(fqn1);
			if (srcRelationshipSet == null || srcRelationshipSet.size() == 0)
				continue;
			Set<IRelationship> relationshipSet = null;
			// if found a set of relationships for this source, determine which
			// of those have a target that is in the map as well...
			for (Object destObj : artifacts) {
				String fqn2 = ((AbstractArtifact) destObj)
						.getFullyQualifiedName();
				Set<IRelationship> dstRelationshipSet = dstRelMap.get(fqn2);
				if (dstRelationshipSet != null && dstRelationshipSet.size() > 0) {
					relationshipSet = new HashSet<IRelationship>();
					relationshipSet.addAll(dstRelationshipSet);
					boolean isChanged = relationshipSet
							.retainAll(srcRelationshipSet);
				}
				if (relationshipSet != null && relationshipSet.size() > 0)
					possibleRelationships.addAll(relationshipSet);
			}
		}
		HashMap<String, QualifiedNamedElement> associationsInMap = new HashMap<String, QualifiedNamedElement>();
		EList associations = map.getAssociations();
		for (Object thisObj : associations) {
			QualifiedNamedElement elem = (QualifiedNamedElement) thisObj;
			associationsInMap.put(elem.getFullyQualifiedName(), elem);
		}
		HashMap<String, QualifiedNamedElement> dependenciesInMap = new HashMap<String, QualifiedNamedElement>();
		EList dependencies = map.getDependencies();
		for (Object thisObj : dependencies) {
			QualifiedNamedElement elem = (QualifiedNamedElement) thisObj;
			dependenciesInMap.put(elem.getFullyQualifiedName(), elem);
		}
		return new Object[] { possibleRelationships, associationsInMap,
				dependenciesInMap, nodesInMap };
	}

	public static void initializeDependency(Dependency dependency,
			IDependencyArtifact dependencyArtifact,
			HashMap<String, QualifiedNamedElement> nodesInMap) {
		// set the basic parameters for the dependency
		dependency.setName(dependencyArtifact.getName());
		dependency.setPackage(dependencyArtifact.getPackage());
		dependency.setIsAbstract(dependencyArtifact.isAbstract());
		dependency.setIsReadonly(dependencyArtifact.isReadonly());
		IStereotypeInstance[] stereotypes = dependencyArtifact
				.getStereotypeInstances();
		for (IStereotypeInstance stereotype : stereotypes) {
			dependency.getStereotypes().add(stereotype.getName());
		}
		// fill in aEnd parameters
		String aEndFQN = dependencyArtifact.getAEndType()
				.getFullyQualifiedName();
		AbstractArtifact aEndArtifact = (AbstractArtifact) nodesInMap
				.get(aEndFQN);
		dependency.setAEnd(aEndArtifact);
		// and fill in zEnd parameters
		String zEndFQN = dependencyArtifact.getZEndType()
				.getFullyQualifiedName();
		AbstractArtifact zEndArtifact = (AbstractArtifact) nodesInMap
				.get(zEndFQN);
		dependency.setZEnd(zEndArtifact);
	}

	public static void initializeAssociation(Association assoc,
			IAssociationArtifact assocArtifact,
			HashMap<String, QualifiedNamedElement> nodesInMap) {
		// set the basic parameters for the association
		assoc.setName(assocArtifact.getName());
		assoc.setPackage(assocArtifact.getPackage());
		assoc.setIsAbstract(assocArtifact.isAbstract());
		assoc.setIsReadonly(assocArtifact.isReadonly());
		IStereotypeInstance[] stereotypes = assocArtifact
				.getStereotypeInstances();
		for (IStereotypeInstance stereotype : stereotypes) {
			assoc.getStereotypes().add(stereotype.getName());
		}
		// fill in aEnd parameters
		String aEndFQN = assocArtifact.getAEnd().getType()
				.getFullyQualifiedName();
		AbstractArtifact aEndArtifact = (AbstractArtifact) nodesInMap
				.get(aEndFQN);
		assoc.setAEnd(aEndArtifact);
		assoc.setAEndName(assocArtifact.getAEnd().getName());
		assoc.setAEndAggregation(getAggregation(assocArtifact.getAEnd()
				.getAggregation()));
		assoc.setAEndIsNavigable(assocArtifact.getAEnd().isNavigable());
		assoc.setAEndIsChangeable(getIsChangeable(assocArtifact.getAEnd()
				.getChangeable()));
		assoc.setAEndIsOrdered(assocArtifact.getAEnd().isOrdered());
		assoc.setAEndMultiplicity(getRelationshipMuliplicity(assocArtifact
				.getAEnd().getMultiplicity()));
		// and fill in zEnd parameters
		String zEndFQN = assocArtifact.getZEnd().getType()
				.getFullyQualifiedName();
		AbstractArtifact zEndArtifact = (AbstractArtifact) nodesInMap
				.get(zEndFQN);
		assoc.setZEnd(zEndArtifact);
		assoc.setZEndName(assocArtifact.getZEnd().getName());
		assoc.setZEndAggregation(getAggregation(assocArtifact.getZEnd()
				.getAggregation()));
		assoc.setZEndIsNavigable(assocArtifact.getZEnd().isNavigable());
		assoc.setZEndIsChangeable(getIsChangeable(assocArtifact.getZEnd()
				.getChangeable()));
		assoc.setZEndIsOrdered(assocArtifact.getZEnd().isOrdered());
		assoc.setZEndMultiplicity(getRelationshipMuliplicity(assocArtifact
				.getZEnd().getMultiplicity()));
	}

	public static void initializeAssociationClass(AssociationClass assocClass,
			IAssociationClassArtifact assocClassArtifact,
			HashMap<String, QualifiedNamedElement> nodesInMap) {
		ArtifactManager artifactMgr = ((org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact) assocClassArtifact)
				.getArtifactManager();
		// first initialize the "association part" of this association class
		initializeAssociation(assocClass, assocClassArtifact, nodesInMap);
		// then initialize the "association class class" part (the class box
		// that is linked to the "association part" of the association class)
		AssociationClassClass assocClassClass = assocClass.getAssociatedClass();
		// set the basic parameters for the association class class
		assocClassClass.setName(assocClass.getName());
		assocClassClass.setPackage(assocClass.getPackage());
		assocClassClass.setIsAbstract(assocClassArtifact.isAbstract());
		assocClassClass.setIsReadonly(assocClassArtifact.isReadonly());
		IStereotypeInstance[] stereotypes = assocClassArtifact
				.getStereotypeInstances();
		for (IStereotypeInstance stereotype : stereotypes) {
			assocClassClass.getStereotypes().add(stereotype.getName());
		}
		// if this class extends another class, add the extends relationship
		// to that class (if it's in the diagram)
		IAbstractArtifact extendedIArtifact = assocClassArtifact
				.getExtendedArtifact();
		if (extendedIArtifact != null) {
			String fqn = extendedIArtifact.getFullyQualifiedName();
			if (nodesInMap.keySet().contains(fqn)) {
				assocClassClass.setExtends((AbstractArtifact) nodesInMap
						.get(fqn));
			}
		}
		// if this class is extended by another class, add the extension link
		for (QualifiedNamedElement node : nodesInMap.values()) {
			try {
				IAbstractArtifact otherArt = node.getCorrespondingIArtifact();
				if (otherArt != null) {
					// This seems to happen when the diagram contains a
					// artifact that is not in the TS model anymore. This
					// should only happen when the diagram is out of sync
					// (in theory never), but just in case. Otherwise no DnD
					// is possible anymore. The un-wanted artifact is then
					// cleaned up by a simple close-reopen from the user.
					// This leaves them with a usable diagram in the meantime.
					IAbstractArtifact extendedArt = otherArt
							.getExtendedArtifact();
					String fqn = assocClassArtifact.getFullyQualifiedName();
					if (extendedArt != null
							&& extendedArt.getFullyQualifiedName().equals(fqn)) {
						if (node instanceof AbstractArtifact)
							((AbstractArtifact) node)
									.setExtends(assocClassClass);
					}
				}
			} catch (TigerstripeException e) {
			}
		}
		// then create attributes (if needed) and add them to the association
		// class class EObject
		for (IField field : assocClassArtifact.getFields()) {
			String typeName = field.getIType().getFullyQualifiedName();
			if ("java.lang.String".equals(typeName))
				typeName = "String";
			if (!shouldDisplayReference() || field.getIType().isPrimitive()
					|| typeName.equals("String")) {
				// either our profile indicates that we shouldn't display
				// non-primitive types as references or it's a primitive
				// type, so create an attribute to contain it
				Attribute eAttribute = VisualeditorFactory.eINSTANCE
						.createAttribute();
				// set the attribute fields to values that match the
				// corresponding artifact
				eAttribute.setMultiplicity(getMultiplicity(field.getIType()
						.getMultiplicity()));
				eAttribute.setName(field.getName());
				eAttribute.setType(typeName);
				eAttribute.setVisibility(mapVisibility(field.getVisibility()));
				// add the attribute to the EObject
				assocClassClass.getAttributes().add(eAttribute);
			} else {
				// it's a non-primitive type and we should display it as a
				// reference (unless it's a enumeration), this else clause
				// handles those two cases
				IAbstractArtifact artifact = artifactMgr
						.getArtifactByFullyQualifiedName(typeName, false,
								new TigerstripeNullProgressMonitor());
				if (artifact instanceof IEnumArtifact) {
					Attribute eAttribute = VisualeditorFactory.eINSTANCE
							.createAttribute();
					// add the attribute to the EObject...it's critical that
					// this be done before we try to call to setType will fail
					// for enumeration attributes
					assocClassClass.getAttributes().add(eAttribute);
					// set the attribute fields to values that match the
					// corresponding artifact
					eAttribute.setMultiplicity(getMultiplicity(field
							.getIType().getMultiplicity()));
					eAttribute.setName(field.getName());
					eAttribute.setType(typeName);
					eAttribute.setVisibility(mapVisibility(field
							.getVisibility()));
				} else {
					QualifiedNamedElement otherNode = nodesInMap.get(typeName);
					if (otherNode == null)
						continue;
					// found a match, so draw the reference link to it
					Reference ref = VisualeditorFactory.eINSTANCE
							.createReference();
					ref.setZEnd((AbstractArtifact) otherNode);
					ref.setMultiplicity(getMultiplicity(field.getIType()
							.getMultiplicity()));
					ref.setName(field.getName());
					assocClassClass.getReferences().add(ref);
				}
			}
		}
		// and create methods (if needed) and add them to the association
		// class class EObject
		for (IMethod method : assocClassArtifact.getMethods()) {
			Method eMethod = VisualeditorFactory.eINSTANCE.createMethod();
			// set the method fields to values that match the corresponding
			// artifact
			eMethod.setIsAbstract(method.isAbstract());
			eMethod.setMultiplicity(getMultiplicity(method.getReturnIType()
					.getMultiplicity()));
			eMethod.setName(method.getName());
			String typeName = method.getReturnIType()
					.getFullyQualifiedName();
			if ("java.lang.String".equals(typeName))
				typeName = "String";
			eMethod.setType(typeName);
			eMethod.setVisibility(mapVisibility(method.getVisibility()));
			// loop through the arguments to the method (if any) and
			// add the corresponding set of parameters to this method
			// EObject
			IArgument[] arguments = method.getIArguments();
			for (IArgument argument : arguments) {
				Parameter param = VisualeditorFactory.eINSTANCE
						.createParameter();
				// set the parameter fields to values that match the
				// corresponding artifact
				param.setMultiplicity(getMultiplicity(argument.getIType()
						.getMultiplicity()));
				param.setName(argument.getName());
				String paramTypeName = argument.getIType()
						.getFullyQualifiedName();
				if ("java.lang.String".equals(paramTypeName))
					paramTypeName = "String";
				param.setType(paramTypeName);
				// and add the parameter to the method EObject
				eMethod.getParameters().add(param);
			}
			// and add the method to the association class class EObject
			assocClassClass.getMethods().add(eMethod);
		}
	}

	/*
	 * methods that follow are utility methods that are used when mapping more
	 * complex field values from the values returned by the getters in the
	 * artifact to the values expected by the corresponding EObject's setters
	 */
	public static AggregationEnum getAggregation(EAggregationEnum endAggregation) {
		if (endAggregation == EAggregationEnum.COMPOSITE)
			return AggregationEnum.COMPOSITE_LITERAL;
		else if (endAggregation == EAggregationEnum.SHARED)
			return AggregationEnum.SHARED_LITERAL;
		else if (endAggregation == EAggregationEnum.NONE)
			return AggregationEnum.NONE_LITERAL;
		throw new IllegalArgumentException("Illegal value " + endAggregation
				+ " found");
	}

	public static ChangeableEnum getIsChangeable(EChangeableEnum isChangeable) {
		if (isChangeable == EChangeableEnum.ADDONLY)
			return ChangeableEnum.ADD_ONLY_LITERAL;
		else if (isChangeable == EChangeableEnum.FROZEN)
			return ChangeableEnum.FROZEN_LITERAL;
		else if (isChangeable == EChangeableEnum.NONE)
			return ChangeableEnum.NONE_LITERAL;
		throw new IllegalArgumentException("Illegal value " + isChangeable
				+ " found");
	}

	public static AssocMultiplicity getRelationshipMuliplicity(
			IModelComponent.EMultiplicity multiplicity) {
		if (multiplicity == IModelComponent.EMultiplicity.ONE)
			return AssocMultiplicity.ONE_LITERAL;
		else if (multiplicity == IModelComponent.EMultiplicity.ONE_STAR)
			return AssocMultiplicity.ONE_STAR_LITERAL;
		else if (multiplicity == IModelComponent.EMultiplicity.STAR)
			return AssocMultiplicity.STAR_LITERAL;
		else if (multiplicity == IModelComponent.EMultiplicity.ZERO)
			return AssocMultiplicity.ZERO_LITERAL;
		else if (multiplicity == IModelComponent.EMultiplicity.ZERO_ONE)
			return AssocMultiplicity.ZERO_ONE_LITERAL;
		else if (multiplicity == IModelComponent.EMultiplicity.ZERO_STAR)
			return AssocMultiplicity.ZERO_STAR_LITERAL;
		throw new IllegalArgumentException("Illegal value " + multiplicity
				+ " found");
	}

	public static Visibility mapVisibility(int visibility) {
		if (visibility == IModelComponent.VISIBILITY_PUBLIC)
			return Visibility.PUBLIC_LITERAL;
		else if (visibility == IModelComponent.VISIBILITY_PACKAGE)
			return Visibility.PACKAGE_LITERAL;
		else if (visibility == IModelComponent.VISIBILITY_PROTECTED)
			return Visibility.PROTECTED_LITERAL;
		else if (visibility == IModelComponent.VISIBILITY_PRIVATE)
			return Visibility.PRIVATE_LITERAL;
		throw new IllegalArgumentException("Illegal value " + visibility
				+ " found");
	}

	public static TypeMultiplicity getMultiplicity(int multiplicy) {
		if (multiplicy == IType.MULTIPLICITY_SINGLE)
			return TypeMultiplicity.NONE_LITERAL;
		else if (multiplicy == IType.MULTIPLICITY_MULTI)
			return TypeMultiplicity.ARRAY_LITERAL;
		throw new IllegalArgumentException("Illegal value " + multiplicy
				+ " found");
	}

	public static boolean shouldDisplayReference() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getIWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayReference = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
		return displayReference;
	}

}
