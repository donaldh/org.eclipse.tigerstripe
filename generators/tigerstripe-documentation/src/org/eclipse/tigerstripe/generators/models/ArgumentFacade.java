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



import java.util.Collection;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;



public class ArgumentFacade {

	private IArgument argument;
	private IPluginConfig pluginRef;
	private AbstractClassModel parentModel;


	public ArgumentFacade(){

	}

	public ArgumentFacade(IArgument argument) {
		this.argument = argument;
	}

	public IType getType() {
		return argument.getType();
	}
	

	public String getName() {
		return argument.getName();
	}


	public String getComment() {
		return argument.getComment();
	}


	public IMethod getContainingMethod() {
		return argument.getContainingMethod();
	}
	
	public IArgument getArgument(){
		return this.argument;
	}


	public IAbstractArtifact getContainingArtifact() {
		return argument.getContainingArtifact();
	}


	public String getDefaultValue() {
		return argument.getDefaultValue();
	}


	public boolean isOrdered() {
		return argument.isOrdered();
	}


	public boolean isUnique() {
		return argument.isUnique();
	}

	public boolean hasExactStereotype( String stereotypeName){
		Collection<IStereotypeInstance> stereos = getStereotypeInstances();
		for (IStereotypeInstance stereo : stereos ){
			if (stereo.getName().equals(stereotypeName)) {
				return true;
			}
		}
		return false;
	}
	
	public Collection<IStereotypeInstance> getStereotypeInstances() {
		return this.argument.getStereotypeInstances();
	}

	public void setPluginRef(IPluginConfig pluginRef) {
		this.pluginRef = pluginRef;
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

    public java.lang.String getMultiplicity(){
    	EMultiplicity multi = this.getType().getTypeMultiplicity();
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
    
	/**
	* "Getter" for upperMultiplicity.
	* No comment found in UML.
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
	
    public AbstractClassModel getTypeModel(){
    	if (getType().isArtifact()
    			&& !getType().getFullyQualifiedName().startsWith("primitive")
    			&& ! getType().isPrimitive()){
    		AbstractClassModel model = ModelFactory.getInstance().getModel(getType().getArtifact());
    		model.setPluginRef(this.getPluginRef());
    		return model;
    	}
		return null;
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
    
    public boolean isTypePrimitive(){
    	if(this.getType().isPrimitive() || this.getType().getFullyQualifiedName().startsWith("primitive") || this.getType().getFullyQualifiedName().equals("java.lang.String"))
    		return true;
    	else
    		return false;
    }
    
    public boolean isEnum(){
    	if(this.getType().isEnum())
    		return true;
    		else
    			return false;
    }
    

	public IPluginConfig getPluginRef() {
		return pluginRef;
	}
	
    



	public String getDocumentation(String leader, int lineLength, boolean htmlStyle){
		// split into lines and pre-pend the "leader" on each one.

		// Make the line max of lineLength Chars
		String newComment = "";
		if (getComment().length() == 0){
			return "";
		}
		String[] lines = getComment().split("\n");
		for (String line: lines){
			line = leader+line;
			while (line.length() > lineLength){
				String firstline = line.substring(0,lineLength);
				String firstLineFullWords = firstline.substring(0,firstline.lastIndexOf(" "));
				
				String remains = line.substring(firstLineFullWords.length()+1);
				newComment = newComment+leader+firstLineFullWords+"\n";
				line = leader+remains;
			}
			newComment = newComment+line;
		}
		return newComment;
	}
	
	public String getConfiguredProperty(String property){
		if (this.getPluginRef().getProperty(property) == null)
			return "";
		return (String) this.getPluginRef().getProperty(property).toString();
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
}
