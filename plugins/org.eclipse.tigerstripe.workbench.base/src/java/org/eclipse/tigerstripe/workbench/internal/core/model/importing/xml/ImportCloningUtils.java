/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml;

import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

/**
 * NOTE cloning CANNOT handle Annotations!
 */
public class ImportCloningUtils {
	
	private AnnotationHelper helper = AnnotationHelper.getInstance();

	public static IField cloneField(IField inField){
		
		IField outField = (IField) inField.getContainingArtifact().makeField();
		cloneModelComponentFields(inField, outField);
		outField.setType(inField.getType());
		outField.setDefaultValue(inField.getDefaultValue());
		outField.setOptional(inField.isOptional());
		outField.setOrdered(inField.isOrdered());
		outField.setUnique(inField.isUnique());
		outField.setReadOnly(inField.isReadOnly());
		return outField;
	}

	public static ILiteral cloneLiteral(ILiteral inLiteral){

		ILiteral outLiteral = (ILiteral) inLiteral.getContainingArtifact().makeLiteral();
		cloneModelComponentFields(inLiteral, outLiteral);
		outLiteral.setType(inLiteral.getType());
		outLiteral.setValue(inLiteral.getValue());
		
		return outLiteral;
	}
	
	public static IMethod cloneMethod(IMethod inMethod){

		IMethod outMethod = (IMethod) inMethod.getContainingArtifact().makeMethod();
		cloneModelComponentFields(inMethod, outMethod);
		
		outMethod.setAbstract(inMethod.isAbstract());
		outMethod.setDefaultReturnValue(inMethod.getDefaultReturnValue());
		outMethod.setOptional(inMethod.isOptional());
		outMethod.setOrdered(inMethod.isOrdered());
		outMethod.setReturnName(inMethod.getReturnName());
		outMethod.setReturnType(inMethod.getReturnType());
		outMethod.setUnique(inMethod.isUnique());
		outMethod.setVoid(inMethod.isVoid());
		
		
		// Arguments
		// These support Annotations - so need to be done carefully
		for (IArgument inArgument : inMethod.getArguments()){
			outMethod.addArgument(cloneArgument(inArgument));
		}
		
		// Exceptions
		outMethod.setExceptions(inMethod.getExceptions());
		
		return outMethod;
	}
	
	public static IArgument cloneArgument(IArgument inArgument){
		IArgument outArgument = (IArgument) inArgument.getContainingMethod().makeArgument();
		outArgument.setName(inArgument.getName());
		outArgument.setComment(inArgument.getComment());
		outArgument.setDefaultValue(inArgument.getDefaultValue());
		outArgument.setDirection(inArgument.getDirection());
		outArgument.setOrdered(inArgument.isOrdered());
		outArgument.setUnique(inArgument.isUnique());
		outArgument.setType(inArgument.getType());
		
		cloneSterotypes(inArgument, outArgument);
		return outArgument;
	}
	

	private static void cloneModelComponentFields(IModelComponent inComponent, IModelComponent outComponent){
		cloneSterotypes(inComponent, outComponent);
		outComponent.setName(inComponent.getName());
		outComponent.setVisibility(inComponent.getVisibility());
		outComponent.setComment(inComponent.getComment());
	}
	
	private static void cloneSterotypes(IStereotypeCapable inCapable, IStereotypeCapable outCapable){
		for (IStereotypeInstance instance : inCapable.getStereotypeInstances()){
			outCapable.addStereotypeInstance(instance);
		}
	}
	
}