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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;



public class ExceptionModel extends AbstractClassModel {

	private IExceptionArtifact exceptionArtifact;
	
	public ExceptionModel() {
	}

	
	public ExceptionModel(IAbstractArtifact artifact ){
		build(artifact);
	}
	
	protected void build(IAbstractArtifact artifact){
		super.build(artifact);
		this.exceptionArtifact = (IExceptionArtifact) artifact;
	}
    
    public AbstractClassModel getGeneralization(){
    	IExceptionArtifact bob = this.exceptionArtifact;
    	if (bob.getExtendedArtifact() != null){
			AbstractClassModel acm = ModelFactory.getInstance().getModel(bob.getExtendedArtifact());
			acm.setPluginRef(this.getPluginRef());
			if(acm.getFullyQualifiedName().equals("java.lang.Exception"))
			return null;
			else
			return acm;
		} else {
			return null; 
		}	 
    }
    
}

