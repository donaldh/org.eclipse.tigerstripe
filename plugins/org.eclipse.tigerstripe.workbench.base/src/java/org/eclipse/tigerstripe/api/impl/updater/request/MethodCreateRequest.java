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
package org.eclipse.tigerstripe.api.impl.updater.request;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextMethod;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

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
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art == null)
			return false;

		IextMethod[] methods = art.getIextMethods();
		for (IextMethod method : methods) {
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

		IMethod method = art.makeIMethod();
		method.setName(getMethodName());
		IType type = method.makeIType();
		type.setFullyQualifiedName(getMethodType());
		if (EMultiplicity.parse(methodMultiplicity) != null)
			type.setTypeMultiplicity(EMultiplicity.parse(methodMultiplicity));
		else
			type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		method.setReturnIType(type);

		art.addIMethod(method);
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
