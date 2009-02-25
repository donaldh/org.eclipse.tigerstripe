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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution;

public class AnnotationSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Name";
    /**
     * @param column
     * @param dir
     */
    public AnnotationSorter(int dir) {
        super();
        this.column = "Name";
        this.dir = dir;
    }
    
    public AnnotationSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	AnnotationTypeContribution p1;
    	AnnotationTypeContribution p2;
    	if (e1 != null && e1 instanceof AnnotationTypeContribution && 
    			e2 != null && e2 instanceof AnnotationTypeContribution){
    		p1 = (AnnotationTypeContribution) e1;
    		p2 = (AnnotationTypeContribution) e2;

    		if (this.column.equals("Name")){
    			returnValue = p1.getName().compareToIgnoreCase(
    					p2.getName());
    		} else if (this.column.equals("eClass")){
        			returnValue = p1.getEClass().compareToIgnoreCase(
        					p2.getEClass());
    		} else if (this.column.equals("Namespace")){
    			returnValue = p1.getNamespace().compareToIgnoreCase(
    					p2.getNamespace());
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