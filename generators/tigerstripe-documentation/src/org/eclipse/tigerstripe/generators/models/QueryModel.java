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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;


public class QueryModel extends AbstractClassModel {

	private IQueryArtifact queryArtifact;
	
	public QueryModel() {
	}
	
	public QueryModel(IAbstractArtifact artifact ){
		build(artifact);
	}

	protected void build(IAbstractArtifact artifact){
		super.build(artifact);
		this.queryArtifact = (IQueryArtifact) artifact;
	}
	
	public String getReturnedType(){
		if (this.queryArtifact.getReturnedType() != null ){
			AbstractClassModel acm = ModelFactory.getInstance().getModel(this.queryArtifact.getReturnedType().getArtifact());
			acm.setPluginRef(this.getPluginRef());
			return acm.getFullyQualifiedName();
		} else {
			return null;
		}
	}
	
}
