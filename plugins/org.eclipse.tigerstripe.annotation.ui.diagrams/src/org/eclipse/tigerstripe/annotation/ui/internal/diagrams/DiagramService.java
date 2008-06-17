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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramListener;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramService;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Yuri Strot
 *
 */
public class DiagramService implements IDiagramService, IPartListener {
	
	private ListenerList listeners = new ListenerList();
	
	public DiagramService() {
		initialize();
	}
	
	protected void initialize() {
		addDaigramListener(new AnnotationDiagramBuilder());
		IWorkbenchWindow window = PlatformUI.getWorkbench(
			).getActiveWorkbenchWindow();
		if (window != null)
			window.getPartService().addPartListener(this);
	}
	
	protected void fireDaigramOpened(IDiagramWorkbenchPart editor) {
		Object[] objects = listeners.getListeners();
		for (Object object : objects) {
			IDiagramListener listener = (IDiagramListener)object;
			listener.diagramOpened(editor);
		}
	}
	
	protected void fireDaigramClosed(IDiagramWorkbenchPart editor) {
		Object[] objects = listeners.getListeners();
		for (Object object : objects) {
			IDiagramListener listener = (IDiagramListener)object;
			listener.diagramClosed(editor);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partActivated(IWorkbenchPart part) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof IDiagramWorkbenchPart)
			fireDaigramClosed((IDiagramWorkbenchPart)part);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partDeactivated(IWorkbenchPart part) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partOpened(IWorkbenchPart part) {
		if (part instanceof IDiagramWorkbenchPart)
			fireDaigramOpened((IDiagramWorkbenchPart)part);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramService#addDaigramListener(org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramListener)
	 */
	public void addDaigramListener(IDiagramListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramService#removeDaigramListener(org.eclipse.tigerstripe.annotation.ui.diagrams.IDiagramListener)
	 */
	public void removeDaigramListener(IDiagramListener listener) {
		listeners.remove(listener);
	}
	
	public void dispose() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null)
			window.getPartService().removePartListener(this);
	}

}
