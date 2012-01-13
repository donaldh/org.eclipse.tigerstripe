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
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.core.IExtendedMenuCreator;
import org.eclipse.tigerstripe.annotation.ui.core.IMenuProvider;
import org.eclipse.tigerstripe.annotation.ui.util.AnnotationGroup;


/**
 * @author Yuri Strot
 *
 */
public class CreateAnnotationAction extends DelegateAction implements IMenuProvider {
	
	private Object object;
	private List<Object> list;
	private MenuCreator menu;
	private TargetAnnotationType[] types;
	
	public CreateAnnotationAction() {
		super("Create");
		list = new ArrayList<Object>();
		menu = new MenuCreator(list);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.actions.DelegateAction#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	    super.selectionChanged(action, selection);
	    if (action.isEnabled()) {
	    	updateAnnotationsList();
	    	action.setMenuCreator(menu);
	    }
	    else {
	    	action.setMenuCreator(null);
	    }
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.actions.DelegateAction#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		super.run(action);
	}
	
	protected void updateAnnotationsList() {
    	list.clear();
    	list.add(new OpenAnnotationWizardAction(object, "Open Annotation Wizard..."));
    	list.add(new Separator());
    	
    	boolean addFirstSeparator = false;
    	
    	AnnotationGroup[] groups = AnnotationGroup.getGroups(types);
    	for (AnnotationGroup annotationGroup : groups) {
    		String name = annotationGroup.getName();
    		if (name != null) {
    			list.add(new CreateAnnotationGroupAction(
    					name, annotationGroup.getTypes()));
    			addFirstSeparator = true;
    		}
    		else {
        		if (addFirstSeparator)
                	list.add(new Separator());
                addFirstSeparator = true;
            	for (TargetAnnotationType type : annotationGroup.getTypes()) {
            		list.add(new CreateSpecificTypeAnnotationAction(type));
    			}
    		}
		}
	}
	
	@Override
	protected void adaptSelection(ISelection selection) {
		types = null;
		object = getSelected(selection);
		if (object != null) {
			types = AnnotationPlugin.getManager().getAnnotationTargets(object);
		}
		setEnabled(types != null && types.length > 0);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.IMenuProvider#getMenu(org.eclipse.jface.viewers.ISelection)
	 */
	public IExtendedMenuCreator getMenu(ISelection selection) {
		adaptSelection(selection);
		if (isEnabled()) {
			updateAnnotationsList();
			return menu;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.IMenuProvider#getBaseAction()
	 */
	public IAction getBaseAction() {
		return this;
	}

}
