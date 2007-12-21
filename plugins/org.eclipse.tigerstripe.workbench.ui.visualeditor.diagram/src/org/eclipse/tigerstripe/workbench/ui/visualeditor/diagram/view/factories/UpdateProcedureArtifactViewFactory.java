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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.gmf.AbstractTigerstripeShapeViewFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;

/**
 * @generated NOT
 */
public class UpdateProcedureArtifactViewFactory extends
		AbstractTigerstripeShapeViewFactory {

	/**
	 * @generated
	 */
	@Override
	protected List createStyles(View view) {
		List styles = new ArrayList();
		styles.add(NotationFactory.eINSTANCE.createFontStyle());
		styles.add(NotationFactory.eINSTANCE.createDescriptionStyle());
		styles.add(NotationFactory.eINSTANCE.createFillStyle());
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
			semanticHint = TigerstripeVisualIDRegistry
					.getType(org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart.VISUAL_ID);
			view.setType(semanticHint);
		}
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
				index, persisted);
		if (!MapEditPart.MODEL_ID.equals(TigerstripeVisualIDRegistry
				.getModelID(containerView))) {
			EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE
					.createEAnnotation();
			shortcutAnnotation.setSource("Shortcut"); //$NON-NLS-1$
			shortcutAnnotation.getDetails()
					.put("modelID", MapEditPart.MODEL_ID); //$NON-NLS-1$
			view.getEAnnotations().add(shortcutAnnotation);
		}
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						TigerstripeVisualIDRegistry
								.getType(UpdateProcedureArtifactStereotypesEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						TigerstripeVisualIDRegistry
								.getType(UpdateProcedureArtifactNamePackageEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
		getViewService()
				.createNode(
						semanticAdapter,
						view,
						TigerstripeVisualIDRegistry
								.getType(UpdateProcedureArtifactAttributeCompartmentEditPart.VISUAL_ID),
						ViewUtil.APPEND, true, getPreferencesHint());
	}

}
