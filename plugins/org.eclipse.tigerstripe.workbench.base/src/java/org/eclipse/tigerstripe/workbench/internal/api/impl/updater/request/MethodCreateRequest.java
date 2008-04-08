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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public class MethodCreateRequest extends BaseArtifactElementRequest implements
		IMethodCreateRequest {

	private String methodName;
	private String methodType;
	private String methodMultiplicity;
	private IMethod method;

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getMethodType() {
		return this.methodType;
	}

	public void setMethodMultiplicity(String methodMultiplicity) {
		this.methodMultiplicity = methodMultiplicity;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		try{
			IAbstractArtifact art = mgrSession
			.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (IMethod method : art.getMethods()) {
				if (method.getName().equals(getMethodName()))
					return false;
			}
			return true;
		}
		catch (TigerstripeException t){
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		IMethod method;
		if (this.getMethod()== null){
			method = art.makeMethod();
		
		method.setName(getMethodName());
		IType type = method.makeType();
		type.setFullyQualifiedName(getMethodType());
		if (IModelComponent.EMultiplicity.parse(methodMultiplicity) != null)
			type.setTypeMultiplicity(IModelComponent.EMultiplicity.parse(methodMultiplicity));
		else
			type.setTypeMultiplicity(IModelComponent.EMultiplicity.ZERO_ONE);
		method.setReturnType(type);
		} else {
			method = this.getMethod();
		}
		art.addMethod(method);
		art.doSave(new NullProgressMonitor());
	}

	public IMethod getMethod() {
		return method;
	}

	public void setMethod(IMethod method) {
		this.method = method;
	}
}
