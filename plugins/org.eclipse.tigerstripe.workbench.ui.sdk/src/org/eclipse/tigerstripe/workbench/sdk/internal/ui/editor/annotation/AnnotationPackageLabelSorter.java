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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPackageLabelContribution;

public class AnnotationPackageLabelSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Name";
    /**
     * @param column
     * @param dir
     */
    public AnnotationPackageLabelSorter(int dir) {
        super();
        this.column = "Name";
        this.dir = dir;
    }
    
    public AnnotationPackageLabelSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	AnnotationPackageLabelContribution p1;
    	AnnotationPackageLabelContribution p2;
    	if (e1 != null && e1 instanceof AnnotationPackageLabelContribution && 
    			e2 != null && e2 instanceof AnnotationPackageLabelContribution){
    		p1 = (AnnotationPackageLabelContribution) e1;
    		p2 = (AnnotationPackageLabelContribution) e2;

    		if (this.column.equals("Name")){
    			returnValue = p1.getName().compareToIgnoreCase(
    					p2.getName());

    		} else if (this.column.equals("Namespace")){
    			returnValue = p1.getUri().compareToIgnoreCase(
    					p2.getUri());
    		} else if (this.column.equals("Contributor")){
    			returnValue = p1.getContributor().compareToIgnoreCase(
    					p2.getContributor());
    		} 
    		if (this.dir == SWT.DOWN) {
    			returnValue = returnValue * -1;
    		}
    	}
        return returnValue;
    }
    
    

}