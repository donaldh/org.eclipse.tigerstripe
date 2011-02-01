/*******************************************************************************
 * Copyright (c) 2010 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ClassDiagramPartsUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl;

public abstract class AssociationEndParser extends
		TigerstripeFieldnameConstrainedFeatureParser {

	public AssociationEndParser(EStructuralFeature feature) {
		super(feature);
	}

	protected abstract String getEndStereotypeNames(Association association);

	protected abstract Visibility getEndVisibility(Association association);

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {

		setCurrentMap(adapter);

		Association association = (Association) adapter
				.getAdapter(AssociationImpl.class);

		StringBuilder result = new StringBuilder();
		if (!hideStereotypes()) {
			String stereoNames = getEndStereotypeNames(association);
			if (stereoNames.length() > 0) {
				result.append(stereoNames);
				result.append(" ");
			}
		}

		// visibilityPrefix
		result.append(ClassDiagramPartsUtils
				.visibilityPrefix(getEndVisibility(association)));

		result.append(super.getPrintString(adapter, flags));

		return result.toString();
	}

	@Override
	public boolean isAffectingEvent(Object event, int flags) {
		if (event instanceof Notification) {
			Object obj = ((Notification) event).getFeature();
			if (VisualeditorPackage.eINSTANCE.getAssociation_AEndIsOrdered()
					.equals(obj)
					|| VisualeditorPackage.eINSTANCE
							.getAssociation_ZEndIsOrdered().equals(obj))
				return true;
		}
		return super.isAffectingEvent(event, flags);
	}
}
