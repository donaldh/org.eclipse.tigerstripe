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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactMethodCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionManagesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionSupportsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AbstractArtifactExtendsViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AbstractArtifactImplementsViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationAEndMultiplicityViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationAEndNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassAEndMultiplicityViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassAEndNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassAssociatedClassViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassClassAttributeCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassClassMethodCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassClassNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassClassStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassClassViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassZEndMultiplicityViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationClassZEndNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationZEndMultiplicityViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AssociationZEndNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Attribute2ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Attribute3ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Attribute4ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Attribute5ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Attribute6ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Attribute7ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.AttributeViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DatatypeArtifactAttributeCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DatatypeArtifactMethodCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DatatypeArtifactNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DatatypeArtifactStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DatatypeArtifactViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DependencyNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DependencyStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.DependencyViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.EnumerationBaseTypeViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.EnumerationLiteralCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.EnumerationNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.EnumerationStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.EnumerationViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ExceptionArtifactAttributeCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ExceptionArtifactNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ExceptionArtifactStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ExceptionArtifactViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.LiteralViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ManagedEntityArtifactAttributeCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ManagedEntityArtifactMethodCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ManagedEntityArtifactNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ManagedEntityArtifactStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ManagedEntityArtifactViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.MapViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Method2ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Method3ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.Method4ViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.MethodViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NamedQueryArtifactAttributeCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NamedQueryArtifactNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NamedQueryArtifactReturnedTypeViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NamedQueryArtifactStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NamedQueryArtifactViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NotificationArtifactAttributeCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NotificationArtifactNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NotificationArtifactStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.NotificationArtifactViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.QueryReturnsViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ReferenceMultiplicityViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ReferenceNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.ReferenceViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionEmitsViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionExposesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactEmittedNotificationsViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactExposedProceduresViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactManagedEntitiesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactMethodCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactNamedQueriesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionFacadeArtifactViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionManagesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.SessionSupportsViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.UpdateProcedureArtifactAttributeCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.UpdateProcedureArtifactNamePackageViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.UpdateProcedureArtifactStereotypesViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories.UpdateProcedureArtifactViewFactory;

/**
 * @generated
 */
public class TigerstripeViewProvider extends AbstractViewProvider {

	/**
	 * @generated
	 */
	@Override
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		EObject semanticElement = getSemanticElement(semanticAdapter);
		if (MapEditPart.MODEL_ID.equals(diagramKind)
				&& TigerstripeVisualIDRegistry
						.getDiagramVisualID(semanticElement) != -1)
			return MapViewFactory.class;
		return null;
	}

	/**
	 * @generated
	 */
	@Override
	protected Class getNodeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		if (containerView == null)
			return null;
		IElementType elementType = getSemanticElementType(semanticAdapter);
		if (elementType != null
				&& !TigerstripeElementTypes.isKnownElementType(elementType))
			return null;
		EClass semanticType = getSemanticEClass(semanticAdapter);
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int nodeVID = TigerstripeVisualIDRegistry.getNodeVisualID(
				containerView, semanticElement, semanticType, semanticHint);
		switch (nodeVID) {
		case NamedQueryArtifactEditPart.VISUAL_ID:
			return NamedQueryArtifactViewFactory.class;
		case NamedQueryArtifactStereotypesEditPart.VISUAL_ID:
			return NamedQueryArtifactStereotypesViewFactory.class;
		case NamedQueryArtifactNamePackageEditPart.VISUAL_ID:
			return NamedQueryArtifactNamePackageViewFactory.class;
		case ExceptionArtifactEditPart.VISUAL_ID:
			return ExceptionArtifactViewFactory.class;
		case ExceptionArtifactStereotypesEditPart.VISUAL_ID:
			return ExceptionArtifactStereotypesViewFactory.class;
		case ExceptionArtifactNamePackageEditPart.VISUAL_ID:
			return ExceptionArtifactNamePackageViewFactory.class;
		case ManagedEntityArtifactEditPart.VISUAL_ID:
			return ManagedEntityArtifactViewFactory.class;
		case ManagedEntityArtifactStereotypesEditPart.VISUAL_ID:
			return ManagedEntityArtifactStereotypesViewFactory.class;
		case ManagedEntityArtifactNamePackageEditPart.VISUAL_ID:
			return ManagedEntityArtifactNamePackageViewFactory.class;
		case NotificationArtifactEditPart.VISUAL_ID:
			return NotificationArtifactViewFactory.class;
		case NotificationArtifactStereotypesEditPart.VISUAL_ID:
			return NotificationArtifactStereotypesViewFactory.class;
		case NotificationArtifactNamePackageEditPart.VISUAL_ID:
			return NotificationArtifactNamePackageViewFactory.class;
		case DatatypeArtifactEditPart.VISUAL_ID:
			return DatatypeArtifactViewFactory.class;
		case DatatypeArtifactStereotypesEditPart.VISUAL_ID:
			return DatatypeArtifactStereotypesViewFactory.class;
		case DatatypeArtifactNamePackageEditPart.VISUAL_ID:
			return DatatypeArtifactNamePackageViewFactory.class;
		case EnumerationEditPart.VISUAL_ID:
			return EnumerationViewFactory.class;
		case EnumerationStereotypesEditPart.VISUAL_ID:
			return EnumerationStereotypesViewFactory.class;
		case EnumerationNamePackageEditPart.VISUAL_ID:
			return EnumerationNamePackageViewFactory.class;
		case EnumerationBaseTypeEditPart.VISUAL_ID:
			return EnumerationBaseTypeViewFactory.class;
		case UpdateProcedureArtifactEditPart.VISUAL_ID:
			return UpdateProcedureArtifactViewFactory.class;
		case UpdateProcedureArtifactStereotypesEditPart.VISUAL_ID:
			return UpdateProcedureArtifactStereotypesViewFactory.class;
		case UpdateProcedureArtifactNamePackageEditPart.VISUAL_ID:
			return UpdateProcedureArtifactNamePackageViewFactory.class;
		case SessionFacadeArtifactEditPart.VISUAL_ID:
			return SessionFacadeArtifactViewFactory.class;
		case SessionFacadeArtifactStereotypesEditPart.VISUAL_ID:
			return SessionFacadeArtifactStereotypesViewFactory.class;
		case SessionFacadeArtifactNamePackageEditPart.VISUAL_ID:
			return SessionFacadeArtifactNamePackageViewFactory.class;
		case AssociationClassClassEditPart.VISUAL_ID:
			return AssociationClassClassViewFactory.class;
		case AssociationClassClassStereotypesEditPart.VISUAL_ID:
			return AssociationClassClassStereotypesViewFactory.class;
		case AssociationClassClassNamePackageEditPart.VISUAL_ID:
			return AssociationClassClassNamePackageViewFactory.class;
		case AttributeEditPart.VISUAL_ID:
			return AttributeViewFactory.class;
		case Attribute2EditPart.VISUAL_ID:
			return Attribute2ViewFactory.class;
		case Attribute3EditPart.VISUAL_ID:
			return Attribute3ViewFactory.class;
		case MethodEditPart.VISUAL_ID:
			return MethodViewFactory.class;
		case Attribute4EditPart.VISUAL_ID:
			return Attribute4ViewFactory.class;
		case Attribute5EditPart.VISUAL_ID:
			return Attribute5ViewFactory.class;
		case Method2EditPart.VISUAL_ID:
			return Method2ViewFactory.class;
		case LiteralEditPart.VISUAL_ID:
			return LiteralViewFactory.class;
		case Attribute6EditPart.VISUAL_ID:
			return Attribute6ViewFactory.class;
		case Method3EditPart.VISUAL_ID:
			return Method3ViewFactory.class;
		case Attribute7EditPart.VISUAL_ID:
			return Attribute7ViewFactory.class;
		case Method4EditPart.VISUAL_ID:
			return Method4ViewFactory.class;
		case NamedQueryArtifactAttributeCompartmentEditPart.VISUAL_ID:
			return NamedQueryArtifactAttributeCompartmentViewFactory.class;
		case ExceptionArtifactAttributeCompartmentEditPart.VISUAL_ID:
			return ExceptionArtifactAttributeCompartmentViewFactory.class;
		case ManagedEntityArtifactAttributeCompartmentEditPart.VISUAL_ID:
			return ManagedEntityArtifactAttributeCompartmentViewFactory.class;
		case ManagedEntityArtifactMethodCompartmentEditPart.VISUAL_ID:
			return ManagedEntityArtifactMethodCompartmentViewFactory.class;
		case NotificationArtifactAttributeCompartmentEditPart.VISUAL_ID:
			return NotificationArtifactAttributeCompartmentViewFactory.class;
		case DatatypeArtifactAttributeCompartmentEditPart.VISUAL_ID:
			return DatatypeArtifactAttributeCompartmentViewFactory.class;
		case DatatypeArtifactMethodCompartmentEditPart.VISUAL_ID:
			return DatatypeArtifactMethodCompartmentViewFactory.class;
		case EnumerationLiteralCompartmentEditPart.VISUAL_ID:
			return EnumerationLiteralCompartmentViewFactory.class;
		case UpdateProcedureArtifactAttributeCompartmentEditPart.VISUAL_ID:
			return UpdateProcedureArtifactAttributeCompartmentViewFactory.class;
		case SessionFacadeArtifactMethodCompartmentEditPart.VISUAL_ID:
			return SessionFacadeArtifactMethodCompartmentViewFactory.class;
		case AssociationClassClassAttributeCompartmentEditPart.VISUAL_ID:
			return AssociationClassClassAttributeCompartmentViewFactory.class;
		case AssociationClassClassMethodCompartmentEditPart.VISUAL_ID:
			return AssociationClassClassMethodCompartmentViewFactory.class;
		case AssociationStereotypesEditPart.VISUAL_ID:
			return AssociationStereotypesViewFactory.class;
		case AssociationNamePackageEditPart.VISUAL_ID:
			return AssociationNamePackageViewFactory.class;
		case AssociationAEndMultiplicityEditPart.VISUAL_ID:
			return AssociationAEndMultiplicityViewFactory.class;
		case AssociationZEndMultiplicityEditPart.VISUAL_ID:
			return AssociationZEndMultiplicityViewFactory.class;
		case AssociationAEndNameEditPart.VISUAL_ID:
			return AssociationAEndNameViewFactory.class;
		case AssociationZEndNameEditPart.VISUAL_ID:
			return AssociationZEndNameViewFactory.class;
		case SessionEmitsEditPart.VISUAL_ID:
			return SessionEmitsViewFactory.class;
		case SessionManagesEditPart.VISUAL_ID:
			return SessionManagesViewFactory.class;
		case QueryReturnsEditPart.VISUAL_ID:
			return QueryReturnsViewFactory.class;
		case SessionSupportsEditPart.VISUAL_ID:
			return SessionSupportsViewFactory.class;
		case SessionExposesEditPart.VISUAL_ID:
			return SessionExposesViewFactory.class;
		case DependencyNamePackageEditPart.VISUAL_ID:
			return DependencyNamePackageViewFactory.class;
		case DependencyStereotypesEditPart.VISUAL_ID:
			return DependencyStereotypesViewFactory.class;
		case ReferenceNameEditPart.VISUAL_ID:
			return ReferenceNameViewFactory.class;
		case ReferenceMultiplicityEditPart.VISUAL_ID:
			return ReferenceMultiplicityViewFactory.class;
		case AssociationClassAEndMultiplicityEditPart.VISUAL_ID:
			return AssociationClassAEndMultiplicityViewFactory.class;
		case AssociationClassZEndMultiplicityEditPart.VISUAL_ID:
			return AssociationClassZEndMultiplicityViewFactory.class;
		case AssociationClassAEndNameEditPart.VISUAL_ID:
			return AssociationClassAEndNameViewFactory.class;
		case AssociationClassZEndNameEditPart.VISUAL_ID:
			return AssociationClassZEndNameViewFactory.class;
		}
		return null;
	}

	/**
	 * @generated
	 */
	@Override
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		IElementType elementType = getSemanticElementType(semanticAdapter);
		if (elementType != null
				&& !TigerstripeElementTypes.isKnownElementType(elementType))
			return null;
		if (TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002
				.equals(elementType))
			return SessionFacadeArtifactEmittedNotificationsViewFactory.class;
		if (TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003
				.equals(elementType))
			return SessionFacadeArtifactManagedEntitiesViewFactory.class;
		if (TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004
				.equals(elementType))
			return NamedQueryArtifactReturnedTypeViewFactory.class;
		if (TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005
				.equals(elementType))
			return SessionFacadeArtifactNamedQueriesViewFactory.class;
		if (TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006
				.equals(elementType))
			return SessionFacadeArtifactExposedProceduresViewFactory.class;
		if (TigerstripeElementTypes.AbstractArtifactExtends_3007
				.equals(elementType))
			return AbstractArtifactExtendsViewFactory.class;
		if (TigerstripeElementTypes.AssociationClassAssociatedClass_3011
				.equals(elementType))
			return AssociationClassAssociatedClassViewFactory.class;
		if (TigerstripeElementTypes.AbstractArtifactImplements_3012
				.equals(elementType))
			return AbstractArtifactImplementsViewFactory.class;
		EClass semanticType = getSemanticEClass(semanticAdapter);
		if (semanticType == null)
			return null;
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int linkVID = TigerstripeVisualIDRegistry.getLinkWithClassVisualID(
				semanticElement, semanticType);
		switch (linkVID) {
		case AssociationEditPart.VISUAL_ID:
			return AssociationViewFactory.class;
		case DependencyEditPart.VISUAL_ID:
			return DependencyViewFactory.class;
		case ReferenceEditPart.VISUAL_ID:
			return ReferenceViewFactory.class;
		case AssociationClassEditPart.VISUAL_ID:
			return AssociationClassViewFactory.class;
		}
		return getUnrecognizedConnectorViewClass(semanticAdapter,
				containerView, semanticHint);
	}

	/**
	 * @generated
	 */
	private IElementType getSemanticElementType(IAdaptable semanticAdapter) {
		if (semanticAdapter == null)
			return null;
		return (IElementType) semanticAdapter.getAdapter(IElementType.class);
	}

	/**
	 * @generated
	 */
	private Class getUnrecognizedConnectorViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		// Handle unrecognized child node classes here
		return null;
	}

}
