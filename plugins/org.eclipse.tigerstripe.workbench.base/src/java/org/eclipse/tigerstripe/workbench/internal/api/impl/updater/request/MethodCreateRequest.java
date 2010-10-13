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
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
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
	private String createdMethodLabel;


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
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (IMethod method : art.getMethods()) {
				if (method.getName().equals(getMethodName()))
					return false;
			}
			return true;
		} catch (TigerstripeException t) {
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);
		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN()).makeWorkingCopy(null);

		if (getMethod() == null) {
			method = art.makeMethod();

			method.setName(getMethodName());
			IType type = method.makeType();
			type.setFullyQualifiedName(getMethodType());
			if (IModelComponent.EMultiplicity.parse(methodMultiplicity) != null)
				type.setTypeMultiplicity(IModelComponent.EMultiplicity
						.parse(methodMultiplicity));
			else
				type
						.setTypeMultiplicity(IModelComponent.EMultiplicity.ZERO_ONE);
			method.setReturnType(type);
			setCreatedMethodLabel(method.getLabelString());
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

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.ADD);

		try {
			IAbstractArtifact art = (IAbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			URI compURI = (URI) art.getAdapter(URI.class);
			delta.setAffectedModelComponentURI(compURI);
			delta.setFeature(IModelChangeDelta.METHOD);
			delta.setNewValue(method.getAdapter(URI.class));
			delta.setProject(art.getProject());
			delta.setSource(this);
			return delta;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return ModelChangeDelta.UNKNOWNDELTA;
	}

	public String getCreatedMethodLabel() {
		return createdMethodLabel;
	}

	private void setCreatedMethodLabel(String createdMethodLabel) {
		this.createdMethodLabel = createdMethodLabel;
	}

}
