/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.patterns;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.patterns.IQueryPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Element;

public class QueryPattern extends NodePattern implements IQueryPattern {

	private String returnType = "";
	
	
	public String getReturnType() {
		return returnType;
	}

	private void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	@Override
	public void setElement(Element artifactElement) {
		super.setElement(artifactElement);
		String returnType = xmlParserUtils.getReturnType(artifactElement);
		setReturnType(returnType);
	}

	public IAbstractArtifact createArtifact(ITigerstripeModelProject project,
			String packageName, String artifactName,
			String extendedArtifactName, String returnType)
			throws TigerstripeException {
		IAbstractArtifact artifact = super.createArtifact(project, packageName, artifactName, extendedArtifactName);
		addReturnType(artifact, returnType);
		return artifact;
	}

	private void addReturnType(IAbstractArtifact artifact,String returnType) throws TigerstripeException {
	if (returnType != null){
		if (artifact instanceof IQueryArtifact){
			IQueryArtifact query = (IQueryArtifact) artifact;
			IType type = query.getReturnedType();
			if (type == null) {
				type = query.makeType();
			}
			type.setFullyQualifiedName(returnType);
			query.setReturnedType(type);
		}
	}
	
}
	
}
