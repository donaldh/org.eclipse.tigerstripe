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
package org.eclipse.tigerstripe.annotation.ui.core.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationPropertiesSection extends AbstractPropertySection {
    
    protected void updateSection(Annotation annotation) {
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
    
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        updateSection(selection);
    }
	
	protected void updateSection(final ISelection selection) {
		Annotation annotation = getAnnotation(selection);
		if (annotation != null)
			updateSection(annotation);
	}
	
	private Annotation getAnnotation(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			return (Annotation )((IStructuredSelection)selection).getFirstElement();
		}
		return null;
	}

}
