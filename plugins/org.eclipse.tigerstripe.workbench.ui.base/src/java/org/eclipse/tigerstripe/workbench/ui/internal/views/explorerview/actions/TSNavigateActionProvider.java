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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.ICommonActionConstants;

/**
 * Navigate action provider provides 'Open' action for Tigerstripe elements.
 * 
 * @see TSNavigateActionGroup
 */
public class TSNavigateActionProvider extends ActionGroupWrapper {

	@Override
	protected ActionGroup createGroup(CommonNavigator navigator) {
		return new TSNavigateActionGroup(navigator);
	}

	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN,
				((TSNavigateActionGroup) actionGroup).getOpenAction());
	}
}
