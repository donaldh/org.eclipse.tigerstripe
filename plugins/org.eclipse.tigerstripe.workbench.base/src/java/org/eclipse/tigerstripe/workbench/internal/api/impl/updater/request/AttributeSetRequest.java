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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;

public class AttributeSetRequest extends BaseArtifactElementRequest implements
		IAttributeSetRequest {

	private String featureId;

	private String newValue;

	private String oldValue;

	private String attributeName;

	private URI attributeURI;

	private URI newAttributeURI;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		super.canExecute(mgrSession);

		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());

			if (art != null) {
				for (IField field : art.getFields()) {
					if (field.getName().equals(attributeName))
						return true;
				}
			}
			return false;
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

		boolean needSave = false;
		if (art != null) {
			for (IField field : art.getFields()) {
				if (field.getName().equals(attributeName)) {
					IField iField = (IField) field;
					attributeURI = (URI) iField.getAdapter(URI.class);
					if (COMMENT_FEATURE.equals(featureId)) {
						iField.setComment(newValue);
						needSave = true;
					} else if (NAME_FEATURE.equals(featureId)) {
						iField.setName(newValue);
						newAttributeURI = (URI) iField.getAdapter(URI.class);
						needSave = true;
					} else if (TYPE_FEATURE.equals(featureId)) {
						iField.getType().setFullyQualifiedName(newValue);
						needSave = true;
					} else if (MULTIPLICITY_FEATURE.equals(featureId)) {
						IModelComponent.EMultiplicity mult = IModelComponent.EMultiplicity
								.parse(newValue);
						iField.getType().setTypeMultiplicity(mult);
						needSave = true;
					} else if (VISIBILITY_FEATURE.equals(featureId)) {
						if ("PUBLIC".equalsIgnoreCase(newValue)) {
							iField.setVisibility(EVisibility.PUBLIC);
						} else if ("PROTECTED".equalsIgnoreCase(newValue)) {
							iField.setVisibility(EVisibility.PROTECTED);
						} else if ("PRIVATE".equalsIgnoreCase(newValue)) {
							iField.setVisibility(EVisibility.PRIVATE);
						} else if ("PACKAGE".equalsIgnoreCase(newValue)) {
							iField.setVisibility(EVisibility.PACKAGE);
						}
						needSave = true;
					} else if (ISUNIQUE_FEATURE.equals(featureId)) {
						boolean bool = Boolean.parseBoolean(newValue);
						iField.setUnique(bool);
						needSave = true;
					} else if (ISORDERED_FEATURE.equals(featureId)) {
						boolean bool = Boolean.parseBoolean(newValue);
						iField.setOrdered(bool);
						needSave = true;
					} else if (READONLY_FEATURE.equals(featureId)) {
						boolean bool = Boolean.parseBoolean(newValue);
						iField.setReadOnly(bool);
						needSave = true;
					} else if (DEFAULTVALUE_FEATURE.equals(featureId)) {
						iField.setDefaultValue(newValue);
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
		this.oldValue = oldValue;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.SET);

		try {
			IAbstractArtifact art = (IAbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			delta.setAffectedModelComponentURI(attributeURI);
			delta.setFeature(this.featureId);
			if ("name".equals(featureId)) {
				// Bug 235928 need to put in the URI here
				delta.setOldValue(attributeURI);
				delta.setNewValue(newAttributeURI);
			} else {
				delta.setNewValue(this.newValue);
				delta.setOldValue(this.oldValue);
			}
			delta.setProject(art.getProject());
			delta.setSource(this);
			return delta;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return ModelChangeDelta.UNKNOWNDELTA;
	}

}
