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
import org.eclipse.tigerstripe.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.updater.request.ILabelRemoveRequest;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class LabelRemoveRequest extends BaseModelChangeRequest implements
		ILabelRemoveRequest {

	private String artifactFQN;

	private String labelName;

	public void setArtifactFQN(String name) {
		this.artifactFQN = name;
	}

	public String getArtifactFQN() {
		return this.artifactFQN;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelName() {
		return this.labelName;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IAbstractArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art == null)
			return false;

		ILabel[] labels = art.getILabels();
		for (ILabel label : labels) {
			if (label.getName().equals(getLabelName()))
				return true;
		}
		return false;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		ILabel[] labels = art.getILabels();
		for (ILabel label : labels) {
			if (label.getName().equals(getLabelName())) {
				art.removeILabels(new ILabel[] { (ILabel) label });
			}
		}
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
