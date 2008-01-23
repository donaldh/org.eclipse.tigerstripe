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

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.updater.request.IMethodRemoveRequest;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class MethodRemoveRequest extends BaseModelChangeRequest implements
		IMethodRemoveRequest {

	private String artifactFQN;

	private String methodName;

	public void setArtifactFQN(String name) {
		this.artifactFQN = name;
	}

	public String getArtifactFQN() {
		return this.artifactFQN;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return this.methodName;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IAbstractArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art == null)
			return false;

		IMethod[] methods = art.getIMethods();
		for (IMethod method : methods) {
			if (method.getName().equals(getMethodName()))
				return true;
		}
		return false;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		IMethod[] methods = art.getIMethods();
		for (IMethod method : methods) {
			if (method.getName().equals(getMethodName())) {
				art.removeIMethods(new IMethod[] { (IMethod) method });
			}
		}
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
