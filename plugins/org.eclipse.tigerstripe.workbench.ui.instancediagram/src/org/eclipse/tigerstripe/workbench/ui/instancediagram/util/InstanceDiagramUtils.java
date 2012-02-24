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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.ModelUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum;

public class InstanceDiagramUtils {

	public final static int RELATIONSHIP_SINGLE = 0;
	public final static int RELATIONSHIP_MULTIPLE = 1;

	/**
	 * This method is used to determine if a call to "eSet" was made from an EMF
	 * property sheet or not. If the set is from an EMF property sheet but that
	 * sheet is tied to a class diagram, will return false here. This method
	 * only returns true if the set was made in an instance diagram EMF property
	 * sheet.
	 * 
	 * @return
	 */

	public static boolean isInstanceEMFsetCommand() {
		Exception e = new Exception();
		StackTraceElement[] stackTrace = e.getStackTrace();
		int EMFidx = 0;
		boolean isEMFsetCommand = false;
		// first, loop forward through the stack trace and find an element
		// who's classname starts with the string "EMF" and who's method
		// name is "setPropertyValue"; if you find this element, then this
		// is an EMF "set command", but need to know where it came from
		// to determine if you should allow the "set" command or not
		for (; EMFidx < stackTrace.length; EMFidx++) {
			StackTraceElement stackTraceElement = stackTrace[EMFidx];
			String className = stackTraceElement.getClassName();
			int lastDotPos = className.lastIndexOf(".");
			if (lastDotPos >= 0
					&& lastDotPos < className.length() - 2
					&& className.substring(lastDotPos + 1).startsWith("EMF")
					&& stackTraceElement.getMethodName().equals(
							"setPropertyValue"))
				isEMFsetCommand = true;
		}
		if (isEMFsetCommand) {
			// now that we know it was an EMF "set command", step back up the
			// stack
			// until we find the first "eSet" from the com.tigerstripe space...
			for (int i = (EMFidx - 1); i >= 0; i--) {
				StackTraceElement stackTraceElement = stackTrace[i];
				String className = stackTraceElement.getClassName();
				if (className
						.startsWith("org.eclipse.tigerstripe.workbench.ui.visualeditor"))
					// the set was made through a visual editor (class diagram)
					// property sheet,
					// so allow it(i.e. pretend it isn't an EMF "set command")
					return false;
				else if (className
						.startsWith("org.eclipse.tigerstripe.workbench.ui.instancediagram"))
					// the set was made through the instance diagram property
					// sheet, to return
					// "true" (indicating that the set should be ignored)
					return true;
			}
		}
		return false;
	}

	public static IField[] getInstanceVariables(IAbstractArtifact artifact) {
		IAbstractArtifact lclArtifact = artifact;
		// using a List here to preserve order of entries
		List<IField> iFieldSet = new ArrayList<IField>();
		Stack<List> fieldStack = new Stack<List>();
		List<String> fieldNames = new ArrayList<String>();
		do {
			List<IField> lclFieldSet = new ArrayList<IField>();
			for (IField field : lclArtifact.getFields()) {
				if (fieldNames.contains(field.getLabelString()))
					continue;
				// int visibility = field.getVisibility();
				// if the field is not visible from this artifact, skip it
				// (changed on 25-Apr-2007 to show all fields, regardless of
				// visibility...
				// this change was made after discussions with Eric regarding a
				// request from
				// Chris to allow for any fields from the superclass, not just
				// visible fields,
				// as you may set these values in the superclass through a
				// publicly accessible
				// setter even though you can't access them directly)
				// if ((visibility == IField.VISIBILITY_PACKAGE &&
				// !lclArtifact.getPackage().equals(artifact.getPackage()))
				// || visibility == IField.VISIBILITY_PRIVATE)
				// continue;
				lclFieldSet.add(field);
				fieldNames.add(field.getLabelString());
			}
			fieldStack.push(lclFieldSet);
		} while ((lclArtifact = lclArtifact.getExtendedArtifact()) != null);
		// pull apart the variable list (from the root class for this artifact
		// down the
		// inheritance tree to this artifact) and add artifacts from each class
		// (in turn)
		// to the list of variables (note, with this structure in place, fields
		// from the
		// sub classes replace fields from the super class as they should)
		List classFields;
		while (!fieldStack.empty()) {
			classFields = fieldStack.pop();
			if (classFields != null && classFields.size() > 0)
				iFieldSet.addAll(classFields);
		}
		IField[] fields = new IField[iFieldSet.size()];
		iFieldSet.toArray(fields);
		return fields;
	}

	public static Set<IRelationship> getRelationshipSet(
			IArtifactManagerSession artifactMgrSession,
			IAbstractArtifact sourceArtifact, IAbstractArtifact targetArtifact) {
		
		
		Set<IRelationship> relationshipSet = new HashSet<IRelationship>();
		
		// put together a list of all of the relationships that can be built
		// with the source's
		// FQN (or the FQN of any of the source's parents) as the source of the
		// relationship
		{
			Set<IAbstractArtifact> heirarhy = new HashSet<IAbstractArtifact>();
			ModelUtils.featchHierarhyUpAsModels(sourceArtifact, heirarhy);
			for (IAbstractArtifact artifact : heirarhy) {
				String fqn = artifact.getFullyQualifiedName();
				try {
					List<IRelationship> relList = artifactMgrSession
							.getOriginatingRelationshipForFQN(fqn, true);
					if (relList != null && relList.size() > 0)
						relationshipSet.addAll(relList);
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logInfoMessage(
							"TigerstripeException detected", e);
				}
			}
		}
			
		// put together a list of all of the relationships that can be built
		// with the target's
		// FQN (or the FQN of any of the target's parents) as the target of the
		// relationship
		Set<IRelationship> targetRelationshipSet = new HashSet<IRelationship>();
		{
			Set<IAbstractArtifact> heirarhy = new HashSet<IAbstractArtifact>();
			ModelUtils.featchHierarhyUpAsModels(targetArtifact, heirarhy);

			for (IAbstractArtifact artifact : heirarhy) {
				String fqn = artifact.getFullyQualifiedName();
				try {
					List<IRelationship> relList = artifactMgrSession
							.getTerminatingRelationshipForFQN(fqn, true);
					if (relList != null && relList.size() > 0)
						targetRelationshipSet.addAll(relList);
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logInfoMessage(
							"TigerstripeException detected", e);
				}
			}
		}
		// the intersection of these two sets is the set of relationships that
		// can be built between
		// the source and target for this association instance
		relationshipSet.retainAll(targetRelationshipSet);
		return relationshipSet;
	}

	public static IField findIFieldByName(IAbstractArtifact iArtifact,
			String name) {
		IField[] iFields = InstanceDiagramUtils.getInstanceVariables(iArtifact);
		for (IField field : iFields) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}

	public static int getRelationshipMuliplicity(IModelComponent.EMultiplicity multiplicity) {
		if (multiplicity == IModelComponent.EMultiplicity.ONE)
			return RELATIONSHIP_SINGLE;
		else if (multiplicity == IModelComponent.EMultiplicity.ZERO)
			return RELATIONSHIP_SINGLE;
		else if (multiplicity == IModelComponent.EMultiplicity.ZERO_ONE)
			return RELATIONSHIP_SINGLE;
		else if (multiplicity == IModelComponent.EMultiplicity.ONE_STAR)
			return RELATIONSHIP_MULTIPLE;
		else if (multiplicity == IModelComponent.EMultiplicity.STAR)
			return RELATIONSHIP_MULTIPLE;
		else if (multiplicity == IModelComponent.EMultiplicity.ZERO_STAR)
			return RELATIONSHIP_MULTIPLE;
		throw new IllegalArgumentException("Illegal value " + multiplicity
				+ " found");
	}

	public static void updateAggregation(AssociationInstance eAssociation,
			IAssociationArtifact iArtifact, boolean updateAEnd,
			boolean updateZEnd) {

		IAssociationEnd aEnd = (IAssociationEnd) iArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iArtifact.getZEnd();

		if (updateAEnd) {
			if (aEnd.getAggregation() == EAggregationEnum.NONE
					&& eAssociation.getAEndAggregation() != AggregationEnum.NONE_LITERAL) {
				eAssociation.setAEndAggregation(AggregationEnum.NONE_LITERAL);
			} else if (aEnd.getAggregation() == EAggregationEnum.COMPOSITE
					&& eAssociation.getAEndAggregation() != AggregationEnum.COMPOSITE_LITERAL) {
				eAssociation
						.setAEndAggregation(AggregationEnum.COMPOSITE_LITERAL);
			} else if (aEnd.getAggregation() == EAggregationEnum.SHARED
					&& eAssociation.getAEndAggregation() != AggregationEnum.SHARED_LITERAL) {
				eAssociation.setAEndAggregation(AggregationEnum.SHARED_LITERAL);
			}
		}

		if (updateZEnd) {
			if (zEnd.getAggregation() == EAggregationEnum.NONE
					&& eAssociation.getZEndAggregation() != AggregationEnum.NONE_LITERAL) {
				eAssociation.setZEndAggregation(AggregationEnum.NONE_LITERAL);
			} else if (zEnd.getAggregation() == EAggregationEnum.COMPOSITE
					&& eAssociation.getZEndAggregation() != AggregationEnum.COMPOSITE_LITERAL) {
				eAssociation
						.setZEndAggregation(AggregationEnum.COMPOSITE_LITERAL);
			} else if (zEnd.getAggregation() == EAggregationEnum.SHARED
					&& eAssociation.getZEndAggregation() != AggregationEnum.SHARED_LITERAL) {
				eAssociation.setZEndAggregation(AggregationEnum.SHARED_LITERAL);
			}
		}

	}

	public static void updateMultiplicity(AssociationInstance eAssociation,
			IAssociationArtifact iArtifact, boolean updateAEnd,
			boolean updateZEnd) {

		IAssociationEnd aEnd = (IAssociationEnd) iArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iArtifact.getZEnd();

		if (updateAEnd) {
			String lowerBound = eAssociation.getAEndMultiplicityLowerBound();
			String upperBound = eAssociation.getAEndMultiplicityUpperBound();
			if (aEnd.getMultiplicity() == IModelComponent.EMultiplicity.ONE
					&& (lowerBound == null || !"1".equals(lowerBound))) {
				eAssociation.setAEndMultiplicityLowerBound("1");
				eAssociation.setAEndMultiplicityUpperBound("");
			} else if (aEnd.getMultiplicity() == IModelComponent.EMultiplicity.ONE_STAR) {
				if (lowerBound == null || !"1".equals(lowerBound)) {
					eAssociation.setAEndMultiplicityLowerBound("1");
				}
				if (upperBound == null || !"*".equals(upperBound)) {
					eAssociation.setAEndMultiplicityUpperBound("*");
				}
			} else if (aEnd.getMultiplicity() == IModelComponent.EMultiplicity.STAR
					&& (lowerBound == null || !"*".equals(lowerBound))) {
				eAssociation.setAEndMultiplicityLowerBound("*");
				eAssociation.setAEndMultiplicityUpperBound("");
			} else if (aEnd.getMultiplicity() == IModelComponent.EMultiplicity.ZERO
					&& (lowerBound == null || !"0".equals(lowerBound))) {
				eAssociation.setAEndMultiplicityLowerBound("0");
				eAssociation.setAEndMultiplicityUpperBound("");
			} else if (aEnd.getMultiplicity() == IModelComponent.EMultiplicity.ZERO_ONE) {
				if (lowerBound == null || !"0".equals(lowerBound)) {
					eAssociation.setAEndMultiplicityLowerBound("0");
				}
				if (upperBound == null || !"1".equals(upperBound)) {
					eAssociation.setAEndMultiplicityUpperBound("1");
				}
			} else if (aEnd.getMultiplicity() == IModelComponent.EMultiplicity.ZERO_STAR) {
				if (lowerBound == null || !"0".equals(lowerBound)) {
					eAssociation.setAEndMultiplicityLowerBound("0");
				}
				if (upperBound == null || !"*".equals(upperBound)) {
					eAssociation.setAEndMultiplicityUpperBound("*");
				}
			}
		}

		if (updateZEnd) {
			String lowerBound = eAssociation.getZEndMultiplicityLowerBound();
			String upperBound = eAssociation.getZEndMultiplicityLowerBound();
			if (zEnd.getMultiplicity() == IModelComponent.EMultiplicity.ONE
					&& (lowerBound == null || !"1".equals(lowerBound))) {
				eAssociation.setZEndMultiplicityLowerBound("1");
				eAssociation.setZEndMultiplicityUpperBound("");
			} else if (zEnd.getMultiplicity() == IModelComponent.EMultiplicity.ONE_STAR) {
				if (lowerBound == null || !"1".equals(lowerBound)) {
					eAssociation.setZEndMultiplicityLowerBound("1");
				}
				if (upperBound == null || !"*".equals(upperBound)) {
					eAssociation.setZEndMultiplicityUpperBound("*");
				}
			} else if (zEnd.getMultiplicity() == IModelComponent.EMultiplicity.STAR
					&& (lowerBound == null || !"*".equals(lowerBound))) {
				eAssociation.setZEndMultiplicityLowerBound("*");
				eAssociation.setZEndMultiplicityUpperBound("");
			} else if (zEnd.getMultiplicity() == IModelComponent.EMultiplicity.ZERO
					&& (lowerBound == null || !"0".equals(lowerBound))) {
				eAssociation.setZEndMultiplicityLowerBound("0");
				eAssociation.setZEndMultiplicityUpperBound("");
			} else if (zEnd.getMultiplicity() == IModelComponent.EMultiplicity.ZERO_ONE) {
				if (lowerBound == null || !"0".equals(lowerBound)) {
					eAssociation.setZEndMultiplicityLowerBound("0");
				}
				if (upperBound == null || !"1".equals(upperBound)) {
					eAssociation.setZEndMultiplicityUpperBound("1");
				}
			} else if (zEnd.getMultiplicity() == IModelComponent.EMultiplicity.ZERO_STAR) {
				if (lowerBound == null || !"0".equals(lowerBound)) {
					eAssociation.setZEndMultiplicityLowerBound("0");
				}
				if (upperBound == null || !"*".equals(upperBound)) {
					eAssociation.setZEndMultiplicityUpperBound("*");
				}
			}
		}

	}

	public static void updateChangeable(AssociationInstance eAssociation,
			IAssociationArtifact iArtifact, boolean updateAEnd,
			boolean updateZEnd) {

		IAssociationEnd aEnd = (IAssociationEnd) iArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iArtifact.getZEnd();

		if (updateAEnd) {
			if (aEnd.getChangeable() == EChangeableEnum.ADDONLY
					&& eAssociation.getAEndIsChangeable() != ChangeableEnum.ADD_ONLY_LITERAL) {
				eAssociation
						.setAEndIsChangeable(ChangeableEnum.ADD_ONLY_LITERAL);
			} else if (aEnd.getChangeable() == EChangeableEnum.FROZEN
					&& eAssociation.getAEndIsChangeable() != ChangeableEnum.FROZEN_LITERAL) {
				eAssociation.setAEndIsChangeable(ChangeableEnum.FROZEN_LITERAL);
			} else if (aEnd.getChangeable() == EChangeableEnum.NONE
					&& eAssociation.getAEndIsChangeable() != ChangeableEnum.NONE_LITERAL) {
				eAssociation.setAEndIsChangeable(ChangeableEnum.NONE_LITERAL);
			}
		}
		if (updateZEnd) {
			if (zEnd.getChangeable() == EChangeableEnum.ADDONLY
					&& eAssociation.getZEndIsChangeable() != ChangeableEnum.ADD_ONLY_LITERAL) {
				eAssociation
						.setZEndIsChangeable(ChangeableEnum.ADD_ONLY_LITERAL);
			} else if (zEnd.getChangeable() == EChangeableEnum.FROZEN
					&& eAssociation.getZEndIsChangeable() != ChangeableEnum.FROZEN_LITERAL) {
				eAssociation.setZEndIsChangeable(ChangeableEnum.FROZEN_LITERAL);
			} else if (zEnd.getChangeable() == EChangeableEnum.NONE
					&& eAssociation.getZEndIsChangeable() != ChangeableEnum.NONE_LITERAL) {
				eAssociation.setZEndIsChangeable(ChangeableEnum.NONE_LITERAL);
			}
		}

	}

	public static boolean isPrimitive(IArtifactManagerSession artMgrSession,
			IType type) {
		try {
			IAbstractArtifact iArt = artMgrSession
					.getArtifactByFullyQualifiedName(type
							.getFullyQualifiedName());
			if (iArt instanceof IPrimitiveTypeArtifact)
				return true;
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logInfoMessage("TigerstripeException detected",
					e);
		}
		return (type.isPrimitive() || type.isEnum() || "String".equals(type
				.getName()));
	}

	public static String removeInstanceReference(String referenceValue,
			String instanceName) {
		String[] values = referenceValue.split("[, ]+");
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for (String value : values) {
			++count;
			if (value.equals(instanceName)) {
				if (count == values.length) {
					// removing the last instance from the list, so have a
					// couple
					// of extra characters at the end of the string...remove
					// them
					int extraCharPos = buffer.lastIndexOf(", ");
					int buffLen = buffer.length();
					if (extraCharPos >= 0)
						buffer.delete(extraCharPos, buffLen);
				}
				continue;
			}
			buffer.append(value);
			if (count < values.length)
				buffer.append(", ");
		}
		return buffer.toString();
	}

	public static String renameInstanceReference(String referenceValue,
			String oldName, String newName) {
		String[] values = referenceValue.split("[, ]+");
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for (String value : values) {
			if (value.equals(oldName))
				buffer.append(newName);
			else
				buffer.append(value);
			if (++count < values.length)
				buffer.append(", ");
		}
		return buffer.toString();
	}

	public static List<String> instanceReferencesAsList(String referenceValue) {
		String[] values = (referenceValue).split("[, ]+");
		return Arrays.asList(values);
	}

}
