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

import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IModelManager;
import org.eclipse.tigerstripe.workbench.model.IModelRepository;
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

		IModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		// Creating 2 Managed entities
		IManagedEntityArtifact m1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();

		m1.setName("M1");
		m1.setPackage("com.mycompany.test");
		repo.store(m1, true);

		IManagedEntityArtifact m2 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();

		m2.setName("M2");
		m2.setPackage("com.mycompany.test");
		repo.store(m2, true);

		// Still required for now
		project.getArtifactManagerSession().refresh(true,null);
		
		return project;
	}

}
