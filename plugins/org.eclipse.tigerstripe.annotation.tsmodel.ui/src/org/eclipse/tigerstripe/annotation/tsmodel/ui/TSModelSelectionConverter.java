/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Worrell (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.tsmodel.ui;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.tigerstripe.annotation.tsmodel.TSModelURIConverter;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionConverter;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;


/**
 * @author Yuri Strot
 *
 */
public class TSModelSelectionConverter implements ISelectionConverter {

	public ISelection convert(IWorkbenchPart part, ISelection selection) {
		if (part instanceof TigerstripeExplorerPart)
		{
			TreeSelection ts = (TreeSelection)selection;
			Object firstElement = ts.getFirstElement();
			if(firstElement == null)
				return null;
			if(firstElement instanceof IModelComponent)
				return new StructuredSelection(firstElement);
			return new StructuredSelection(TSModelURIConverter.toModelComponent(firstElement));
		}

	    return null;
    }

	public void open(ISelection selection) {
		System.out.println("Entered TSModelSelectionConverter.open(...): selection: "+selection);
		IModelComponent element = getElement(selection);
		if (element != null) {
//			switch (element.getElementType()) {
//        		case IJavaElement.JAVA_MODEL:
//        		case IJavaElement.JAVA_PROJECT:
//        		case IJavaElement.PACKAGE_FRAGMENT:
//        		case IJavaElement.PACKAGE_FRAGMENT_ROOT:
//        			IResource resource = (IResource)Platform.getAdapterManager(
//        			     ).getAdapter(element, IResource.class);
//        			if (resource != null) {
//        				openResource(resource);
//            			break;
//        			}
//        			//if this resource do not open like resource,
//        			//try to open it like IJavaElement 
//            	default:
        			openSelection(new StructuredSelection(element));
//            }
		}
    }
	
//	protected void openResource(IResource resource) {
//		System.out.println("Entered TSModelSelectionConverter.openResource(...)");
//
//		IWorkbenchPage activePage = WorkbenchUtil.getPage();
//		if (activePage != null) {
//			IViewPart view;
//            try {
//	            view = activePage.showView(JavaUI.ID_PACKAGES);
//				if (view instanceof IPackagesViewPart) {
//					((IPackagesViewPart) view).selectAndReveal(resource);
//				}
//            }
//            catch (PartInitException e) {
//	            e.printStackTrace();
//            }
//		}
//	}
	
	protected IModelComponent getElement(ISelection selection) {
//		System.out.println("Entered TSModelSelectionConverter.getElement(...)");

		if (selection instanceof StructuredSelection) {
			Iterator<?> it = ((StructuredSelection)selection).iterator();
			while (it.hasNext()) {
	            Object elem = (Object) it.next();
	            if (elem instanceof IModelComponent)
	            {
//	            	System.out.println("TSModelSelectionConverter.getElement(...): element: "+elem);
	            	return (IModelComponent)elem;
	            }
            }
		}
		return null;
	}
	
	protected void openSelection(ISelection selection) {
//		System.out.println("Entered TSModelSelectionConverter.openSelection(...)");

		IWorkbenchSite site = WorkbenchUtil.getSite();
		IWorkbenchPage page = WorkbenchUtil.getPage();
		if(page != null)
		{
			try {
				IViewPart part = page.showView(TigerstripeExplorerPart.AEXPLORER_ID);
				if(part instanceof TigerstripeExplorerPart)
				{
					((TigerstripeExplorerPart)part).selectReveal(selection);
				}
			}
			catch (PartInitException e) {
				e.printStackTrace();
			}
		}
//		if (site != null) {
//			IStructuredSelection sel = (IStructuredSelection)selection;
//			new OpenAction(site).run(sel);
//		}
	}

}
