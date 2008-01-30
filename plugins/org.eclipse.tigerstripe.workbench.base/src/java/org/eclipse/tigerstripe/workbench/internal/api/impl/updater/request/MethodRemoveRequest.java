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

import java.util.Collections;

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

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
		try{
			IAbstractArtifact art = mgrSession
			.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (IMethod method : art.getMethods()) {
				if (method.getName().equals(getMethodName()))
					return true;
			}
			return false;
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

		for (IMethod method : art.getMethods()) {
			if (method.getName().equals(getMethodName())) {
				art.removeMethods(Collections.singleton(method));
			}
		}
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
