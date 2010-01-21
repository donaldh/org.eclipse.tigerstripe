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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ClassDiagramPartsUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl;

public class AssociationZEndParser extends
		TigerstripeFieldnameConstrainedFeatureParser {

	public AssociationZEndParser(EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {

		setCurrentMap(adapter);
		
		Association association = (Association) adapter
				.getAdapter(AssociationImpl.class);
		
		String printString = super.getPrintString(adapter, flags);
		
		String visibilityPrefix = ClassDiagramPartsUtils
			.visibilityPrefix(association.getAEndVisibility());
		
		String stereotypePref = "";
		if (!hideStereotypes()) {
			String stereoNames = association.getZEndStereotypeNames();
			if (stereoNames.length() > 0)
				stereotypePref =  stereoNames+ " ";
		}

		return  stereotypePref + visibilityPrefix + printString;
	}
	
}
