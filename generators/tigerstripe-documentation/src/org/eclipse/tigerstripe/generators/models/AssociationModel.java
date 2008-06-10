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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;


public class AssociationModel extends AbstractClassModel {
	
	private IAssociationArtifact association;
	
	public AssociationModel() {
		super();
	}

	public AssociationModel(IAbstractArtifact artifact) {
		build(artifact);
	}

	protected void build(IAbstractArtifact artifact){
	    super.build(artifact);
	    this.association = (IAssociationArtifact) artifact;
	}
	
	public AssociationEndFacade getAEnd(){
		AssociationEndFacade end = new AssociationEndFacade(this.association.getAEnd());
		end.setParentModel(this);
		return end;
	}
	
	public AssociationEndFacade getZEnd(){
		AssociationEndFacade end = new AssociationEndFacade(this.association.getZEnd());
		end.setParentModel(this);
		return end;
	}
	
    //	========= Additional features for UML Associations =====//
	
	public Collection getAssociationEnds(){
		ArrayList ends= new ArrayList();
		ends.add( getAEnd() );
		ends.add( getZEnd() );
		return ends;
	}
	
	public AssociationEndFacade getAssociationEndA(){
		return getAEnd();
	}
	
	public AssociationEndFacade getAssociationEndB(){
		return getZEnd();
	}
    
}