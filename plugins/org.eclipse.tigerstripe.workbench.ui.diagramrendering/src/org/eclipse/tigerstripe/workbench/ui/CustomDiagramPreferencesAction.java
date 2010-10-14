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
package org.eclipse.tigerstripe.workbench.ui;

import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractGMFDiagramNode;
import org.eclipse.ui.IWorkbenchSite;

public class CustomDiagramPreferencesAction extends SelectionDispatchAction {

	protected CustomDiagramPreferencesAction(IWorkbenchSite site) {
		super(site);
	}

	@Override
	public void run(IStructuredSelection selection) {

		Object firstElement = selection.getFirstElement();
		if (!(firstElement instanceof AbstractGMFDiagramNode)) {
			return;
		}
		AbstractGMFDiagramNode cd = (AbstractGMFDiagramNode) firstElement;
		new CustomDiagramPreferencesDialog(getShell(), cd).open();
	}

}
