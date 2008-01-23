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
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

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
		IAbstractArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art == null)
			return false;

		IField[] fields = art.getIFields();
		for (IField field : fields) {
			if (field.getName().equals(getAttributeName()))
				return false;
		}
		return true;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		IField field = art.makeIField();
		field.setName(getAttributeName());
		IType type = field.makeIType();
		type.setFullyQualifiedName(getAttributeType());

		if (EMultiplicity.parse(attributeMultiplicity) != null)
			type
					.setTypeMultiplicity(EMultiplicity
							.parse(attributeMultiplicity));
		else
			type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		field.setIType(type);

		art.addIField(field);
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
