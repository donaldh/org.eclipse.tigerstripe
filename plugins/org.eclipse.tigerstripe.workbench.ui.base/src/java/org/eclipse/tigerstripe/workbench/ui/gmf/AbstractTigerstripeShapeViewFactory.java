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
package org.eclipse.tigerstripe.workbench.ui.gmf;

import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.notation.View;

public abstract class AbstractTigerstripeShapeViewFactory extends
		AbstractShapeViewFactory {

	// Bug #799
	@Override
	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);
		InitialDiagramPrefs.setDefaultFillColor(view);
		InitialDiagramPrefs.setDefaultFontStyle(view);
	}

}
