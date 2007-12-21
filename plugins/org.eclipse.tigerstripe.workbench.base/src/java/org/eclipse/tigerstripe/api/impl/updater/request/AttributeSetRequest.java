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
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.api.external.model.IextModelComponent;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class AttributeSetRequest extends BaseArtifactElementRequest implements
		IAttributeSetRequest {

	private String featureId;

	private String newValue;

	private String attributeName;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		if (art != null) {
			IextField[] fields = art.getIextFields();
			for (IextField field : fields) {
				if (field.getName().equals(attributeName))
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
			IextField[] fields = art.getIextFields();
			for (IextField field : fields) {
				if (field.getName().equals(attributeName)) {
					IField iField = (IField) field;
					if (NAME_FEATURE.equals(featureId)) {
						iField.setName(newValue);
						needSave = true;
					} else if (TYPE_FEATURE.equals(featureId)) {
						iField.getIType().setFullyQualifiedName(newValue);
						needSave = true;
					} else if (MULTIPLICITY_FEATURE.equals(featureId)) {
						EMultiplicity mult = EMultiplicity.parse(newValue);
						iField.getIType().setTypeMultiplicity(mult);
						needSave = true;
					} else if (VISIBILITY_FEATURE.equals(featureId)) {
						if ("PUBLIC".equals(newValue)) {
							iField
									.setVisibility(IextModelComponent.VISIBILITY_PUBLIC);
						} else if ("PROTECTED".equals(newValue)) {
							iField
									.setVisibility(IextModelComponent.VISIBILITY_PROTECTED);
						} else if ("PRIVATE".equals(newValue)) {
							iField
									.setVisibility(IextModelComponent.VISIBILITY_PRIVATE);
						} else if ("PACKAGE".equals(newValue)) {
							iField
									.setVisibility(IextModelComponent.VISIBILITY_PACKAGE);
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
					} else if (DEFAULTVALUE_FEATURE.equals(featureId)) {
						iField.setDefaultValue(newValue);
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

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
}
