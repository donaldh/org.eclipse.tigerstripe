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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

import org.eclipse.tigerstripe.generators.models.AssociationClassModel;
import org.eclipse.tigerstripe.generators.models.AssociationModel;


public class AssociationEndFacade extends AbstractComponentModel {

    private IAssociationEnd end;
    private AbstractClassModel parentModel;

	
	public AssociationEndFacade(){
		super();
	}
	
	public AssociationEndFacade(IAssociationEnd end) {
		build(end);
	}
	
	public void build(IAssociationEnd end) {
		super.build(end);
		this.end = end;
	}
	
	public IType getType(){
		return end.getType();
	}
	
	/**
	 * Get the thing that contains this attribute
	 * @return
	 */
	public AssociationModel getAssociation(){
		AssociationModel model ;
		if (this.getContainingAssociation() instanceof IAssociationClassArtifact){
			 model = new AssociationClassModel(this.getContainingArtifact());
			 model.setPluginRef(this.getPluginRef());
		} else {
			 model = new AssociationModel(this.getContainingAssociation());
			 model.setPluginRef(this.getPluginRef());
		}
		model.setPluginRef(getPluginRef());
		return model;
	}
	
	public EAggregationEnum getEAggregation() {
		return end.getAggregation();
	}

	public EChangeableEnum getChangeable() {
		return end.getChangeable();
	}

	public EMultiplicity getMultiplicity() {
		return end.getMultiplicity();
	}
	
	public java.lang.String getDocMultiplicity(){
    	EMultiplicity multi = this.getMultiplicity();
    	switch (multi) {
    	case ZERO:
    		return "0";
    	case ZERO_ONE:
    		return "0..1";
    	case ZERO_STAR:
    		return "0..*";
    	case STAR:
    		return "*";
    	default:
    		return "1";
    	}

    }
	
    public java.lang.String getLowerMultiplicity(){
    	EMultiplicity multi = this.getMultiplicity();
    	switch (multi) {
    	case ZERO:
    		return "0";
    	case ZERO_ONE:
    		return "0";
    	case ZERO_STAR:
    		return "0";
    	case STAR:
    		return "0";
    	default:
    		return "1";
    	}

    }
    
	public java.lang.String getUpperMultiplicity(){
    	EMultiplicity multi = this.getMultiplicity();
    	switch (multi) {
    	case ZERO:
    		return "0";
    	case ZERO_ONE:
    		return "1";
    	case ZERO_STAR:
    		return "unbounded";
    	case STAR:
    		return "unbounded";
    	case ONE:
    		return "1";
    	case ONE_STAR:
    		return "unbounded";
    	default:
    		return "unbounded";
    	}
	    }

	
	public java.lang.String getUpperMultiplicity(String type){
		String maxString = "";
		if (type.equals("xml")){
			maxString = "unbounded";
		} else {
			maxString = "-1";
		}
    	EMultiplicity multi = this.getMultiplicity();
    	switch (multi) {
    	case ZERO:
    		return "0";
    	case ZERO_ONE:
    		return "1";
    	case ZERO_STAR:
    		return maxString;
    	case STAR:
    		return maxString;
    	case ONE:
    		return "1";
    	case ONE_STAR:
    		return maxString;
    	default:
    		return maxString;
    	}
	    }



	public boolean isNavigable() {
		return end.isNavigable();
	}

	public boolean isOrdered() {
		return end.isOrdered();
	}
	
	public boolean isOptional() {
		return this.getLowerMultiplicity().equals("0");
	}
	
	public boolean isRequired() {
		
		return !isOptional();
	}
	
	public boolean isField(){
		return false;
	}

	public IAbstractArtifact getContainingAssociation() {
		return this.end.getContainingAssociation();
	}
	
	public IAbstractArtifact getContainingArtifact() {
		return this.end.getContainingAssociation();
	}
	
	public IRelationship getContainingRelationship() {
		return this.end.getContainingRelationship();
	}


	public boolean isUnique() {
		return this.end.isUnique();
	}

	
	public AbstractClassModel getTypeModel() {
		if (getType().isArtifact() && !getType().getFullyQualifiedName().startsWith("primitive") ){
    		AbstractClassModel type = ModelFactory.getInstance().getModel(getType().getArtifact());
    		type.setPluginRef(this.getPluginRef());
    		return type;
		}
		//AbstractClassModel type = new AbstractClassModel();
		return null;
	}
	
	public AssociationEndFacade getOtherEnd(){
		AssociationEndFacade oEnd = new AssociationEndFacade( (IAssociationEnd) this.end.getOtherEnd());
		oEnd.setPluginRef(this.getPluginRef());
		oEnd.setParentModel(this.getParentModel());
		return oEnd;
		
	}
    
	
	
	public boolean isComposition(){
		return this.end.getAggregation().equals(EAggregationEnum.COMPOSITE);		
	}
	
	public boolean isAggregation() {
		return this.end.getAggregation().equals(EAggregationEnum.SHARED);
	}
	
	public boolean isOne2One(){
		boolean one = this.end.getMultiplicity().equals(EMultiplicity.ZERO_ONE) ||
			this.end.getMultiplicity().equals(EMultiplicity.ONE);
		boolean otherOne = getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ZERO_ONE) ||
			getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ONE);;
		return one && otherOne;
	}
	
    public boolean isOne2Many(){
    	boolean one = this.end.getMultiplicity().equals(EMultiplicity.ZERO_ONE) ||
			this.end.getMultiplicity().equals(EMultiplicity.ONE);
    	boolean otherMany = getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ZERO_STAR) ||
    	    getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ONE_STAR) ||
    	    getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.STAR);
        	
    	return one && otherMany;
	}
	
    public boolean isMany(){
    	boolean many = this.getMultiplicity().equals(EMultiplicity.ZERO_STAR) ||
    		this.getMultiplicity().equals(EMultiplicity.ONE_STAR) ||
    		this.getMultiplicity().equals(EMultiplicity.STAR);
    	return many;
	}
    
    
    public boolean isMany2One(){
    	boolean many = this.end.getMultiplicity().equals(EMultiplicity.ZERO_STAR) ||
    		this.end.getMultiplicity().equals(EMultiplicity.ONE_STAR) ||
    		this.end.getMultiplicity().equals(EMultiplicity.STAR);
    	boolean otherOne = getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ZERO_ONE) ||
		getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ONE);;
    	return many & otherOne;
	}
    
    public boolean isMany2Many(){
    	boolean many = this.end.getMultiplicity().equals(EMultiplicity.ZERO_STAR) ||
    		this.end.getMultiplicity().equals(EMultiplicity.ONE_STAR) ||
    		this.end.getMultiplicity().equals(EMultiplicity.STAR);
    	boolean otherMany = getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ZERO_STAR) ||
	    	getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.ONE_STAR) ||
	    	getOtherEnd().getAssociationEnd().getMultiplicity().equals(EMultiplicity.STAR);
    	return many & otherMany;
	}
    
    

	public IAssociationEnd getAssociationEnd() {
		return end;
	}

    
    public boolean isAssociationEnd(){
    	return true;
    }
    


	/**
	 * @return the parentModel
	 */
	public AbstractClassModel getParentModel() {
		return parentModel;
	}

	/**
	 * @param parentModel the parentModel to set
	 */
	public void setParentModel(AbstractClassModel parentModel) {
		this.parentModel = parentModel;
	}

		
	@Override
	public IPluginConfig getPluginRef() {
		return this.getParentModel().getPluginRef();
	}
    
}