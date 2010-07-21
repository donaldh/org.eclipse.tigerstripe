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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

/**
 * Actions wrapper.
 */
public abstract class ActionGroupWrapper extends CommonActionProvider {

	protected ActionGroup actionGroup;

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		ICommonViewerSite viewSite = aSite.getViewSite();
		if (viewSite instanceof ICommonViewerWorkbenchSite) {
			ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite) viewSite;
			IWorkbenchPart part = workbenchSite.getPart();
			if (part instanceof CommonNavigator) {
				actionGroup = createGroup((CommonNavigator) part);
			}
		}
	}

	protected abstract ActionGroup createGroup(CommonNavigator navigator);

	public void fillActionBars(IActionBars actionBars) {
		actionGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) {
		actionGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) {
		actionGroup.setContext(context);
	}

	public ActionContext getContext() {
		return actionGroup.getContext();
	}

	public void updateActionBars() {
		actionGroup.updateActionBars();
	}
}
