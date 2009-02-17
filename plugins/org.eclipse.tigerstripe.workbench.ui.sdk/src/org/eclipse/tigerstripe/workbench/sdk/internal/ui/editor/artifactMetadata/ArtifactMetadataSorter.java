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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactMetadataContribution;

public class ArtifactMetadataSorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "Artifact Type";
    /**
     * @param column
     * @param dir
     */
    public ArtifactMetadataSorter(int dir) {
        super();
        this.column = "Artifact Type";
        this.dir = dir;
    }
    
    public ArtifactMetadataSorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    
    public int compare(Viewer viewer, Object e1, Object e2) {
    	int returnValue = 0;
    	ArtifactMetadataContribution p1;
    	ArtifactMetadataContribution p2;
    	if (e1 != null && e1 instanceof ArtifactMetadataContribution && 
    			e2 != null && e2 instanceof ArtifactMetadataContribution){
    		p1 = (ArtifactMetadataContribution) e1;
    		p2 = (ArtifactMetadataContribution) e2;

    		if (this.column.equals("Artifact Type")){
    			returnValue = p1.getArtifactType().compareToIgnoreCase(
    					p2.getArtifactType());
    		} else if (this.column.equals("User Label")){
        			returnValue = p1.getUserLabel().compareToIgnoreCase(
        					p2.getUserLabel());
    		} else if (this.column.equals("Fields")){
    			returnValue = ((Boolean) p1.isHasFields()).compareTo(
    					((Boolean)p2.isHasFields()));
    		} else if (this.column.equals("Methods")){
    			returnValue = ((Boolean) p1.isHasMethods()).compareTo(
    					((Boolean)p2.isHasMethods()));
    		} else if (this.column.equals("Literals")){
    			returnValue = ((Boolean) p1.isHasLiterals()).compareTo(
    					((Boolean)p2.isHasLiterals()));
    		} else if (this.column.equals("Icon")){
    			returnValue = p1.getIcon().compareToIgnoreCase(
    					p2.getIcon());
    		} else if (this.column.equals("Icon-new")){
    			returnValue = p1.getIcon_new().compareToIgnoreCase(
    					p2.getIcon_new());
    		} else if (this.column.equals("Icon-Grey")){
    			returnValue = p1.getIcon_gs().compareToIgnoreCase(
    					p2.getIcon_gs());
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