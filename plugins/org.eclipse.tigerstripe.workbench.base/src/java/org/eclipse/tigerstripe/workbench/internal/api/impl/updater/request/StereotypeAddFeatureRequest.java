/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IStereotypeAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public class StereotypeAddFeatureRequest extends BaseArtifactElementRequest
		implements IStereotypeAddFeatureRequest, IMethodChangeRequest {

	private String featureId;
	
	/**
	 * Generally sets the name of the thing to change.
	 * In the case of a METHOD this should be a LabelString
	 */
	private String capableName;
	
	/** 
	 * Only used for Argument Changes
	 */
	private Integer argumentPosition;
	
	public Integer getArgumentPosition() {
		return argumentPosition;
	}

	public void setArgumentPosition(Integer argumentPosition) {
		this.argumentPosition = argumentPosition;
	}

	private ECapableClass capableClass;

	public ECapableClass getCapableClass() {
		return capableClass;
	}

	public void setCapableClass(ECapableClass capableClass) {
		this.capableClass = capableClass;
	}

	public String getCapableName() {
		return capableName;
	}

	public void setCapableName(String capableName) {
		this.capableName = capableName;
	}

	private IStereotypeInstance featureValue;

	// These methods etc are necessary to support the IMethodChangeRequest interface
	// because we may change the method Signature....
	// by adding argument Stereotypes....
	
	private String methodLabelAfterChange;
	
	public String getMethodLabelAfterChange(){
		return this.methodLabelAfterChange;
	}

	protected void setMethodLabelAfterChange(String methodLabelAfterChange) {
		this.methodLabelAfterChange = methodLabelAfterChange;
	}
	

	public void setMethodLabelBeforeChange(String methodLabel) {
		if (getCapableClass().equals(ECapableClass.METHOD) ||
				getCapableClass().equals(ECapableClass.METHODARGUMENT) ||
				getCapableClass().equals(ECapableClass.METHODRETURN)){
			setCapableName(methodLabel);
			
		}
		// Set the after so that it is not null
		// It will get upadted if necessary
		setMethodLabelAfterChange(methodLabel);
	}	
	
	
	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			// TODO Make sure we can find the capable element
			
			if (getCapableClass().equals(ECapableClass.ARTIFACT)){
				return art != null;
			} else if (getCapableClass().equals(ECapableClass.FIELD)){
				Collection<IField> fields = art.getFields();
				for (IField field : fields ){
					boolean match = field.getName().equals(getCapableName());
					if (match) 
						return true;
				}
			} else if (getCapableClass().equals(ECapableClass.METHOD) || getCapableClass().equals(ECapableClass.METHODRETURN)){
				for (IMethod method : art.getMethods()) {
					if (method.getLabelString().equals(getCapableName()))
						return true;
				}
			} else if (getCapableClass().equals(ECapableClass.METHODARGUMENT) ){
				Collection<IMethod> methods = art.getMethods();
				for (IMethod method : methods ){
					if (method.getLabelString().equals(getCapableName())){ 
						if (method.getArguments().size() > getArgumentPosition()){
							return true;
						}
						
					}
				}
			} else if (getCapableClass().equals(ECapableClass.LITERAL)){
				Collection<ILiteral> literals = art.getLiterals();
				for (ILiteral literal : literals ){
					boolean match = literal.getName().equals(getCapableName());
					if (match) 
						return true;
				}
			} else if (getCapableClass().equals(ECapableClass.AEND)){
				if (art instanceof AssociationArtifact){
					AssociationArtifact assoc  = (AssociationArtifact) art;
					return (null != assoc.getAEnd());
				} else 
					return false;


			} else if (getCapableClass().equals(ECapableClass.ZEND)){
				if (art instanceof AssociationArtifact){
					AssociationArtifact assoc  = (AssociationArtifact) art;
					return (null != assoc.getZEnd());
				} else 
					return false;

			}
			return false;
		} catch (TigerstripeException t) {
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);

		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());
		
		
		IStereotypeInstance instance = (IStereotypeInstance) this.featureValue;
		if (getCapableClass().equals(ECapableClass.ARTIFACT)){
			addInstanceToCapable(art,instance);
		} else if (getCapableClass().equals(ECapableClass.FIELD)){
			Collection<IField> fields = art.getFields();
			for (IField field : fields ){
				boolean match = field.getName().equals(getCapableName());
				if (match){ 
					addInstanceToCapable(field,instance);
					setFeatureId(IModelChangeDelta.ATTRIBUTE);
				}
			}
		} else if (getCapableClass().equals(ECapableClass.METHOD)){
			Collection<IMethod> methods = art.getMethods();
			for (IMethod method : methods ){
				if (method.getLabelString().equals(getCapableName())){
					addInstanceToCapable(method,instance);
					setFeatureId(IModelChangeDelta.METHOD);
					// NOTE THIS MAY CHANGE THE METHOD LABEL
					setMethodLabelAfterChange(method.getLabelString());
				}
			}
		} else if (getCapableClass().equals(ECapableClass.METHODRETURN)){
			Collection<IMethod> methods = art.getMethods();
			for (IMethod method : methods ){
				if (method.getLabelString().equals(getCapableName())){ 
					addReturnInstanceToCapable(method,instance);
					setFeatureId(IModelChangeDelta.METHOD);
					// NOTE THIS MAY CHANGE THE METHOD LABEL
					setMethodLabelAfterChange(method.getLabelString());
				}
			}
		} else if (getCapableClass().equals(ECapableClass.METHODARGUMENT)){
			Collection<IMethod> methods = art.getMethods();
			for (IMethod method : methods ){
				if (method.getLabelString().equals(getCapableName())){ 
					Collection<IArgument> arguments = method.getArguments();
					IArgument[] argumentArray = arguments.toArray(new IMethod.IArgument[0]);
					IArgument argument = argumentArray[getArgumentPosition()];
					addInstanceToCapable(argument,instance);
					setFeatureId(IModelChangeDelta.METHOD);
					// NOTE THIS MAY CHANGE THE METHOD LABEL
					setMethodLabelAfterChange(method.getLabelString());
				}
			}
		} else if (getCapableClass().equals(ECapableClass.LITERAL)){
			Collection<ILiteral> literals = art.getLiterals();
			for (ILiteral literal : literals ){
				boolean match = literal.getName().equals(getCapableName());
				if (match){ 
					addInstanceToCapable(literal,instance);
					setFeatureId(IModelChangeDelta.LITERAL);
				}
			}
		} else if (getCapableClass().equals(ECapableClass.AEND)){
				AssociationArtifact assoc  = (AssociationArtifact) art;
					addInstanceToCapable(assoc.getAEnd(),instance);

		} else if (getCapableClass().equals(ECapableClass.ZEND)){
			AssociationArtifact assoc  = (AssociationArtifact) art;
				addInstanceToCapable(assoc.getZEnd(),instance);
		}
		
		art.doSave(null);
		
	}

	private void addInstanceToCapable(IStereotypeCapable capable,IStereotypeInstance instance){
		if ( capable.hasStereotypeInstance(instance.getName())){
			IStereotypeInstance oldInstance = capable.getStereotypeInstanceByName(instance.getName());
			capable.removeStereotypeInstance(oldInstance);
		}
		capable.addStereotypeInstance(instance);
	}
	
	private void addReturnInstanceToCapable(IMethod capable,IStereotypeInstance instance){
		if ( capable.hasReturnStereotypeInstance(instance.getName())){
			IStereotypeInstance oldInstance = capable.getReturnStereotypeInstanceByName(instance.getName());
			capable.removeStereotypeInstance(oldInstance);
		}
		capable.addReturnStereotypeInstance(instance);
	}
	
	public void setFeatureId(String featureID) {
		this.featureId = featureID;
	}

	public void setFeatureValue(IStereotypeInstance value) {
		this.featureValue = value;
	}

	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.ADD);

		try {
			IModelComponent comp = getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			delta
					.setAffectedModelComponentURI((URI) comp
							.getAdapter(URI.class));

			delta.setFeature(featureId);
			delta.setNewValue(featureValue);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return ModelChangeDelta.UNKNOWNDELTA;
		}

		return delta;
	}


}
