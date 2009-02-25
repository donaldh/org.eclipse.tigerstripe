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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AuditContribution;

public class ArtifactIconSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Decorator Class";
    /**
     * @param column
     * @param dir
     */
    public ArtifactIconSorter(int dir) {
        super();
        this.column = "Decorator Class";
        this.dir = dir;
    }
    
    public ArtifactIconSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	AuditContribution p1;
    	AuditContribution p2;
    	if (e1 != null && e1 instanceof AuditContribution && 
    			e2 != null && e2 instanceof AuditContribution){
    		p1 = (AuditContribution) e1;
    		p2 = (AuditContribution) e2;

    		if (this.column.equals("Name")){
    			returnValue = p1.getName().compareToIgnoreCase(
    					p2.getName());
    		} else if (this.column.equals("Auditor Class")){
        			returnValue = p1.getAuditorClass().compareToIgnoreCase(
        					p2.getAuditorClass());
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