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
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.decorator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DecoratorContribution;

public class DecoratorSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Decorator Class";
    /**
     * @param column
     * @param dir
     */
    public DecoratorSorter(int dir) {
        super();
        this.column = "Decorator Class";
        this.dir = dir;
    }
    
    public DecoratorSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	DecoratorContribution p1;
    	DecoratorContribution p2;
    	if (e1 != null && e1 instanceof DecoratorContribution && 
    			e2 != null && e2 instanceof DecoratorContribution){
    		p1 = (DecoratorContribution) e1;
    		p2 = (DecoratorContribution) e2;

    		if (this.column.equals("Decorator Class")){
    			returnValue = p1.getDecoratorClass().compareToIgnoreCase(
    					p2.getDecoratorClass());
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