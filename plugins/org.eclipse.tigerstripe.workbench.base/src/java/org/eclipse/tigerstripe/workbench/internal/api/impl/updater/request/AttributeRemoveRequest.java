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

import java.util.Collections;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;

public class AttributeRemoveRequest extends BaseModelChangeRequest implements
		IAttributeRemoveRequest {

	private String artifactFQN;
	private IField toRemove;
	private String attributeName;
	private URI attributeURI;

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
		super.canExecute(mgrSession);
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (IField field : art.getFields()) {
				if (field.getName().equals(getAttributeName()))
					return true;
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

		for (IField field : art.getFields()) {
			if (field.getName().equals(getAttributeName())) {
				toRemove = field;
				attributeURI = (URI) field.getAdapter(URI.class);
			}
		}
		if (toRemove != null)
			art.removeFields(Collections.singleton(toRemove));
		art.doSave(new NullProgressMonitor());
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.REMOVE);

		try {
			IAbstractArtifact art = (IAbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			URI compURI = (URI) art.getAdapter(URI.class);
			delta.setAffectedModelComponentURI(compURI);
			delta.setFeature(IModelChangeDelta.ATTRIBUTE);
			delta.setOldValue(attributeURI);
			delta.setProject(art.getProject());
			delta.setSource(this);
			return delta;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return ModelChangeDelta.UNKNOWNDELTA;
	}
}
