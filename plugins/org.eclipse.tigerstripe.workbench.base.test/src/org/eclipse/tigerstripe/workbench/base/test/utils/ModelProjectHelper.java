/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.utils;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ModelProjectHelper {
	/**
	 * Creates a model project with the following content - 2 Managed entities
	 * (com.mycompany.M1, com.mycompany.M2)
	 * 
	 * @param projectName
	 * @return
	 */
	public static ITigerstripeModelProject createModelProject(String projectName)
			throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName(projectName);
		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectDetails, null,
						ITigerstripeModelProject.class, null, null);

		// Creating 2 Managed entities
		IArtifactManagerSession session = project.getArtifactManagerSession();
		org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact m1 = session
				.makeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact.class.getName());
		m1.setFullyQualifiedName("com.mycompany.M1");
		m1.doSave(null);
		
		org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact m2 = session
				.makeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact.class.getName());
		m2.setFullyQualifiedName("com.mycompany.M2");
		m2.doSave(null);
		
		return project;
	}

}
