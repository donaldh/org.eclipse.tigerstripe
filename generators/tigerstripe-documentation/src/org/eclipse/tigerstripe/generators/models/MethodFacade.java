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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public class MethodFacade extends AbstractComponentModel {

	
	private IMethod method;
	
	private AbstractClassModel parentModel;

	public MethodFacade(){
		super();
	}

	public MethodFacade(IMethod method) {
		build(method);
	}

	public void build(IMethod method) {
		super.build(method);
		this.method = method;
	}
	
	public Collection<IArgument> getArguments() {
		return method.getArguments();
	}

	public String getMethodId() {
		return method.getMethodId();
	}

	public boolean isMethod() {
		return true;
	}
	
	public IType getReturnType() {
		return method.getReturnType();
	}

	public boolean isVoid() {
		return method.isVoid();
	}

	public IAbstractArtifact getContainingArtifact() {
		return this.method.getContainingArtifact();
	}
	
	public String getDefaultReturnValue() {
		return this.method.getDefaultReturnValue();
	}

	public boolean isAbstract() {
		return this.method.isAbstract();
	}

	public boolean isOrdered() {
		return this.method.isOrdered();
	}

	public boolean isUnique() {
		return this.method.isUnique();
	}
    
	public String getReturnName(){
		return getMethodReturnName();
	}
	
	public String getMethodReturnName() {
		return this.method.getReturnName();
	}

	public Collection<IStereotypeInstance> getReturnStereotypeInstances() {
		return this.method.getReturnStereotypeInstances();
	}
	
	
	/**
	 * Return true if this has "multiplicity" anything other than SINGLE
	 */
	public boolean returnIsMany(){
		
		boolean many = this.getReturnType().getTypeMultiplicity().equals(EMultiplicity.ZERO_STAR) ||
		this.getReturnType().getTypeMultiplicity().equals(EMultiplicity.ONE_STAR) ||
		this.getReturnType().getTypeMultiplicity().equals(EMultiplicity.STAR);
		
		return many;
	}
	
	
	
	public String getPackage(){
		return getContainingArtifact().getPackage();
	}
	
	
	
	
	public boolean returnParameterHasStereotype( String stereotypeName){
		if(this.getReturnStereotypeInstances() != null){
		Collection<IStereotypeInstance> stereos = this.getReturnStereotypeInstances();
		for (IStereotypeInstance stereo : stereos){
			if (stereo.getName().equals(stereotypeName)) {
					return true;
			}
		}
		return false;
		}
		return false;
	}


	
	public boolean isReturnTypePresent(){
		return !isVoid();
	}
	
	
	
	public  Collection getExceptions() {
		Collection<AbstractClassModel> facades = new ArrayList<AbstractClassModel>();
		for (IException exception : method.getExceptions()){
			AbstractClassModel facade = ModelFactory.getInstance().getModel(exception);
			if (facade != null){
				facade.setPluginRef(this.getPluginRef());
				facades.add(facade);
			}
		}
		return facades;
	}
	
	public boolean isExceptionsPresent(){
		if (method.getExceptions().size() == 0){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Formatted String list of the exceptions
	 * @return
	 */
	public String getExceptionList(){
		String outString = "";
		String comma = " ";
		Iterator it = method.getExceptions().iterator();
		while (it.hasNext()){
		    IException exception = (IException) it.next();
			
			outString = outString + comma+exception.getName() ;
			if (it.hasNext()){
				outString = outString +"\n"; 
			}
			comma = ",";
		}
		
	
		return outString;
	}
	
	/**
	 * Formatted string of the method signature
	 * @return
	 */
	public String getSignature(){
		String sig = "";
		sig = method.getName()+"(";
		sig = sig + getTypedArgumentList();
		sig = sig+")";
		return sig;
	}

	/** 
	 * rename & recast for getIextArguments 
	 * 
	 */
	public Collection getParameters(){
		Collection<ArgumentFacade> facades = new ArrayList<ArgumentFacade>();
		for (IArgument arg : getArguments()){
			ArgumentFacade facade = new ArgumentFacade( arg);
			facade.setPluginRef(this.getPluginRef());
			facade.setParentModel(this.getParentModel());
			facades.add(facade);
		}
		return facades;
	}
	
	/**
	 * Formatted list of the arguments & types
	 * @return
	 */
	public String getTypedArgumentList(){
		String argList = "";
		String comma = " ";
		int arg =0;
		for (IArgument argument : method.getArguments()){
			argList = argList + comma + argument.getType().getFullyQualifiedName()
				+ " arg"+arg;
			comma = "        ,\n";
			arg++;
		}
		return argList;
	}
	
	
	public AbstractClassModel getOwner(){
		AbstractClassModel model = ModelFactory.getInstance().getModel(method.getContainingArtifact());
		model.setPluginRef(this.getPluginRef());
		return model;
	}
	
	
	public AbstractClassModel getReturnTypeModel(){
		if (getReturnType().isArtifact() 
				&& !getReturnType().getFullyQualifiedName().startsWith("primitive")
				&& ! getReturnType().isPrimitive()){
    		AbstractClassModel model = ModelFactory.getInstance().getModel(getReturnType().getArtifact());
    		model.setPluginRef(this.getPluginRef());
    		return model;
    	}
		return null;
	}
	

	public boolean hasReturnExactStereotype( String stereotypeName){

		Collection<IStereotypeInstance> stereos = getReturnStereotypeInstances();
		for (IStereotypeInstance stereo :stereos){
			if (stereo.getName().equals(stereotypeName)) {
				return true;
			}
		}
		return false;
	}

	/**
	* "Getter" for upperMultiplicity.
	* No comment found in UML.
	*/
	public java.lang.String getReturnUpperMultiplicity(){
		EMultiplicity multi = this.getReturnType().getTypeMultiplicity();
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
	
	public java.lang.String getReturnMultiplicity(){
		EMultiplicity multi = this.getReturnType().getTypeMultiplicity();
		switch (multi) {
		case ZERO:
			return "0";
		case ZERO_ONE:
			return "0..1";
		case ZERO_STAR:
			return "0..*";
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
	
	public boolean isReturnMultiMany(){
		EMultiplicity multi = this.getReturnType().getTypeMultiplicity();
		switch (multi) {
		case ZERO:
			return false;
		case ZERO_ONE:
			return false;
		case ZERO_STAR:
			return true;
		case STAR:
			return true;
		case ONE:
			return false;
		case ONE_STAR:
			return true;
		default:
			return false;
		}
	}
	
	
	public java.lang.String getReturnUpperMultiplicity(String type){
		String maxString = "";
		if (type.equals("xml")){
			maxString = "unbounded";
		} else {
			maxString = "-1";
		}
    	EMultiplicity multi = this.getReturnType().getTypeMultiplicity();
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
	
	
	/**
	* "Getter" for lowerMultiplicity.
	* No comment found in UML.
	*/
    public java.lang.String getReturnLowerMultiplicity(){
    	EMultiplicity multi = this.getReturnType().getTypeMultiplicity();
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
