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

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

public class BaseActionProvider extends CommonActionProvider {

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		doInit(getSite());
	}

	protected IWorkbenchPartSite getSite() {
		ICommonViewerWorkbenchSite site = (ICommonViewerWorkbenchSite) getActionSite()
				.getViewSite();
		return site.getSite();
	}

	protected ISelectionProvider getSelectionProvider() {
		return getActionSite().getViewSite().getSelectionProvider();
	}

	protected void doInit(IWorkbenchPartSite site) {
	}

}
