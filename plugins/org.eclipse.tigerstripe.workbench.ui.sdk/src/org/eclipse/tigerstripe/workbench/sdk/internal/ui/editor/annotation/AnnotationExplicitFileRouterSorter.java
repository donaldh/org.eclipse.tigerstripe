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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationExplicitFileRouterContribution;

public class AnnotationExplicitFileRouterSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "nsURI";
    /**
     * @param column
     * @param dir
     */
    public AnnotationExplicitFileRouterSorter(int dir) {
        super();
        this.column = "nsURI";
        this.dir = dir;
    }
    
    public AnnotationExplicitFileRouterSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	AnnotationExplicitFileRouterContribution p1;
    	AnnotationExplicitFileRouterContribution p2;
    	if (e1 != null && e1 instanceof AnnotationExplicitFileRouterContribution && 
    			e2 != null && e2 instanceof AnnotationExplicitFileRouterContribution){
    		p1 = (AnnotationExplicitFileRouterContribution) e1;
    		p2 = (AnnotationExplicitFileRouterContribution) e2;

    		if (this.column.equals("nsURI")){
    			returnValue = p1.getNsURI().compareToIgnoreCase(
    					p2.getNsURI());
    		} else if (this.column.equals("path")){
        			returnValue = p1.getPath().compareToIgnoreCase(
        					p2.getPath());
    		} else if (this.column.equals("eclass")){
    			returnValue = p1.getEClass().compareToIgnoreCase(
    					p2.getEClass());
    		} else if (this.column.equals("epackage")){
    			returnValue = p1.getEPackage().compareToIgnoreCase(
    					p2.getEPackage());
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