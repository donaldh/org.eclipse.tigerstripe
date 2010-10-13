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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

public class LiteralCreateRequest extends BaseArtifactElementRequest implements
		ILiteralCreateRequest {

	private String literalName;
	private String literalValue;
	private String literalType;
	private ILiteral literal;
	private URI literalURI;

	public void setLiteralType(String type) {
		this.literalType = type;
	}

	public String getLiteralType() {
		return this.literalType;
	}

	public void setLiteralName(String literalName) {
		this.literalName = literalName;
	}

	public String getLiteralName() {
		return this.literalName;
	}

	public void setLiteralValue(String literalValue) {
		this.literalValue = literalValue;
	}

	public String getLiteralValue() {
		return this.literalValue;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (ILiteral literal : art.getLiterals()) {
				if (literal.getName().equals(getLiteralName()))
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
		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN()).makeWorkingCopy(null);

		ILiteral literal;
		if (this.getLiteral() == null) {
			literal = art.makeLiteral();

			literal.setName(getLiteralName());
			literal.setValue(getLiteralValue());
			IType type = literal.makeType();
			type.setFullyQualifiedName(getLiteralType());
			literal.setType(type);
		} else {
			literal = this.getLiteral();
		}
		art.addLiteral(literal);
		literalURI = (URI) literal.getAdapter(URI.class);
		art.doSave(new NullProgressMonitor());
	}

	public void setLiteral(ILiteral literal) {
		this.literal = literal;
	}

	public ILiteral getLiteral() {
		return this.literal;
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.ADD);

		try {
			IAbstractArtifact art = (IAbstractArtifact) getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			URI compURI = (URI) art.getAdapter(URI.class);
			delta.setFeature(IModelChangeDelta.LITERAL);
			delta.setAffectedModelComponentURI(compURI);
			delta.setNewValue(literalURI);
			delta.setProject(art.getProject());
			delta.setSource(this);
			return delta;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return ModelChangeDelta.UNKNOWNDELTA;
	}

}
