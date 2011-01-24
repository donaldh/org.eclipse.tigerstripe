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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;

public class AssociationAEndParser extends AssociationEndParser {

	public AssociationAEndParser(EStructuralFeature feature) {
		super(feature);
	}

	@Override
	protected String getEndStereotypeNames(Association association) {
		return association.getAEndStereotypeNames();
	}

	@Override
	protected boolean isEndIsOrdered(Association association) {
		return association.isAEndIsOrdered();
	}

	@Override
	protected Visibility getEndVisibility(Association association) {
		return association.getAEndVisibility();
	}
}
