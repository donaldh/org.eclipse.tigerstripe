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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.gmf.TigerstripeConnectionViewFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceAEndMultiplicityLowerBoEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceNamePackageArtifactNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceZEndMultiplicityLowerBoEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceVisualIDRegistry;

/**
 * @generated
 */
public class AssociationInstanceViewFactory extends
		TigerstripeConnectionViewFactory {

	/**
	 * @generated
	 */
	@Override
	protected List createStyles(View view) {
		List styles = new ArrayList();
		styles.add(NotationFactory.eINSTANCE.createRoutingStyle());
		styles.add(NotationFactory.eINSTANCE.createFontStyle());
		styles.add(NotationFactory.eINSTANCE.createLineStyle());
		return styles;
	}

	/**
	 * @generated
	 */
	@Override
	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		if (semanticHint == null) {
			semanticHint = InstanceVisualIDRegistry
					.getType(org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceEditPart.VISUAL_ID);
			view.setType(semanticHint);
		}
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
				index, persisted);
		if (!InstanceMapEditPart.MODEL_ID.equals(InstanceVisualIDRegistry
				.getModelID(containerView))) {
			EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE
					.createEAnnotation();
			shortcutAnnotation.setSource("Shortcut"); //$NON-NLS-1$
			shortcutAnnotation.getDetails().put(
					"modelID", InstanceMapEditPart.MODEL_ID); //$NON-NLS-1$
			view.getEAnnotations().add(shortcutAnnotation);
		}
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						InstanceVisualIDRegistry
								.getType(AssociationInstanceNamePackageArtifactNameEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						InstanceVisualIDRegistry
								.getType(AssociationInstanceAEndMultiplicityLowerBoEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						InstanceVisualIDRegistry
								.getType(AssociationInstanceAEndNameEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						InstanceVisualIDRegistry
								.getType(AssociationInstanceZEndMultiplicityLowerBoEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						InstanceVisualIDRegistry
								.getType(AssociationInstanceZEndNameEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
	}

}
