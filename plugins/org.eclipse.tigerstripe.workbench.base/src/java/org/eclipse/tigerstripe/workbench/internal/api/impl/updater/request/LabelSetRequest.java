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

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILabelSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

public class LabelSetRequest extends BaseArtifactElementRequest implements
		ILabelSetRequest {

	private String featureId;

	private String newValue;

	private String labelName;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IAbstractArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		if (art != null) {
			for (ILabel label : art.getLabels()) {
				if (label.getName().equals(labelName))
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
			for (ILabel label : art.getLabels()) {
				if (label.getName().equals(labelName)) {
					ILabel iLabel = (ILabel) label;
					if (NAME_FEATURE.equals(featureId)) {
						iLabel.setName(newValue);
						needSave = true;
					} else if (VALUE_FEATURE.equals(featureId)) {
						iLabel.setValue(newValue);
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

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
}
