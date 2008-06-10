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
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public class LabelFacade extends AbstractComponentModel {
	
	private ILiteral label;
	private AbstractClassModel parentModel;
	
	public LabelFacade(){
	}
	
	public LabelFacade(ILiteral label) {
		build(label);
	}
	
	public void build(ILiteral label) {
		super.build(label);
		this.label = label;
	}
	
	public IType getIextType() {
		return label.getType();
	}


	public String getValue() {
		return label.getValue();
	}
	
	
	public IAbstractArtifact getContainingArtifact() {
		return this.label.getContainingArtifact();
	}


	
	public int getIndexValue() {
		int ret = -1;
		//Object o = this.findTaggedValue("EnumValue");
		Object o = getValue();
		if (o != null) {
			ret = Integer.parseInt(o.toString());
		}
		return ret;
	}
	
	public AbstractClassModel getOwner(){
		AbstractClassModel model = ModelFactory.getInstance().getModel(label.getContainingArtifact());
		model.setPluginRef(this.getPluginRef());
		return model;
	}
	
	public void setParentModel(AbstractClassModel parentModel) {
		this.parentModel = parentModel;
	}
}
