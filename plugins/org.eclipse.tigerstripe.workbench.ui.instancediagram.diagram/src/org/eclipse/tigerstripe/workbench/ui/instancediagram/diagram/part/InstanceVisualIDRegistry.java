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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceAEndMultiplicityLowerBoEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceNamePackageArtifactNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceZEndMultiplicityLowerBoEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceNamePackageArtifactNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceVariableCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.VariableEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented by
 * a domain model object.
 * 
 * @generated
 */
public class InstanceVisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = InstanceDiagramEditorPlugin
			.getInstance().getBundle().getSymbolicName()
			+ "/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (InstanceMapEditPart.MODEL_ID.equals(view.getType()))
				return InstanceMapEditPart.VISUAL_ID;
			else
				return -1;
		}
		return getVisualID(view.getType());
	}

	/**
	 * @generated
	 */
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null)
				return annotation.getDetails().get("modelID"); //$NON-NLS-1$
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	 * @generated
	 */
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(
					Platform.getDebugOption(DEBUG_KEY))) {
				InstanceDiagramEditorPlugin.getInstance().logError(
						"Unable to parse view type as a visualID number: "
								+ type);
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static String getType(int visualID) {
		return String.valueOf(visualID);
	}

	/**
	 * @generated
	 */
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null)
			return -1;
		EClass domainElementMetaclass = domainElement.eClass();
		return getDiagramVisualID(domainElement, domainElementMetaclass);
	}

	/**
	 * @generated
	 */
	private static int getDiagramVisualID(EObject domainElement,
			EClass domainElementMetaclass) {
		if (InstancediagramPackage.eINSTANCE.getInstanceMap().isSuperTypeOf(
				domainElementMetaclass)
				&& isDiagramInstanceMap_79((InstanceMap) domainElement))
			return InstanceMapEditPart.VISUAL_ID;
		return getUnrecognizedDiagramID(domainElement);
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null)
			return -1;
		EClass domainElementMetaclass = domainElement.eClass();
		return getNodeVisualID(containerView, domainElement,
				domainElementMetaclass, null);
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView,
			EObject domainElement, EClass domainElementMetaclass,
			String semanticHint) {
		String containerModelID = getModelID(containerView);
		if (!InstanceMapEditPart.MODEL_ID.equals(containerModelID))
			return -1;
		int containerVisualID;
		if (InstanceMapEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = InstanceMapEditPart.VISUAL_ID;
			} else
				return -1;
		}
		int nodeVisualID = semanticHint != null ? getVisualID(semanticHint)
				: -1;
		switch (containerVisualID) {
		case ClassInstanceEditPart.VISUAL_ID:
			if (ClassInstanceNamePackageArtifactNameEditPart.VISUAL_ID == nodeVisualID)
				return ClassInstanceNamePackageArtifactNameEditPart.VISUAL_ID;
			if (ClassInstanceVariableCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return ClassInstanceVariableCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedClassInstance_1001ChildNodeID(domainElement,
					semanticHint);
		case VariableEditPart.VISUAL_ID:
			return getUnrecognizedVariable_2001ChildNodeID(domainElement,
					semanticHint);
		case ClassInstanceVariableCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || VariableEditPart.VISUAL_ID == nodeVisualID)
					&& InstancediagramPackage.eINSTANCE.getVariable()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeVariable_2001((Variable) domainElement)))
				return VariableEditPart.VISUAL_ID;
			return getUnrecognizedClassInstanceVariableCompartment_5001ChildNodeID(
					domainElement, semanticHint);
		case InstanceMapEditPart.VISUAL_ID:
			if ((semanticHint == null || ClassInstanceEditPart.VISUAL_ID == nodeVisualID)
					&& InstancediagramPackage.eINSTANCE.getClassInstance()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeClassInstance_1001((ClassInstance) domainElement)))
				return ClassInstanceEditPart.VISUAL_ID;
			return getUnrecognizedInstanceMap_79ChildNodeID(domainElement,
					semanticHint);
		case AssociationInstanceEditPart.VISUAL_ID:
			if (AssociationInstanceNamePackageArtifactNameEditPart.VISUAL_ID == nodeVisualID)
				return AssociationInstanceNamePackageArtifactNameEditPart.VISUAL_ID;
			if (AssociationInstanceAEndMultiplicityLowerBoEditPart.VISUAL_ID == nodeVisualID)
				return AssociationInstanceAEndMultiplicityLowerBoEditPart.VISUAL_ID;
			if (AssociationInstanceAEndNameEditPart.VISUAL_ID == nodeVisualID)
				return AssociationInstanceAEndNameEditPart.VISUAL_ID;
			if (AssociationInstanceZEndMultiplicityLowerBoEditPart.VISUAL_ID == nodeVisualID)
				return AssociationInstanceZEndMultiplicityLowerBoEditPart.VISUAL_ID;
			if (AssociationInstanceZEndNameEditPart.VISUAL_ID == nodeVisualID)
				return AssociationInstanceZEndNameEditPart.VISUAL_ID;
			return getUnrecognizedAssociationInstance_3001LinkLabelID(semanticHint);
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null)
			return -1;
		EClass domainElementMetaclass = domainElement.eClass();
		return getLinkWithClassVisualID(domainElement, domainElementMetaclass);
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement,
			EClass domainElementMetaclass) {
		if (InstancediagramPackage.eINSTANCE.getAssociationInstance()
				.isSuperTypeOf(domainElementMetaclass)
				&& (domainElement == null || isLinkWithClassAssociationInstance_3001((AssociationInstance) domainElement)))
			return AssociationInstanceEditPart.VISUAL_ID;
		else
			return getUnrecognizedLinkWithClassID(domainElement);
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isDiagramInstanceMap_79(InstanceMap element) {
		return true;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedDiagramID(EObject domainElement) {
		return -1;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeClassInstance_1001(ClassInstance element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeVariable_2001(Variable element) {
		return true;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedClassInstance_1001ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedVariable_2001ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedClassInstanceVariableCompartment_5001ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedInstanceMap_79ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAssociationInstance_3001LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedLinkWithClassID(EObject domainElement) {
		return -1;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isLinkWithClassAssociationInstance_3001(
			AssociationInstance element) {
		return true;
	}
}
