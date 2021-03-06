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

	public final static String DEFAULT_PKG = "com.mycompany";

	public final static String M1 = DEFAULT_PKG + ".M1";
	public final static String M2 = DEFAULT_PKG + ".M2";
	public final static String M3 = DEFAULT_PKG + ".M3";

	public final static String AC1 = DEFAULT_PKG + ".AC1";

	public final static String AS1 = DEFAULT_PKG + ".AS1";

	/**
	 * Creates a model project with the following content AS1
	 * M3<--------------------AC1 M1--------->M2
	 * 
	 * 
	 * @param projectName
	 * @return
	 */
	public static ITigerstripeModelProject createModelProject(String projectName)
			throws TigerstripeException {
		return createModelProject(projectName, false);
	}

	/**
	 * Creates an empty model project.
	 * 
	 * @param projectName
	 * @return
	 * @throws TigerstripeException
	 */
	public static ITigerstripeModelProject createEmptyModelProject(
			String projectName, String modelId) throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setModelId(modelId);
		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectName, projectDetails, null,
						ITigerstripeModelProject.class, null, null);

		return project;
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
		projectDetails.setModelId(projectName);
		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectName, projectDetails, null,
						ITigerstripeModelProject.class, null, null);

		createEntitiesWithAssociations(project, fullContent);

		return project;
	}
	
	public static void createEntitiesWithAssociations(
			ITigerstripeModelProject project, boolean createAssociations)
			throws TigerstripeException {
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

		if (createAssociations) {
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
			aEnd.setName("_aEnd");
			aEnd.setNavigable(false);
			IType aType = aEnd.makeType();
			aType.setFullyQualifiedName(M1);
			aEnd.setType(aType);
			ac1.setAEnd(aEnd);

			IAssociationEnd zEnd = ac1.makeAssociationEnd();
			zEnd.setName("_zEnd");
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
			aEnd.setName("aaEnd");
			aEnd.setNavigable(false);
			aType = aEnd.makeType();
			aType.setFullyQualifiedName(AC1);
			aEnd.setType(aType);
			as1.setAEnd(aEnd);

			zEnd = as1.makeAssociationEnd();
			zEnd.setName("zzEnd");
			zEnd.setNavigable(true);
			zType = zEnd.makeType();
			zType.setFullyQualifiedName(M3);
			zEnd.setType(zType);
			as1.setZEnd(zEnd);
			as1.doSave(null);
		}

	}
}
