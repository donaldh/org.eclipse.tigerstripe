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

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

public class MethodCreateRequest extends BaseArtifactElementRequest implements
		IMethodCreateRequest {

	private String methodName;
	private String methodType;
	private String methodMultiplicity;

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
		IAbstractArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art == null)
			return false;

		for (IMethod method : art.getMethods()) {
			if (method.getName().equals(getMethodName()))
				return false;
		}
		return true;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		IMethod method = art.makeMethod();
		method.setName(getMethodName());
		IType type = method.makeIType();
		type.setFullyQualifiedName(getMethodType());
		if (IModelComponent.EMultiplicity.parse(methodMultiplicity) != null)
			type.setTypeMultiplicity(IModelComponent.EMultiplicity.parse(methodMultiplicity));
		else
			type.setTypeMultiplicity(IModelComponent.EMultiplicity.ZERO_ONE);
		method.setReturnIType(type);

		art.addMethod(method);
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
