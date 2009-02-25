/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.annotation;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPropertyProviderContribution;

public class AnnotationPropertyProviderSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Class";
    /**
     * @param column
     * @param dir
     */
    public AnnotationPropertyProviderSorter(int dir) {
        super();
        this.column = "Class";
        this.dir = dir;
    }
    
    public AnnotationPropertyProviderSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	AnnotationPropertyProviderContribution p1;
    	AnnotationPropertyProviderContribution p2;
    	if (e1 != null && e1 instanceof AnnotationPropertyProviderContribution && 
    			e2 != null && e2 instanceof AnnotationPropertyProviderContribution){
    		p1 = (AnnotationPropertyProviderContribution) e1;
    		p2 = (AnnotationPropertyProviderContribution) e2;

    		if (this.column.equals("Class")){
    			returnValue = p1.get_class().compareToIgnoreCase(
    					p2.get_class());
    		
    		} else if (this.column.equals("Priority")){
    			returnValue = p1.getPriority().compareToIgnoreCase(
    					p2.getPriority());
    		} else if (this.column.equals("Contributor")){
    			returnValue = p1.getContributor().toString().compareToIgnoreCase(
    					p2.getContributor().toString());
    		} 
    		if (this.dir == SWT.DOWN) {
    			returnValue = returnValue * -1;
    		}
    	}
        return returnValue;
    }
    
    

}