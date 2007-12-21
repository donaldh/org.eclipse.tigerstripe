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
import org.eclipse.tigerstripe.api.artifacts.model.ILabel;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.updater.request.ILabelCreateRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextLabel;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class LabelCreateRequest extends BaseArtifactElementRequest implements
		ILabelCreateRequest {

	private String labelName;
	private String labelValue;
	private String labelType;

	public void setLabelType(String type) {
		this.labelType = type;
	}

	public String getLabelType() {
		return this.labelType;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelName() {
		return this.labelName;
	}

	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}

	public String getLabelValue() {
		return this.labelValue;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art == null)
			return false;

		IextLabel[] labels = art.getIextLabels();
		for (IextLabel label : labels) {
			if (label.getName().equals(getLabelName()))
				return false;
		}
		return true;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		ILabel label = art.makeILabel();
		label.setName(getLabelName());
		label.setValue(getLabelValue());
		IType type = label.makeIType();
		type.setFullyQualifiedName(getLabelType());
		label.setIType(type);
		art.addILabel(label);
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
