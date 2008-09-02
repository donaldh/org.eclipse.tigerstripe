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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;

public class MethodAddFeatureRequest extends MethodSetRequest
		implements IMethodAddFeatureRequest {
	
	private int argumentPosition = 0;
	
	public void setArgumentPosition(int position) {
		this.argumentPosition = position;
		
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);

		IAbstractArtifact art = mgrSession
		.getArtifactByFullyQualifiedName(getArtifactFQN());

		boolean needSave = false;
		if (art != null) {
			for (IMethod method : art.getMethods()) {
				if (((IMethod) method).getLabelString().equals(
						methodLabelBeforeChange)) {
					iMethod = (IMethod) method;
					methodURI = (URI) iMethod.getAdapter(URI.class);

					if (EXCEPTIONS_FEATURE.equals(featureId)){
						Collection<IException> startList = new ArrayList<IException>();
						startList.addAll(iMethod.getExceptions());
						if (newValue.equals("")){
							// This is a *remove*
							if (startList.contains(oldValue)){
								startList.remove(oldValue);
							}
						} else {
							// This is an *add*
							IException newOne = iMethod.makeException();
							newOne.setFullyQualifiedName(newValue);
							startList.add(newOne);
						}
						iMethod.setExceptions(startList);
						needSave = true;
					} else if (featureId.startsWith("argument_")){
						Collection<IArgument> startList = new ArrayList<IArgument>();
						startList.addAll(iMethod.getArguments());						
						IArgument[] arguments = startList.toArray(new IArgument[0]);
						IArgument argumentToChange;
						if (this.argumentPosition > arguments.length || arguments.length == 0 ){
							// This is a new argument
							 argumentToChange = iMethod.makeArgument();
							 // give it a default type 
							 IType defaultType = iMethod.makeType();
							 defaultType.setFullyQualifiedName("String");
							 argumentToChange.setType(defaultType);
							 startList.add(argumentToChange);
						} else {
							// This is a change to an existing argument
							argumentToChange = arguments[this.argumentPosition];
						}
						if (newValue == null){
							// This is shorthand for a REMOVE argument
							startList.remove(argumentToChange);
						}
						
						if (featureId.endsWith("name")){						
								argumentToChange.setName(newValue);
						}
						
						
						if (featureId.endsWith("type")){
							IType type = argumentToChange.getType();
							if (type == null){
								type = iMethod.makeType();
							}
							type.setFullyQualifiedName(newValue);
							argumentToChange.setType(type);
						}
						if (featureId.endsWith("defaultValue")){
							argumentToChange.setDefaultValue(newValue);
						}
						if (featureId.endsWith("isOrdered")){
							boolean bool = Boolean.parseBoolean(newValue);
							argumentToChange.setOrdered(bool);
						}
						if (featureId.endsWith("isUnique")){
							boolean bool = Boolean.parseBoolean(newValue);
							argumentToChange.setUnique(bool);
						}
						if (featureId.endsWith("comment")){
							argumentToChange.setComment(newValue);
						}
						if (featureId.endsWith("multiplicity")){
							IType type = argumentToChange.getType();
							if (type == null){
								type = iMethod.makeType();
							}
							IModelComponent.EMultiplicity mult = IModelComponent.EMultiplicity
								.parse(newValue);
							type.setTypeMultiplicity(mult);
							argumentToChange.setType(type);
						}
						
						iMethod.setArguments(startList);
						needSave = true;
					}
					setMethodLabelAfterChange(iMethod.getLabelString());
				}
			}
			if (needSave)
				art.doSave(new NullProgressMonitor());
		}
	}
	
}
