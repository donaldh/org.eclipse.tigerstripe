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
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.artifactMetadata;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ModelComponentIconProviderContribution;

public class ComponentIconSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Artifact Type";
    /**
     * @param column
     * @param dir
     */
    public ComponentIconSorter(int dir) {
        super();
        this.column = "Artifact Type";
        this.dir = dir;
    }
    
    public ComponentIconSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	ModelComponentIconProviderContribution p1;
    	ModelComponentIconProviderContribution p2;
    	if (e1 != null && e1 instanceof ModelComponentIconProviderContribution && 
    			e2 != null && e2 instanceof ModelComponentIconProviderContribution){
    		p1 = (ModelComponentIconProviderContribution) e1;
    		p2 = (ModelComponentIconProviderContribution) e2;

    		if (this.column.equals("Artifact Type")){
    			returnValue = p1.getArtifactType().compareToIgnoreCase(
    					p2.getArtifactType());
    		} else if (this.column.equals("Provider Class")){
        			returnValue = p1.getProvider().compareToIgnoreCase(
        					p2.getProvider());
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