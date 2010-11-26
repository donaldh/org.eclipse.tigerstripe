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

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
		
		// N.M Bugzilla 328947 - [TS Explorer] Double clicking on containers should expand them
		if (getContext() !=null) {
			ISelection selection = getContext().getSelection();
			if (selection instanceof IStructuredSelection) {
				Object firstElement = ((IStructuredSelection)selection).getFirstElement();
				if (!((firstElement instanceof IPackageFragmentRoot) || (firstElement instanceof IProject))) {
					actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, ((TSNavigateActionGroup) actionGroup).getOpenAction());			
				}
			}
		}
		
	}
}
