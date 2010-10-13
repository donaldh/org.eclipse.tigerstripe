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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;

public class LiteralSetRequest extends BaseArtifactElementRequest implements
		ILiteralSetRequest {

	private String featureId;

	private String newValue;

	private String oldValue;

	private String literalName;

	private URI literalURI;
	private URI newLiteralURI;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());

			if (art != null) {
				for (ILiteral literal : art.getLiterals()) {
					if (literal.getName().equals(literalName))
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

		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN()).makeWorkingCopy(null);

		boolean needSave = false;
		if (art != null) {
			for (ILiteral literal : art.getLiterals()) {
				if (literal.getName().equals(literalName)) {
					ILiteral iLiteral = (ILiteral) literal;
					literalURI = (URI) iLiteral.getAdapter(URI.class);
					if (COMMENT_FEATURE.equals(featureId)) {
						iLiteral.setComment(newValue);
						needSave = true;
					} else if (NAME_FEATURE.equals(featureId)) {
						iLiteral.setName(newValue);
						newLiteralURI = (URI) iLiteral.getAdapter(URI.class);
						needSave = true;
					} else if (TYPE_FEATURE.equals(featureId)) {
						iLiteral.getType().setFullyQualifiedName(newValue);
						needSave = true;
					} else if (VALUE_FEATURE.equals(featureId)) {
						iLiteral.setValue(newValue);
						needSave = true;
					} else if (VISIBILITY_FEATURE.equals(featureId)) {
						if ("PUBLIC".equalsIgnoreCase(newValue)) {
							iLiteral.setVisibility(EVisibility.PUBLIC);
						} else if ("PROTECTED".equalsIgnoreCase(newValue)) {
							iLiteral.setVisibility(EVisibility.PROTECTED);
						} else if ("PRIVATE".equalsIgnoreCase(newValue)) {
							iLiteral.setVisibility(EVisibility.PRIVATE);
						} else if ("PACKAGE".equalsIgnoreCase(newValue)) {
							iLiteral.setVisibility(EVisibility.PACKAGE);
						}
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

	public void setLiteralName(String literalName) {
		this.literalName = literalName;
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.SET);

		try {
			IAbstractArtifact art = (IAbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			delta.setAffectedModelComponentURI(literalURI);
			delta.setFeature(this.featureId);
			if ("name".equals(featureId)) {
				// Bug 235928 need to put in the URI here
				delta.setOldValue(literalURI);
				delta.setNewValue(newLiteralURI);
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
