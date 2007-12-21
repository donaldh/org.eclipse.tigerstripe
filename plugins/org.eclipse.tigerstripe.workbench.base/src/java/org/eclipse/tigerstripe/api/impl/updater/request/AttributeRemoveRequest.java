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
import org.eclipse.tigerstripe.api.artifacts.updater.request.IAttributeRemoveRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class AttributeRemoveRequest extends BaseModelChangeRequest implements
		IAttributeRemoveRequest {

	private String artifactFQN;

	private String attributeName;

	public void setArtifactFQN(String name) {
		this.artifactFQN = name;
	}

	public String getArtifactFQN() {
		return this.artifactFQN;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (art == null)
			return false;

		IextField[] fields = art.getIextFields();
		for (IextField field : fields) {
			if (field.getName().equals(getAttributeName()))
				return true;
		}
		return false;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		IField[] fields = art.getIFields();
		for (IField field : fields) {
			if (field.getName().equals(getAttributeName())) {
				art.removeIFields(new IField[] { field });
			}
		}
		art.doSave(new TigerstripeNullProgressMonitor());
	}
}
