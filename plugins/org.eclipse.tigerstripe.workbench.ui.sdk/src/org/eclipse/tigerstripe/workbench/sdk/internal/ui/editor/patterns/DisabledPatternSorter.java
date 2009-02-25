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
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.patterns;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DisabledPatternContribution;

public class DisabledPatternSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Pattern Name";
    /**
     * @param column
     * @param dir
     */
    public DisabledPatternSorter(int dir) {
        super();
        this.column = "Pattern Name";
        this.dir = dir;
    }
    
    public DisabledPatternSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	DisabledPatternContribution p1;
    	DisabledPatternContribution p2;
    	if (e1 != null && e1 instanceof DisabledPatternContribution && 
    			e2 != null && e2 instanceof DisabledPatternContribution){
    		p1 = (DisabledPatternContribution) e1;
    		p2 = (DisabledPatternContribution) e2;

    		if (this.column.equals("Pattern Name")){
    			returnValue = p1.getDisabledPatternName().compareToIgnoreCase(
    					p2.getDisabledPatternName());
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