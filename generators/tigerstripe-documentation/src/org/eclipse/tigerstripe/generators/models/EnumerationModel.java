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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;


public class EnumerationModel extends AbstractClassModel {
	
	private IEnumArtifact enumArtifact;
	
	public EnumerationModel() {
	}

	public EnumerationModel(IAbstractArtifact artifact) {
		build(artifact);
	}
	
	protected void build(IAbstractArtifact artifact){
		super.build(artifact);
		this.enumArtifact = (IEnumArtifact) artifact;
	}
	
	public String getBaseTypeStr(){
		return enumArtifact.getBaseTypeStr();
	}
	
    //	========= Additional features for UML Enumerations =====//

	/** 
	 * rename & recast for getLabels 
	 * 
	 */
	public Collection getLiterals(){
		Collection<LabelFacade> facades = new ArrayList<LabelFacade>();
		for (ILiteral literal : getArtifact().getLiterals(true)){
			
			LabelFacade facade = new LabelFacade( literal );
			facades.add(facade);
		}
		return facades;
	}
	
	
}
