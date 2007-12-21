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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
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
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.AssociationInstanceAEndMultiplicityLowerBoViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.AssociationInstanceAEndNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.AssociationInstanceNamePackageArtifactNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.AssociationInstanceViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.AssociationInstanceZEndMultiplicityLowerBoViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.AssociationInstanceZEndNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.ClassInstanceNamePackageArtifactNameViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.ClassInstanceVariableCompartmentViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.ClassInstanceViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.InstanceMapViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories.VariableViewFactory;

/**
 * @generated
 */
public class InstanceViewProvider extends AbstractViewProvider {

	/**
	 * @generated
	 */
	@Override
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		EObject semanticElement = getSemanticElement(semanticAdapter);
		if (InstanceMapEditPart.MODEL_ID.equals(diagramKind)
				&& InstanceVisualIDRegistry.getDiagramVisualID(semanticElement) != -1)
			return InstanceMapViewFactory.class;
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
				&& !InstanceElementTypes.isKnownElementType(elementType))
			return null;
		EClass semanticType = getSemanticEClass(semanticAdapter);
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int nodeVID = InstanceVisualIDRegistry.getNodeVisualID(containerView,
				semanticElement, semanticType, semanticHint);
		switch (nodeVID) {
		case ClassInstanceEditPart.VISUAL_ID:
			return ClassInstanceViewFactory.class;
		case ClassInstanceNamePackageArtifactNameEditPart.VISUAL_ID:
			return ClassInstanceNamePackageArtifactNameViewFactory.class;
		case VariableEditPart.VISUAL_ID:
			return VariableViewFactory.class;
		case ClassInstanceVariableCompartmentEditPart.VISUAL_ID:
			return ClassInstanceVariableCompartmentViewFactory.class;
		case AssociationInstanceNamePackageArtifactNameEditPart.VISUAL_ID:
			return AssociationInstanceNamePackageArtifactNameViewFactory.class;
		case AssociationInstanceAEndMultiplicityLowerBoEditPart.VISUAL_ID:
			return AssociationInstanceAEndMultiplicityLowerBoViewFactory.class;
		case AssociationInstanceAEndNameEditPart.VISUAL_ID:
			return AssociationInstanceAEndNameViewFactory.class;
		case AssociationInstanceZEndMultiplicityLowerBoEditPart.VISUAL_ID:
			return AssociationInstanceZEndMultiplicityLowerBoViewFactory.class;
		case AssociationInstanceZEndNameEditPart.VISUAL_ID:
			return AssociationInstanceZEndNameViewFactory.class;
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
				&& !InstanceElementTypes.isKnownElementType(elementType))
			return null;
		EClass semanticType = getSemanticEClass(semanticAdapter);
		if (semanticType == null)
			return null;
		EObject semanticElement = getSemanticElement(semanticAdapter);
		int linkVID = InstanceVisualIDRegistry.getLinkWithClassVisualID(
				semanticElement, semanticType);
		switch (linkVID) {
		case AssociationInstanceEditPart.VISUAL_ID:
			return AssociationInstanceViewFactory.class;
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
