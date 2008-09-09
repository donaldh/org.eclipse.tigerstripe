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
package org.eclipse.tigerstripe.workbench.patterns;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IEnumPattern extends INodePattern {

	public String getBaseType();
	
	/** 
	 * Create and return an artifact that matches the pattern
	 * NOTE : This does not add it to the manager
	 * @param project
	 * @param packageName
	 * @param artifactName
	 * @param extendedArtifactName
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact createArtifact(ITigerstripeModelProject project,
			String packageName, String artifactName, String extendedArtifactName, String baseType)  throws TigerstripeException ;
	
}
