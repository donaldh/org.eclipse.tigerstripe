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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;

public class MethodSetRequest extends BaseArtifactElementRequest implements
		IMethodSetRequest {

	private String featureId;

	private String newValue;

	private String methodLabelBeforeChange;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		try{
		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		if (art != null) {
			for (IMethod method : art.getMethods()) {
				if (method.getLabelString().equals(methodLabelBeforeChange))
					return true;
			}
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

		boolean needSave = false;
		if (art != null) {
			for (IMethod method : art.getMethods()) {
				if (((IMethod) method).getLabelString().equals(
						methodLabelBeforeChange)) {
					IMethod iMethod = (IMethod) method;
					if (NAME_FEATURE.equals(featureId)) {
						iMethod.setName(newValue);
						needSave = true;
					} else if (TYPE_FEATURE.equals(featureId)) {
						IType type = iMethod.makeType();
						type.setFullyQualifiedName(newValue);
						if (iMethod.getReturnType() != null) {
							// don't lose the multiplicity
							type.setTypeMultiplicity(iMethod.getReturnType()
									.getTypeMultiplicity());
						}
						iMethod.setReturnType(type);
						needSave = true;
					} else if (MULTIPLICITY_FEATURE.equals(featureId)) {
						IModelComponent.EMultiplicity mult = IModelComponent.EMultiplicity.parse(newValue);
						iMethod.getReturnType().setTypeMultiplicity(mult);
						needSave = true;
					} else if (VISIBILITY_FEATURE.equals(featureId)) {
						if ("PUBLIC".equals(newValue)) {
							iMethod
									.setVisibility(EVisibility.PUBLIC);
						} else if ("PROTECTED".equals(newValue)) {
							iMethod
									.setVisibility(EVisibility.PROTECTED);
						} else if ("PRIVATE".equals(newValue)) {
							iMethod
									.setVisibility(EVisibility.PRIVATE);
						} else if ("PACKAGE".equals(newValue)) {
							iMethod
									.setVisibility(EVisibility.PACKAGE);
						}
						needSave = true;
					} else if (ISABSTRACT_FEATURE.equals(featureId)) {
						iMethod.setAbstract("true".equals(newValue));
						needSave = true;

					} else if (ISUNIQUE_FEATURE.equals(featureId)) {
						boolean bool = Boolean.parseBoolean(newValue);
						iMethod.setUnique(bool);
						needSave = true;
					} else if (ISORDERED_FEATURE.equals(featureId)) {
						boolean bool = Boolean.parseBoolean(newValue);
						iMethod.setOrdered(bool);
						needSave = true;
					} else if (DEFAULTRETURNVALUE_FEATURE.equals(featureId)) {
						iMethod.setDefaultReturnValue(newValue);
						needSave = true;
					}
				}
			}
			if (needSave)
				art.doSave(new NullProgressMonitor());
		}
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public void setOldValue(String oldValue) {
	}

	public void setMethodLabelBeforeChange(String methodLabelBeforeChange) {
		this.methodLabelBeforeChange = methodLabelBeforeChange;
	}
}
