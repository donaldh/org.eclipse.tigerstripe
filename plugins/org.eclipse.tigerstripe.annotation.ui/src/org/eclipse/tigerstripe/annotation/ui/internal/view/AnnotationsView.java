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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.OpenAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RefreshAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveAllAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.util.AsyncExecUtil;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationsView extends ViewPart implements IAnnotationListener, IRefactoringListener {
	
	public static final String ID = "org.eclipse.tigerstripe.view.annotation";
	
	private AnnotationsTable aTable;

	@Override
    public void createPartControl(Composite parent) {
		aTable = new AnnotationsTable();
		aTable.createContent(parent);
		aTable.addToDoublClick(new OpenAnnotationAction());
		
		fillToolBar();
		fillMenu();
		
		addListeners();
		
		updateAnnotations();
    }
	
	protected void addListeners() {
		AnnotationPlugin.getManager().addRefactoringListener(this);
		AnnotationPlugin.getManager().addAnnotationListener(this);
		getSite().setSelectionProvider(aTable.getViewer());
	}
	
	protected void removeListeners() {
		getSite().setSelectionProvider(null);
		AnnotationPlugin.getManager().removeAnnotationListener(this);
		AnnotationPlugin.getManager().removeRefactoringListener(this);
	}
	
	protected void fillToolBar() {
		IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
		toolbar.add(new RefreshAction(aTable.getViewer()));
	}
	
	protected void fillMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
		
			public void menuAboutToShow(IMenuManager manager) {
				OpenAnnotationAction openAnnotationAction = new OpenAnnotationAction();
				openAnnotationAction.selectionChanged(openAnnotationAction,
				    aTable.getViewer().getSelection());
				if (openAnnotationAction.isEnabled())
					manager.add(openAnnotationAction);
				RemoveAction removeAction = new RemoveAction();
				if (removeAction.isEnabled())
					manager.add(removeAction);
				manager.add(new RemoveAllAnnotationAction());
			}
		
		});
		Control control = aTable.getViewer().getControl();
		Menu menu = menuMgr.createContextMenu(control);
		control.setMenu(menu);
	}
	
	private class RemoveAction extends Action {
		
		private Annotation annotation;
		
		public RemoveAction() {
			super("Remove");
			init();
		}
		
		private void init() {
			this.annotation = null;
			ISelection sel = aTable.getViewer().getSelection();
			if (sel instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection)sel;
				Object elem = ss.getFirstElement();
				if (elem instanceof Annotation) {
					Annotation annotation = (Annotation)elem;
					this.annotation = annotation;
				}
			}
			setEnabled(annotation != null);
		}
		
		
		@Override
		public void run() {
			new RemoveAnnotationAction(annotation).run();
		}
	}
	
	public void annotationAdded(Annotation annotation) {
		updateAnnotations();
	}
	
	public void annotationsRemoved(Annotation[] annotations) {
		updateAnnotations();
	}

	public void annotationsChanged(Annotation[] annotations) {
		updateAnnotations();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener#annotationLoaded(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	public void annotationsLoaded(Annotation[] annotations) {
		updateAnnotations();
	}
	
	protected void updateAnnotations() {
		AsyncExecUtil.run(aTable.getViewer().getControl(), new Runnable() {
			
			public void run() {
				aTable.setInput(AnnotationPlugin.getManager().getLoadedAnnotations());
			}
		
		});
	}
	
	@Override
	public void dispose() {
	    super.dispose();
	    removeListeners();
	}

	@Override
    public void setFocus() {
    }

	public void containerUpdated() {
		AsyncExecUtil.run(aTable.getViewer().getControl(), new Runnable() {
		
			public void run() {
				aTable.refresh();
			}
		
		});
    }

	public void refactoringPerformed(Map<URI, URI> changes) {
    }

}
