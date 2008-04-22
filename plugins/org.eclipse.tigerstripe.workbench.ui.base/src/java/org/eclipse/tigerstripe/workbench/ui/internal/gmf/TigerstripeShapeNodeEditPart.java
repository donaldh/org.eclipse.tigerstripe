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
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.notation.View;

public abstract class TigerstripeShapeNodeEditPart extends ShapeNodeEditPart {

	public TigerstripeShapeNodeEditPart(View arg0) {
		super(arg0);
	}

	/**
	 * A property for this artifact has been changed thru an action of the user
	 * 
	 * @param propertyKey
	 */
	public void artifactPropertyChanged(String propertyKey, String oldValue,
			String newValue) {
		handleArtifactPropertyChanged(propertyKey, oldValue, newValue);
	}

	protected abstract void handleArtifactPropertyChanged(String propertyKey,
			String oldValue, String newValue);
}
