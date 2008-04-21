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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.OpenAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RefreshAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveAllAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.view.AnnotationsTable;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationsSection extends AbstractPropertySection implements IAnnotationListener, IRefactoringListener {
	
	private AnnotationsTable aTable;
	  
    /* (non-Javadoc)
     * @see org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    public void createControls(final Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory()
                .createFlatFormComposite(parent);
        FormData data = null;

        String tableLabelStr = null;
        CLabel tableLabel = null;
        if (tableLabelStr != null && tableLabelStr.length() > 0) {
            tableLabel = getWidgetFactory().createCLabel(composite,
                    tableLabelStr);
            data = new FormData();
            data.left = new FormAttachment(0, 0);
            data.top = new FormAttachment(0, 0);
            tableLabel.setLayoutData(data);
        }
        
        createAllAnnotations(composite);

        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        if (tableLabel == null) {
            data.top = new FormAttachment(0, 0);
        } else {
            data.top = new FormAttachment(tableLabel, 0, SWT.BOTTOM);
        }
        data.bottom = new FormAttachment(100, 0);
//        data.height = 100;
        data.width = 100;
        
        aTable.getControl().setLayoutData(data);

        setActionBars(aTabbedPropertySheetPage.getSite().getActionBars());
        
        addListeners();
	    updateTable();
    }
	
	protected void addListeners() {
		AnnotationUIPlugin.getManager().addRefactoringListener(this);
		AnnotationPlugin.getManager().addAnnotationListener(this);
	}
	
	protected void removeListeners() {
		AnnotationUIPlugin.getManager().removeRefactoringListener(this);
		AnnotationPlugin.getManager().removeAnnotationListener(this);
	}
    
    @Override
    public void dispose() {
        super.dispose();
        removeListeners();
    }
	
	private void createAllAnnotations(Composite parent) {
		aTable = new AnnotationsTable();
		aTable.createContent(parent);
		aTable.addToDoublClick(new OpenAnnotationAction());
	}

    /**
     * Sets and prepares the actionBars for this section
     *  
     * @param actionBars the action bars for this page
     * @see org.eclipse.gmf.runtime.common.ui.properties.TabbedPropertySheetPage#setActionBars(org.eclipse.ui.IActionBars)
     */   
    public void setActionBars(IActionBars actionBars) {
        
        actionBars.getMenuManager().removeAll();
        actionBars.getToolBarManager().removeAll();
        actionBars.getStatusLineManager().removeAll();
        
        actionBars.getToolBarManager().update(true);
        
		IToolBarManager toolbar = actionBars.getToolBarManager();
		toolbar.add(new RefreshAction(aTable.getViewer()));
		
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
	
	protected boolean isInitialized() {
		return aTable != null && aTable.isInitialized();
	}
	
	protected void updateTable() {
	    if (isInitialized())
	    	aTable.setInput(AnnotationPlugin.getManager().getAnnotations());
	}
	
	protected void refreshTable() {
	    if (isInitialized())
	    	aTable.refresh();
	}

	public void containerUpdated() {
		refreshTable();
    }

	public void refactoringPerformed(Map<URI, URI> changes) {
    }

	public void annotationAdded(Annotation annotation) {
		updateTable();
    }

	public void annotationsRemoved(Annotation[] annotations) {
		updateTable();
    }

	public void annotationsChanged(Annotation[] annotations) {
		updateTable();
	}

}
