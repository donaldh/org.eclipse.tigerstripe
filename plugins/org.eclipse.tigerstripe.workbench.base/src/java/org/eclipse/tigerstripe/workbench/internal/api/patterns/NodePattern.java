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
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPatternResult;
import org.eclipse.tigerstripe.workbench.patterns.INodePattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class NodePattern extends ArtifactPattern implements INodePattern {

	
	public IArtifactPatternResult createArtifact(ITigerstripeModelProject project,
			String packageName, String artifactName,
			String extendedArtifactName)
			throws TigerstripeException {
		IArtifactPatternResult artifact = super.createArtifact(project, packageName, artifactName, extendedArtifactName);
		
		return artifact;
	}
}
