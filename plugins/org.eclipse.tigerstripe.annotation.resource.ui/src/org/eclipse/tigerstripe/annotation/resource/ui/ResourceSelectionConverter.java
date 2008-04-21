/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.resource.ui;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionConverter;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.ui.part.ISetSelectionTarget;

/**
 * @author Yuri Strot
 *
 */
public class ResourceSelectionConverter implements ISelectionConverter {

	public ISelection convert(IWorkbenchPart part, ISelection selection) {
	    return null;
    }

	public void open(ISelection selection) {
		if (isResourceSelection(selection)) {
			IWorkbenchPage activePage = WorkbenchUtil.getPage();
			if (activePage != null) {
				IViewPart view;
	            try {
		            view = activePage.showView(ProjectExplorer.VIEW_ID);
					if (view instanceof ISetSelectionTarget) {
						((ISetSelectionTarget) view).selectReveal(selection);
					}
	            }
	            catch (PartInitException e) {
		            e.printStackTrace();
	            }
			}
		}
    }
	
	private boolean isResourceSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Iterator<?> it = ((IStructuredSelection)selection).iterator();
			while (it.hasNext()) {
	            Object elem = it.next();
	            if (!(elem instanceof IResource))
	            	return false;
            }
			return true;
		}
		return false;
	}

}
