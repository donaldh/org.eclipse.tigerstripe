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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ModelProjectHelper {

	public final static String M1 = "com.mycompany.M1";
	public final static String M2 = "com.mycompany.M2";
	public final static String M3 = "com.mycompany.M3";

	public final static String AC1 = "com.mycompany.AC1";

	public final static String AS1 = "com.mycompany.AS1";

	/**
	 * Creates a model project with the following content - 2 Managed entities
	 * (com.mycompany.M1, com.mycompany.M2)
	 * 
	 * @param projectName
	 * @return
	 */
	public static ITigerstripeModelProject createModelProject(String projectName)
			throws TigerstripeException {
		return createModelProject(projectName, false);
	}

	/**
	 * Creates a model project with limited content:2 Managed entities
	 * (com.mycompany.M1, com.mycompany.M2)
	 * 
	 * or with full content (fullContent == true)
	 * 
	 * @param projectName
	 * @param fullContent
	 * @return
	 * @throws TigerstripeException
	 */
	public static ITigerstripeModelProject createModelProject(
			String projectName, boolean fullContent)
			throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName(projectName);
		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectDetails, null,
						ITigerstripeModelProject.class, null, null);

		// Creating 2 Managed entities
		IArtifactManagerSession session = project.getArtifactManagerSession();
		org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact m1 = session
				.makeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact.class
						.getName());
		m1.setFullyQualifiedName(M1);
		m1.doSave(null);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact m2 = session
				.makeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact.class
						.getName());
		m2.setFullyQualifiedName(M2);
		m2.doSave(null);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact m3 = session
				.makeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact.class
						.getName());
		m3.setFullyQualifiedName(M3);
		m3.doSave(null);

		// AC1 is an association class from M1 to M2
		IAssociationClassArtifact ac1 = (IAssociationClassArtifact) session
				.makeArtifact(IAssociationClassArtifact.class.getName());
		ac1.setFullyQualifiedName(AC1);
		IAssociationEnd aEnd = ac1.makeAssociationEnd();
		aEnd.setNavigable(false);
		IType aType = aEnd.makeType();
		aType.setFullyQualifiedName(M1);
		aEnd.setType(aType);
		ac1.setAEnd(aEnd);


		IAssociationEnd zEnd = ac1.makeAssociationEnd();
		zEnd.setNavigable(true);
		IType zType = zEnd.makeType();
		zType.setFullyQualifiedName(M2);
		zEnd.setType(zType);
		ac1.setZEnd(zEnd);
		ac1.doSave(null);

		// AS1 is an association from AC1 to M3
		IAssociationArtifact as1 = (IAssociationArtifact) session
				.makeArtifact(IAssociationArtifact.class.getName());
		as1.setFullyQualifiedName(AS1);
		aEnd = as1.makeAssociationEnd();
		aEnd.setNavigable(false);
		aType = aEnd.makeType();
		aType.setFullyQualifiedName(AC1);
		aEnd.setType(aType);
		as1.setAEnd(aEnd);

		zEnd = as1.makeAssociationEnd();
		zEnd.setNavigable(true);
		zType = zEnd.makeType();
		zType.setFullyQualifiedName(M3);
		zEnd.setType(zType);
		as1.setZEnd(zEnd);
		as1.doSave(null);

		return project;
	}

}
