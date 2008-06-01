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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public class AttributeCreateRequest extends BaseArtifactElementRequest
		implements IAttributeCreateRequest {

	private String attributeName;
	private String attributeType;
	private String attributeMultiplicity;
	private IField field;

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
		super.canExecute(mgrSession);
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (IField field : art.getFields()) {
				if (field.getName().equals(getAttributeName()))
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
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		IField field;
		if (getField() == null) {
			field = art.makeField();
			field.setName(getAttributeName());
			IType type = field.makeType();
			type.setFullyQualifiedName(getAttributeType());

			if (IModelComponent.EMultiplicity.parse(attributeMultiplicity) != null)
				type.setTypeMultiplicity(IModelComponent.EMultiplicity
						.parse(attributeMultiplicity));
			else
				type
						.setTypeMultiplicity(IModelComponent.EMultiplicity.ZERO_ONE);
			field.setType(type);

		} else {
			field = getField();
		}

		art.addField(field);
		art.doSave(new NullProgressMonitor());
	}

	public void setField(IField field) {
		this.field = field;

	}

	public IField getField() {
		return this.field;
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.ADD);

		try {
			IAbstractArtifact art = (IAbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			URI compURI = (URI) art.getAdapter(URI.class);
			delta.setAffectedModelComponentURI(compURI);
			URI attrURI = URI.createURI(compURI.toString()).appendFragment(
					getAttributeName());
			delta.setFeature(IModelChangeDelta.ATTRIBUTE);
			delta.setNewValue(attrURI);
			delta.setProject(art.getProject());
			delta.setSource(this);
			return delta;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return ModelChangeDelta.UNKNOWNDELTA;
	}

}
