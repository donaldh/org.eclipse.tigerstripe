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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;

public class FieldFacade extends AbstractComponentModel {

	private IField field;
	private AbstractClassModel parentModel;
	
	public FieldFacade(){
		super();
	}
	
	public FieldFacade(IField field) {
		build(field);
	}
	
	public void build(IField field) {
		super.build(field);
		this.field = field;
	}
	
	public IType getType() {
		return field.getType();
	}


	public boolean isOptional() {
		return this.getLowerMultiplicity().equals("0");
	}

	public boolean isReadOnly() {
		return field.isReadOnly();
	}
	
	public IAbstractArtifact getContainingArtifact() {
		return this.field.getContainingArtifact();
	}
	

	public boolean isOrdered() {
		return this.field.isOrdered();
	}

	public boolean isUnique() {
		return this.field.isUnique();
	}
	
	
	public String getFullyQualifiedName(){
		
		return this.getContainingArtifact().getFullyQualifiedName() + "." + super.getName();

	}

	
	/**
	 * Return true if this has "multiplicity" anything other than SINGLE
	 */
	public boolean isMany(){
		
		boolean many = this.getType().getTypeMultiplicity().equals(EMultiplicity.ZERO_STAR) ||
		this.getType().getTypeMultiplicity().equals(EMultiplicity.ONE_STAR) ||
		this.getType().getTypeMultiplicity().equals(EMultiplicity.STAR);
		
		return many;
	}
    
    
    public AbstractClassModel getTypeModel(){
    	if (getType().isArtifact()
    			&& !getType().getFullyQualifiedName().startsWith("primitive") 
    			&& !getType().isPrimitive() ){
    		AbstractClassModel model = ModelFactory.getInstance().getModel(getType().getArtifact());
    		model.setPluginRef(this.getPluginRef());
    		return model;
    	}
		return null;
	}  
	
	/**
	 * The opposite of isOptional
	 * 
	 * @return
	 */
	public boolean isRequired(){
		return !isOptional();
	}	
	
	
	/**
	 * String based on the value of "defaultValue" stereotype
	 * 
	 * @return
	 */
	public String getDefaultValue(){
		if (isDefaultValuePresent()){
			return field.getDefaultValue();
		} else
			return null;
	}
	
	/**
	 * String based on the presence of "defaultValue" stereotype
	 * 
	 * @return
	 */
	public boolean isDefaultValuePresent(){
		return (field.getDefaultValue() != null );
	}

	/**
	* "Getter" for lowerMultiplicity.
	* No comment found in UML.
	*/
    public java.lang.String getLowerMultiplicity(){
    	EMultiplicity multi = this.getType().getTypeMultiplicity();
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

	/**
	* "Getter" for upperMultiplicity.
	* No comment found in UML.
	* 
	* NOTE - Better the use the typed version below!
	*/
	public java.lang.String getUpperMultiplicity(){
    	EMultiplicity multi = this.getType().getTypeMultiplicity();
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
	
	public java.lang.String getMultiplicity(){
		EMultiplicity multi = this.getType().getTypeMultiplicity();
    	switch (multi) {
    	case ZERO:
    		return "0";
    	case ZERO_ONE:
    		return "0..1";
    	case ZERO_STAR:
    		return "*";
    	case STAR:
    		return "*";
    	case ONE:
    		return "1";
    	case ONE_STAR:
    		return "1..*";
    	default:
    		return "unknown";
    	}
	}
	
	
	/**
	* "Getter" for upperMultiplicity.
	* No comment found in UML.
	*/
	public java.lang.String getUpperMultiplicity(String type){
		String maxString = "";
		if (type.equals("xml")){
			maxString = "unbounded";
		} else {
			maxString = "-1";
		}
    	EMultiplicity multi = this.getType().getTypeMultiplicity();
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

	public boolean isField(){
		return true;
	}
	
	
	public String toString(){
		return this.getClass().getName()+"["+field.getName()+"]";
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

	public AbstractClassModel getOwner(){
		AbstractClassModel model = ModelFactory.getInstance().getModel(field.getContainingArtifact());
		model.setPluginRef(this.getPluginRef());
		return model;
	}
}
