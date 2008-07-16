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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Yuri Strot
 *
 */
public class PropertiesSection extends AbstractPropertySection {

    private PropertyTree tree;
	  
    /* (non-Javadoc)
     * @see org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    public void createControls(final Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory()
                .createFlatFormComposite(parent);

        tree = new PropertyTree();
        Control control = tree.create(composite);

		FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.height = 100;
        data.width = 100;
        
        control.setLayoutData(data);
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
    
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        updateView(selection);
    }
	
	protected void updateView() {
		ISelection selection = AnnotationUIPlugin.getManager().getSelection();
		if (selection != null)
			updateView(selection);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateView(selection);
	}
	
	protected void updateView(final ISelection selection) {
		Annotation annotation = getAnnotation(selection);
		tree.setContent(annotation == null ? null : annotation.getContent());
	}
	
	private Annotation getAnnotation(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			return (Annotation )((IStructuredSelection)selection).getFirstElement();
		}
		return null;
	}

}
