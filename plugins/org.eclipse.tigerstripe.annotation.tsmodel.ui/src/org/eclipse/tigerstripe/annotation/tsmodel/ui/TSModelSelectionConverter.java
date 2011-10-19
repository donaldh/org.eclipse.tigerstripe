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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionConverter;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

/**
 * @author Yuri Strot
 * 
 */
public class TSModelSelectionConverter implements ISelectionConverter {

	public ISelection convert(IWorkbenchPart part, ISelection selection) {
		return null;
	}

	public void open(ISelection selection) {
		// System.out.println("Entered TSModelSelectionConverter.open(...):
		// selection: "+selection);
//		IModelComponent element = getElement(selection);
		Object element = getElement(selection);
		if (element != null) {
			openSelection(new StructuredSelection(element));
		}
	}

//	protected IModelComponent getElement(ISelection selection) {
	protected Object getElement(ISelection selection) {
		// System.out.println("Entered
		// TSModelSelectionConverter.getElement(...)");

		if (selection instanceof StructuredSelection) {
			Iterator<?> it = ((StructuredSelection) selection).iterator();
			while (it.hasNext()) {
				Object elem = (Object) it.next();
				if (elem instanceof IModelComponent || elem instanceof IDiagram) {
					// System.out.println("TSModelSelectionConverter.getElement(...):
					// element: "+elem);
//					return (IModelComponent) elem;
					return elem;
				}
			}
		}
		return null;
	}

	protected void openSelection(ISelection selection) {
		// System.out.println("Entered
		// TSModelSelectionConverter.openSelection(...)");

		IWorkbenchPage page = WorkbenchUtil.getPage();
		if (page != null) {
			try {
				IViewPart part = page
						.showView(TigerstripeExplorerPart.AEXPLORER_ID);
				if (part instanceof TigerstripeExplorerPart) {
					((TigerstripeExplorerPart) part).selectReveal(selection);
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		// if (site != null) {
		// IStructuredSelection sel = (IStructuredSelection)selection;
		// new OpenAction(site).run(sel);
		// }
	}

}
