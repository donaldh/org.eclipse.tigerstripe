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
import org.eclipse.tigerstripe.api.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextMethod;
import org.eclipse.tigerstripe.api.external.model.IextModelComponent;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class MethodSetRequest extends BaseArtifactElementRequest implements
		IMethodSetRequest {

	private String featureId;

	private String newValue;

	private String methodLabelBeforeChange;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		if (art != null) {
			IMethod[] methods = ((IAbstractArtifact) art).getIMethods();
			for (IMethod method : methods) {
				if (method.getLabelString().equals(methodLabelBeforeChange))
					return true;
			}
		}
		return false;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {

		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		boolean needSave = false;
		if (art != null) {
			IextMethod[] methods = art.getIextMethods();
			for (IextMethod method : methods) {
				if (((IMethod) method).getLabelString().equals(
						methodLabelBeforeChange)) {
					IMethod iMethod = (IMethod) method;
					if (NAME_FEATURE.equals(featureId)) {
						iMethod.setName(newValue);
						needSave = true;
					} else if (TYPE_FEATURE.equals(featureId)) {
						IType type = iMethod.makeIType();
						type.setFullyQualifiedName(newValue);
						if (iMethod.getReturnIType() != null) {
							// don't lose the multiplicity
							type.setTypeMultiplicity(iMethod.getReturnIType()
									.getTypeMultiplicity());
						}
						iMethod.setReturnIType(type);
						needSave = true;
					} else if (MULTIPLICITY_FEATURE.equals(featureId)) {
						EMultiplicity mult = EMultiplicity.parse(newValue);
						iMethod.getReturnIType().setTypeMultiplicity(mult);
						needSave = true;
					} else if (VISIBILITY_FEATURE.equals(featureId)) {
						if ("PUBLIC".equals(newValue)) {
							iMethod
									.setVisibility(IextModelComponent.VISIBILITY_PUBLIC);
						} else if ("PROTECTED".equals(newValue)) {
							iMethod
									.setVisibility(IextModelComponent.VISIBILITY_PROTECTED);
						} else if ("PRIVATE".equals(newValue)) {
							iMethod
									.setVisibility(IextModelComponent.VISIBILITY_PRIVATE);
						} else if ("PACKAGE".equals(newValue)) {
							iMethod
									.setVisibility(IextModelComponent.VISIBILITY_PACKAGE);
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
				art.doSave(new TigerstripeNullProgressMonitor());
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
