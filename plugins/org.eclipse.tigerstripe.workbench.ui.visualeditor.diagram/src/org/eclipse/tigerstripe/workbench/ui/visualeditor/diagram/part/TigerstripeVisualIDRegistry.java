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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationAEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassAEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassMethodCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassZEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationZEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute2EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute4EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute5EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute6EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute7EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AttributeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactMethodCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationBaseTypeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationLiteralCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.LiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactMethodCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method2EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method4EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MethodEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactReturnedTypeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.QueryReturnsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ReferenceEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ReferenceMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ReferenceNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionEmitsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionExposesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEmittedNotificationsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactExposedProceduresEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactManagedEntitiesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactMethodCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactNamedQueriesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionManagesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionSupportsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactStereotypesEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented by
 * a domain model object.
 * 
 * @generated
 */
public class TigerstripeVisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = TigerstripeDiagramEditorPlugin
			.getInstance().getBundle().getSymbolicName()
			+ "/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (MapEditPart.MODEL_ID.equals(view.getType()))
				return MapEditPart.VISUAL_ID;
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
				TigerstripeDiagramEditorPlugin.getInstance().logError(
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
		if (VisualeditorPackage.eINSTANCE.getMap().isSuperTypeOf(
				domainElementMetaclass)
				&& isDiagramMap_79((Map) domainElement))
			return MapEditPart.VISUAL_ID;
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
		if (!MapEditPart.MODEL_ID.equals(containerModelID))
			return -1;
		int containerVisualID;
		if (MapEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = MapEditPart.VISUAL_ID;
			} else
				return -1;
		}
		int nodeVisualID = semanticHint != null ? getVisualID(semanticHint)
				: -1;
		switch (containerVisualID) {
		case NamedQueryArtifactEditPart.VISUAL_ID:
			if (NamedQueryArtifactStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return NamedQueryArtifactStereotypesEditPart.VISUAL_ID;
			if (NamedQueryArtifactNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return NamedQueryArtifactNamePackageEditPart.VISUAL_ID;
			if (NamedQueryArtifactAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return NamedQueryArtifactAttributeCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedNamedQueryArtifact_1001ChildNodeID(
					domainElement, semanticHint);
		case ExceptionArtifactEditPart.VISUAL_ID:
			if (ExceptionArtifactStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return ExceptionArtifactStereotypesEditPart.VISUAL_ID;
			if (ExceptionArtifactNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return ExceptionArtifactNamePackageEditPart.VISUAL_ID;
			if (ExceptionArtifactAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return ExceptionArtifactAttributeCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedExceptionArtifact_1002ChildNodeID(
					domainElement, semanticHint);
		case ManagedEntityArtifactEditPart.VISUAL_ID:
			if (ManagedEntityArtifactStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return ManagedEntityArtifactStereotypesEditPart.VISUAL_ID;
			if (ManagedEntityArtifactNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return ManagedEntityArtifactNamePackageEditPart.VISUAL_ID;
			if (ManagedEntityArtifactAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return ManagedEntityArtifactAttributeCompartmentEditPart.VISUAL_ID;
			if (ManagedEntityArtifactMethodCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return ManagedEntityArtifactMethodCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedManagedEntityArtifact_1003ChildNodeID(
					domainElement, semanticHint);
		case NotificationArtifactEditPart.VISUAL_ID:
			if (NotificationArtifactStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return NotificationArtifactStereotypesEditPart.VISUAL_ID;
			if (NotificationArtifactNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return NotificationArtifactNamePackageEditPart.VISUAL_ID;
			if (NotificationArtifactAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return NotificationArtifactAttributeCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedNotificationArtifact_1004ChildNodeID(
					domainElement, semanticHint);
		case DatatypeArtifactEditPart.VISUAL_ID:
			if (DatatypeArtifactStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return DatatypeArtifactStereotypesEditPart.VISUAL_ID;
			if (DatatypeArtifactNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return DatatypeArtifactNamePackageEditPart.VISUAL_ID;
			if (DatatypeArtifactAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return DatatypeArtifactAttributeCompartmentEditPart.VISUAL_ID;
			if (DatatypeArtifactMethodCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return DatatypeArtifactMethodCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedDatatypeArtifact_1005ChildNodeID(
					domainElement, semanticHint);
		case EnumerationEditPart.VISUAL_ID:
			if (EnumerationStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return EnumerationStereotypesEditPart.VISUAL_ID;
			if (EnumerationNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return EnumerationNamePackageEditPart.VISUAL_ID;
			if (EnumerationBaseTypeEditPart.VISUAL_ID == nodeVisualID)
				return EnumerationBaseTypeEditPart.VISUAL_ID;
			if (EnumerationLiteralCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return EnumerationLiteralCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedEnumeration_1006ChildNodeID(domainElement,
					semanticHint);
		case UpdateProcedureArtifactEditPart.VISUAL_ID:
			if (UpdateProcedureArtifactStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return UpdateProcedureArtifactStereotypesEditPart.VISUAL_ID;
			if (UpdateProcedureArtifactNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return UpdateProcedureArtifactNamePackageEditPart.VISUAL_ID;
			if (UpdateProcedureArtifactAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return UpdateProcedureArtifactAttributeCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedUpdateProcedureArtifact_1007ChildNodeID(
					domainElement, semanticHint);
		case SessionFacadeArtifactEditPart.VISUAL_ID:
			if (SessionFacadeArtifactStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return SessionFacadeArtifactStereotypesEditPart.VISUAL_ID;
			if (SessionFacadeArtifactNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return SessionFacadeArtifactNamePackageEditPart.VISUAL_ID;
			if (SessionFacadeArtifactMethodCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return SessionFacadeArtifactMethodCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedSessionFacadeArtifact_1008ChildNodeID(
					domainElement, semanticHint);
		case AssociationClassClassEditPart.VISUAL_ID:
			if (AssociationClassClassStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassClassStereotypesEditPart.VISUAL_ID;
			if (AssociationClassClassNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassClassNamePackageEditPart.VISUAL_ID;
			if (AssociationClassClassAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassClassAttributeCompartmentEditPart.VISUAL_ID;
			if (AssociationClassClassMethodCompartmentEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassClassMethodCompartmentEditPart.VISUAL_ID;
			return getUnrecognizedAssociationClassClass_1009ChildNodeID(
					domainElement, semanticHint);
		case AttributeEditPart.VISUAL_ID:
			return getUnrecognizedAttribute_2001ChildNodeID(domainElement,
					semanticHint);
		case Attribute2EditPart.VISUAL_ID:
			return getUnrecognizedAttribute_2002ChildNodeID(domainElement,
					semanticHint);
		case Attribute3EditPart.VISUAL_ID:
			return getUnrecognizedAttribute_2003ChildNodeID(domainElement,
					semanticHint);
		case MethodEditPart.VISUAL_ID:
			return getUnrecognizedMethod_2004ChildNodeID(domainElement,
					semanticHint);
		case Attribute4EditPart.VISUAL_ID:
			return getUnrecognizedAttribute_2005ChildNodeID(domainElement,
					semanticHint);
		case Attribute5EditPart.VISUAL_ID:
			return getUnrecognizedAttribute_2006ChildNodeID(domainElement,
					semanticHint);
		case Method2EditPart.VISUAL_ID:
			return getUnrecognizedMethod_2007ChildNodeID(domainElement,
					semanticHint);
		case LiteralEditPart.VISUAL_ID:
			return getUnrecognizedLiteral_2008ChildNodeID(domainElement,
					semanticHint);
		case Attribute6EditPart.VISUAL_ID:
			return getUnrecognizedAttribute_2009ChildNodeID(domainElement,
					semanticHint);
		case Method3EditPart.VISUAL_ID:
			return getUnrecognizedMethod_2010ChildNodeID(domainElement,
					semanticHint);
		case Attribute7EditPart.VISUAL_ID:
			return getUnrecognizedAttribute_2011ChildNodeID(domainElement,
					semanticHint);
		case Method4EditPart.VISUAL_ID:
			return getUnrecognizedMethod_2012ChildNodeID(domainElement,
					semanticHint);
		case NamedQueryArtifactAttributeCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || AttributeEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAttribute()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAttribute_2001((Attribute) domainElement)))
				return AttributeEditPart.VISUAL_ID;
			return getUnrecognizedNamedQueryArtifactAttributeCompartment_5001ChildNodeID(
					domainElement, semanticHint);
		case ExceptionArtifactAttributeCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Attribute2EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAttribute()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAttribute_2002((Attribute) domainElement)))
				return Attribute2EditPart.VISUAL_ID;
			return getUnrecognizedExceptionArtifactAttributeCompartment_5002ChildNodeID(
					domainElement, semanticHint);
		case ManagedEntityArtifactAttributeCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Attribute3EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAttribute()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAttribute_2003((Attribute) domainElement)))
				return Attribute3EditPart.VISUAL_ID;
			return getUnrecognizedManagedEntityArtifactAttributeCompartment_5003ChildNodeID(
					domainElement, semanticHint);
		case ManagedEntityArtifactMethodCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || MethodEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getMethod().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeMethod_2004((Method) domainElement)))
				return MethodEditPart.VISUAL_ID;
			return getUnrecognizedManagedEntityArtifactMethodCompartment_5004ChildNodeID(
					domainElement, semanticHint);
		case NotificationArtifactAttributeCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Attribute4EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAttribute()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAttribute_2005((Attribute) domainElement)))
				return Attribute4EditPart.VISUAL_ID;
			return getUnrecognizedNotificationArtifactAttributeCompartment_5005ChildNodeID(
					domainElement, semanticHint);
		case DatatypeArtifactAttributeCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Attribute5EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAttribute()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAttribute_2006((Attribute) domainElement)))
				return Attribute5EditPart.VISUAL_ID;
			return getUnrecognizedDatatypeArtifactAttributeCompartment_5006ChildNodeID(
					domainElement, semanticHint);
		case DatatypeArtifactMethodCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Method2EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getMethod().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeMethod_2007((Method) domainElement)))
				return Method2EditPart.VISUAL_ID;
			return getUnrecognizedDatatypeArtifactMethodCompartment_5007ChildNodeID(
					domainElement, semanticHint);
		case EnumerationLiteralCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || LiteralEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getLiteral()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeLiteral_2008((Literal) domainElement)))
				return LiteralEditPart.VISUAL_ID;
			return getUnrecognizedEnumerationLiteralCompartment_5008ChildNodeID(
					domainElement, semanticHint);
		case UpdateProcedureArtifactAttributeCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Attribute6EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAttribute()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAttribute_2009((Attribute) domainElement)))
				return Attribute6EditPart.VISUAL_ID;
			return getUnrecognizedUpdateProcedureArtifactAttributeCompartment_5009ChildNodeID(
					domainElement, semanticHint);
		case SessionFacadeArtifactMethodCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Method3EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getMethod().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeMethod_2010((Method) domainElement)))
				return Method3EditPart.VISUAL_ID;
			return getUnrecognizedSessionFacadeArtifactMethodCompartment_5010ChildNodeID(
					domainElement, semanticHint);
		case AssociationClassClassAttributeCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Attribute7EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAttribute()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAttribute_2011((Attribute) domainElement)))
				return Attribute7EditPart.VISUAL_ID;
			return getUnrecognizedAssociationClassClassAttributeCompartment_5011ChildNodeID(
					domainElement, semanticHint);
		case AssociationClassClassMethodCompartmentEditPart.VISUAL_ID:
			if ((semanticHint == null || Method4EditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getMethod().isSuperTypeOf(
							domainElementMetaclass)
					&& (domainElement == null || isNodeMethod_2012((Method) domainElement)))
				return Method4EditPart.VISUAL_ID;
			return getUnrecognizedAssociationClassClassMethodCompartment_5012ChildNodeID(
					domainElement, semanticHint);
		case MapEditPart.VISUAL_ID:
			if ((semanticHint == null || NamedQueryArtifactEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getNamedQueryArtifact()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeNamedQueryArtifact_1001((NamedQueryArtifact) domainElement)))
				return NamedQueryArtifactEditPart.VISUAL_ID;
			if ((semanticHint == null || ExceptionArtifactEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getExceptionArtifact()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeExceptionArtifact_1002((ExceptionArtifact) domainElement)))
				return ExceptionArtifactEditPart.VISUAL_ID;
			if ((semanticHint == null || ManagedEntityArtifactEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getManagedEntityArtifact()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeManagedEntityArtifact_1003((ManagedEntityArtifact) domainElement)))
				return ManagedEntityArtifactEditPart.VISUAL_ID;
			if ((semanticHint == null || NotificationArtifactEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getNotificationArtifact()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeNotificationArtifact_1004((NotificationArtifact) domainElement)))
				return NotificationArtifactEditPart.VISUAL_ID;
			if ((semanticHint == null || DatatypeArtifactEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getDatatypeArtifact()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeDatatypeArtifact_1005((DatatypeArtifact) domainElement)))
				return DatatypeArtifactEditPart.VISUAL_ID;
			if ((semanticHint == null || EnumerationEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getEnumeration()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeEnumeration_1006((Enumeration) domainElement)))
				return EnumerationEditPart.VISUAL_ID;
			if ((semanticHint == null || UpdateProcedureArtifactEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE
							.getUpdateProcedureArtifact().isSuperTypeOf(
									domainElementMetaclass)
					&& (domainElement == null || isNodeUpdateProcedureArtifact_1007((UpdateProcedureArtifact) domainElement)))
				return UpdateProcedureArtifactEditPart.VISUAL_ID;
			if ((semanticHint == null || SessionFacadeArtifactEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getSessionFacadeArtifact()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeSessionFacadeArtifact_1008((SessionFacadeArtifact) domainElement)))
				return SessionFacadeArtifactEditPart.VISUAL_ID;
			if ((semanticHint == null || AssociationClassClassEditPart.VISUAL_ID == nodeVisualID)
					&& VisualeditorPackage.eINSTANCE.getAssociationClassClass()
							.isSuperTypeOf(domainElementMetaclass)
					&& (domainElement == null || isNodeAssociationClassClass_1009((AssociationClassClass) domainElement)))
				return AssociationClassClassEditPart.VISUAL_ID;
			return getUnrecognizedMap_79ChildNodeID(domainElement, semanticHint);
		case AssociationEditPart.VISUAL_ID:
			if (AssociationStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return AssociationStereotypesEditPart.VISUAL_ID;
			if (AssociationNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return AssociationNamePackageEditPart.VISUAL_ID;
			if (AssociationAEndMultiplicityEditPart.VISUAL_ID == nodeVisualID)
				return AssociationAEndMultiplicityEditPart.VISUAL_ID;
			if (AssociationZEndMultiplicityEditPart.VISUAL_ID == nodeVisualID)
				return AssociationZEndMultiplicityEditPart.VISUAL_ID;
			if (AssociationAEndNameEditPart.VISUAL_ID == nodeVisualID)
				return AssociationAEndNameEditPart.VISUAL_ID;
			if (AssociationZEndNameEditPart.VISUAL_ID == nodeVisualID)
				return AssociationZEndNameEditPart.VISUAL_ID;
			return getUnrecognizedAssociation_3001LinkLabelID(semanticHint);
		case SessionFacadeArtifactEmittedNotificationsEditPart.VISUAL_ID:
			if (SessionEmitsEditPart.VISUAL_ID == nodeVisualID)
				return SessionEmitsEditPart.VISUAL_ID;
			return getUnrecognizedSessionFacadeArtifactEmittedNotifications_3002LinkLabelID(semanticHint);
		case SessionFacadeArtifactManagedEntitiesEditPart.VISUAL_ID:
			if (SessionManagesEditPart.VISUAL_ID == nodeVisualID)
				return SessionManagesEditPart.VISUAL_ID;
			return getUnrecognizedSessionFacadeArtifactManagedEntities_3003LinkLabelID(semanticHint);
		case NamedQueryArtifactReturnedTypeEditPart.VISUAL_ID:
			if (QueryReturnsEditPart.VISUAL_ID == nodeVisualID)
				return QueryReturnsEditPart.VISUAL_ID;
			return getUnrecognizedNamedQueryArtifactReturnedType_3004LinkLabelID(semanticHint);
		case SessionFacadeArtifactNamedQueriesEditPart.VISUAL_ID:
			if (SessionSupportsEditPart.VISUAL_ID == nodeVisualID)
				return SessionSupportsEditPart.VISUAL_ID;
			return getUnrecognizedSessionFacadeArtifactNamedQueries_3005LinkLabelID(semanticHint);
		case SessionFacadeArtifactExposedProceduresEditPart.VISUAL_ID:
			if (SessionExposesEditPart.VISUAL_ID == nodeVisualID)
				return SessionExposesEditPart.VISUAL_ID;
			return getUnrecognizedSessionFacadeArtifactExposedProcedures_3006LinkLabelID(semanticHint);
		case DependencyEditPart.VISUAL_ID:
			if (DependencyNamePackageEditPart.VISUAL_ID == nodeVisualID)
				return DependencyNamePackageEditPart.VISUAL_ID;
			if (DependencyStereotypesEditPart.VISUAL_ID == nodeVisualID)
				return DependencyStereotypesEditPart.VISUAL_ID;
			return getUnrecognizedDependency_3008LinkLabelID(semanticHint);
		case ReferenceEditPart.VISUAL_ID:
			if (ReferenceNameEditPart.VISUAL_ID == nodeVisualID)
				return ReferenceNameEditPart.VISUAL_ID;
			if (ReferenceMultiplicityEditPart.VISUAL_ID == nodeVisualID)
				return ReferenceMultiplicityEditPart.VISUAL_ID;
			return getUnrecognizedReference_3009LinkLabelID(semanticHint);
		case AssociationClassEditPart.VISUAL_ID:
			if (AssociationClassAEndMultiplicityEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassAEndMultiplicityEditPart.VISUAL_ID;
			if (AssociationClassZEndMultiplicityEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassZEndMultiplicityEditPart.VISUAL_ID;
			if (AssociationClassAEndNameEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassAEndNameEditPart.VISUAL_ID;
			if (AssociationClassZEndNameEditPart.VISUAL_ID == nodeVisualID)
				return AssociationClassZEndNameEditPart.VISUAL_ID;
			return getUnrecognizedAssociationClass_3010LinkLabelID(semanticHint);
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
	 * @generated NOT
	 */
	public static int getLinkWithClassVisualID(EObject domainElement,
			EClass domainElementMetaclass) {
		if (VisualeditorPackage.eINSTANCE.getAssociationClass().isSuperTypeOf(
				domainElementMetaclass)
				&& (domainElement == null || isLinkWithClassAssociationClass_3010((AssociationClass) domainElement)))
			return AssociationClassEditPart.VISUAL_ID;
		else if (VisualeditorPackage.eINSTANCE.getAssociation().isSuperTypeOf(
				domainElementMetaclass)
				&& (domainElement == null || isLinkWithClassAssociation_3001((Association) domainElement)))
			return AssociationEditPart.VISUAL_ID;
		else if (VisualeditorPackage.eINSTANCE.getDependency().isSuperTypeOf(
				domainElementMetaclass)
				&& (domainElement == null || isLinkWithClassDependency_3008((Dependency) domainElement)))
			return DependencyEditPart.VISUAL_ID;
		else if (VisualeditorPackage.eINSTANCE.getReference().isSuperTypeOf(
				domainElementMetaclass)
				&& (domainElement == null || isLinkWithClassReference_3009((Reference) domainElement)))
			return ReferenceEditPart.VISUAL_ID;
		else
			return getUnrecognizedLinkWithClassID(domainElement);
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isDiagramMap_79(Map element) {
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
	private static boolean isNodeNamedQueryArtifact_1001(
			NamedQueryArtifact element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeExceptionArtifact_1002(
			ExceptionArtifact element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeManagedEntityArtifact_1003(
			ManagedEntityArtifact element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeNotificationArtifact_1004(
			NotificationArtifact element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeDatatypeArtifact_1005(DatatypeArtifact element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeEnumeration_1006(Enumeration element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeUpdateProcedureArtifact_1007(
			UpdateProcedureArtifact element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeSessionFacadeArtifact_1008(
			SessionFacadeArtifact element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAssociationClassClass_1009(
			AssociationClassClass element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAttribute_2001(Attribute element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAttribute_2002(Attribute element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAttribute_2003(Attribute element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeMethod_2004(Method element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAttribute_2005(Attribute element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAttribute_2006(Attribute element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeMethod_2007(Method element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeLiteral_2008(Literal element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAttribute_2009(Attribute element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeMethod_2010(Method element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeAttribute_2011(Attribute element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isNodeMethod_2012(Method element) {
		return true;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedNamedQueryArtifact_1001ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedExceptionArtifact_1002ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedManagedEntityArtifact_1003ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedNotificationArtifact_1004ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedDatatypeArtifact_1005ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedEnumeration_1006ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedUpdateProcedureArtifact_1007ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedSessionFacadeArtifact_1008ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAssociationClassClass_1009ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAttribute_2001ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAttribute_2002ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAttribute_2003ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedMethod_2004ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAttribute_2005ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAttribute_2006ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedMethod_2007ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedLiteral_2008ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAttribute_2009ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedMethod_2010ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAttribute_2011ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedMethod_2012ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedNamedQueryArtifactAttributeCompartment_5001ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedExceptionArtifactAttributeCompartment_5002ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedManagedEntityArtifactAttributeCompartment_5003ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedManagedEntityArtifactMethodCompartment_5004ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedNotificationArtifactAttributeCompartment_5005ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedDatatypeArtifactAttributeCompartment_5006ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedDatatypeArtifactMethodCompartment_5007ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedEnumerationLiteralCompartment_5008ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedUpdateProcedureArtifactAttributeCompartment_5009ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedSessionFacadeArtifactMethodCompartment_5010ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAssociationClassClassAttributeCompartment_5011ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAssociationClassClassMethodCompartment_5012ChildNodeID(
			EObject domainElement, String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedMap_79ChildNodeID(EObject domainElement,
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAssociation_3001LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedSessionFacadeArtifactEmittedNotifications_3002LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedSessionFacadeArtifactManagedEntities_3003LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedNamedQueryArtifactReturnedType_3004LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedSessionFacadeArtifactNamedQueries_3005LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedSessionFacadeArtifactExposedProcedures_3006LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedDependency_3008LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedReference_3009LinkLabelID(
			String semanticHint) {
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static int getUnrecognizedAssociationClass_3010LinkLabelID(
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
	private static boolean isLinkWithClassAssociation_3001(Association element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isLinkWithClassDependency_3008(Dependency element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isLinkWithClassReference_3009(Reference element) {
		return true;
	}

	/**
	 * User can change implementation of this method to check some additional
	 * conditions here.
	 * 
	 * @generated
	 */
	private static boolean isLinkWithClassAssociationClass_3010(
			AssociationClass element) {
		return true;
	}
}
