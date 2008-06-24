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
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.util.AdaptableUtil;
import org.eclipse.ui.IFileEditorInput;


/**
 * @author Yuri Strot
 *
 */
public class CreateAnnotationAction extends DelegateAction {
	
	private Object object;
	private List<Object> list;
	private MenuCreator menu;
	private AnnotationType[] types;
	
	public CreateAnnotationAction() {
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
	}
	
	protected void updateAnnotationsList() {
    	list.clear();
    	list.add(new OpenAnnotationWizardAction(object, "Open Annotation Wizard..."));
    	list.add(new Separator());
    	
		for (int i = 0; i < types.length; i++) {
			list.add(new CreateSpecificTypeAnnotationAction(object, types[i]));
        }
	}
	
	@Override
	protected void adaptSelection(ISelection selection) {
		types = null;
		object = getSelected(selection);
		//TODO: remove this hack!!!
		if (object != null && !(object instanceof IFileEditorInput))
			types = AdaptableUtil.getTypes(object);
		setEnabled(types != null && types.length > 0);
	}

}
