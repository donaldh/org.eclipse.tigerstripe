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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.patterns.IEnumPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Element;

public class EnumPattern extends NodePattern implements IEnumPattern {

	private String baseType;
	
	public String getBaseType(){
		return this.baseType;
	}
	
	private void setBaseType(String baseType){
		this.baseType = baseType;
	}
	
	
	public void setElement(Element artifactElement) {
		super.setElement(artifactElement);
		String baseType = xmlParserUtils.getBaseType(artifactElement);
		setBaseType(baseType);
	}
	
	public IAbstractArtifact createArtifact(ITigerstripeModelProject project,
			String packageName, String artifactName,
			String extendedArtifactName, String baseType)
			throws TigerstripeException {
		IAbstractArtifact artifact = super.createArtifact(project, packageName, artifactName, extendedArtifactName);
		addBaseType(artifact, baseType);
		return artifact;
	}

	// Should maybe add a check here ?
	private void addBaseType(IAbstractArtifact artifact, String baseType){
		if (artifact instanceof IEnumArtifact){
			IEnumArtifact enumArtifact = (IEnumArtifact) artifact;
			IType type = enumArtifact.getBaseType();
			if (type == null){
				type = enumArtifact.makeField().makeType();
			}
			type.setFullyQualifiedName(baseType);
			enumArtifact.setBaseType(type);
		}
	}
	
}
