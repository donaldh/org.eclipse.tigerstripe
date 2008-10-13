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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IPatternBasedCreationValidator {

	/**
	 * Use this version for NODE artifacts
	 */
	public IStatus[] validateWizardEntry(ITigerstripeModelProject project, INodePattern pattern, String artifactName, String artifactPackage, String extendedArtifactFQN);
	
	/**
	 * Use this version for RELATION artifacts
	 */
	public IStatus[] validateWizardEntry(ITigerstripeModelProject project, IRelationPattern pattern, String artifactName, String artifactPackage, String extendedArtifactFQN, 
			String aEndFQN, String zEndFQN);
	
	
	
	
}
