/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.view.factories;

import java.util.List;

import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.AbstractTigerstripeLabelViewFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.PreferencesHelper;

public class AbstractAssociationViewFactory extends
		AbstractTigerstripeLabelViewFactory {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List createStyles(View view) {
		List styles = super.createStyles(view);
		styles.add(NotationFactory.eINSTANCE.createRoutingStyle());
		styles.add(NotationFactory.eINSTANCE.createLineStyle());
		return styles;
	}

	@Override
	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);
		PreferencesHelper.setRoutingStyle(view,
				PreferencesHelper.getStore(view));
	}

}
