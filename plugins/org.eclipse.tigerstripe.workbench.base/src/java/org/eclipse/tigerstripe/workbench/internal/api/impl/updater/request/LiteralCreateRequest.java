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
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralCreateRequest;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

public class LiteralCreateRequest extends BaseArtifactElementRequest implements
		ILiteralCreateRequest {

	private String literalName;
	private String literalValue;
	private String literalType;

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
		try{
			IAbstractArtifact art = mgrSession
			.getArtifactByFullyQualifiedName(getArtifactFQN());
			if (art == null)
				return false;

			for (ILiteral literal : art.getLiterals()) {
				if (literal.getName().equals(getLiteralName()))
					return false;
			}
			return true;
		}
		catch (TigerstripeException t){
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact art = (IAbstractArtifact) mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		ILiteral literal = art.makeLiteral();
		literal.setName(getLiteralName());
		literal.setValue(getLiteralValue());
		IType type = literal.makeType();
		type.setFullyQualifiedName(getLiteralType());
		literal.setType(type);
		art.addLiteral(literal);
		art.doSave(new NullProgressMonitor());
	}
}
