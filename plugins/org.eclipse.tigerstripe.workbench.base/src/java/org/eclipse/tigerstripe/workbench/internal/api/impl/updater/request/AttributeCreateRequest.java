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
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

public class AttributeCreateRequest extends BaseArtifactElementRequest
		implements IAttributeCreateRequest {

	private String attributeName;
	private String attributeType;
	private String attributeMultiplicity;

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getAttributeType() {
		return this.attributeType;
	}

	public void setAttributeMultiplicity(String attributeMultiplicity) {
		this.attributeMultiplicity = attributeMultiplicity;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		try{
			IAbstractArtifact art = mgrSession
			.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (IField field : art.getFields()) {
				if (field.getName().equals(getAttributeName()))
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

		IField field = art.makeField();
		field.setName(getAttributeName());
		IType type = field.makeType();
		type.setFullyQualifiedName(getAttributeType());

		if (IModelComponent.EMultiplicity.parse(attributeMultiplicity) != null)
			type
					.setTypeMultiplicity(IModelComponent.EMultiplicity
							.parse(attributeMultiplicity));
		else
			type.setTypeMultiplicity(IModelComponent.EMultiplicity.ZERO_ONE);
		field.setType(type);

		art.addField(field);
		art.doSave(new NullProgressMonitor());
	}
}
