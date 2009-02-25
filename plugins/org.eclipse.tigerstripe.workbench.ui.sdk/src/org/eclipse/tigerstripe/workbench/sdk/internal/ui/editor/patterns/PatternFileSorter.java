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
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;

public class PatternFileSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "name";
    private ISDKProvider provider = null;
    /**
     * @param column
     * @param dir
     */
    public PatternFileSorter(int dir) {
        super();
        this.column = "Pattern File";
        this.dir = dir;
    }
    
    public PatternFileSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    public PatternFileSorter(ISDKProvider provider, int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
        this.provider = provider;
    }
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	PatternFileContribution p1;
    	PatternFileContribution p2;
    	if (e1 != null && e1 instanceof PatternFileContribution && 
    			e2 != null && e2 instanceof PatternFileContribution){
    		p1 = (PatternFileContribution) e1;
    		p2 = (PatternFileContribution) e2;

    		if (this.column.equals("Pattern File")){
    			returnValue = p1.getFileName().compareToIgnoreCase(
    					p2.getFileName());
    		} else if (this.column.equals("Validator Class")){
    			returnValue = p1.getValidatorClass().compareToIgnoreCase(
    					p2.getValidatorClass());
    		} else if (this.column.equals("Contributor")){
    			returnValue = p1.getContributor().toString().compareToIgnoreCase(
    					p2.getContributor().toString());
    		} else if (provider != null){
    			// Need to get the actual pattern!
    			IPattern pattern1 = provider.getPattern(p1.getContributor(), p1.getFileName());
    			IPattern pattern2 = provider.getPattern(p2.getContributor(), p2.getFileName());
    			if (pattern1 != null && pattern2 != null){
    				if (this.column.equals("Pattern Name")){
    					returnValue = pattern1.getName().compareToIgnoreCase(
    							pattern2.getName());
    				} else if (this.column.equals("UI Label")){
    					returnValue = pattern1.getUILabel().compareToIgnoreCase(
    							pattern2.getUILabel());
    				} else if (this.column.equals("Index")){
    					returnValue = ((Integer)pattern1.getIndex()).compareTo((Integer)pattern2.getIndex());
    				} else if (this.column.equals("Icon Path")){
    					returnValue = pattern1.getIconPath().compareToIgnoreCase(
    							pattern2.getIconPath());
    				}
    			}
    			
    		}
    		if (this.dir == SWT.DOWN) {
    			returnValue = returnValue * -1;
    		}
    	}
        return returnValue;
    }
    
    

}