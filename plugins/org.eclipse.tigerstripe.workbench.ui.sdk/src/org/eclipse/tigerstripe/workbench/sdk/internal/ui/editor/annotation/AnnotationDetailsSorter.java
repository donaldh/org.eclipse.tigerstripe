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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution.Target;

public class AnnotationDetailsSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Type";
    /**
     * @param column
     * @param dir
     */
    public AnnotationDetailsSorter(int dir) {
        super();
        this.column = "Type";
        this.dir = dir;
    }
    
    public AnnotationDetailsSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	Target p1;
    	Target p2;
    	if (e1 != null && e1 instanceof Target && 
    			e2 != null && e2 instanceof Target){
    		p1 = (Target) e1;
    		p2 = (Target) e2;

    		if (this.column.equals("Type")){
    			returnValue = p1.getType().compareToIgnoreCase(
    					p2.getType());
    		} else if (this.column.equals("Unique")){
        			returnValue = p1.getUnique().compareToIgnoreCase(
        					p2.getUnique());
    		}
    		if (this.dir == SWT.DOWN) {
    			returnValue = returnValue * -1;
    		}
    	}
        return returnValue;
    }
    
    

}