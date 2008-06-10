/******************************************************************************* 
 * 
 * Copyright (c) 2008 Cisco Systems, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Cisco Systems, Inc. - dkeysell
********************************************************************************/
package org.eclipse.tigerstripe.generators.models;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;

public class SessionModel extends AbstractClassModel {
	
	private ISessionArtifact sessionArtifact;
	
	public SessionModel(){}
	
	public SessionModel(IAbstractArtifact artifact){
		build(artifact);
	}
	
	protected void build(IAbstractArtifact artifact){
		super.build(artifact);
		this.sessionArtifact = (ISessionArtifact) artifact;
	}
	

    public boolean isInterface(){
    	return true;
    }

}
