/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test;

import java.util.Collection;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.annotation.ArtifactTestHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;

/**
 * @author Yuri Strot
 * 
 */
public abstract class AbstractTigerstripeTestCase extends TestCase {

	protected static final String TEST_PACKAGE_NAME = "com.test";

	protected IAbstractTigerstripeProject createEmptyModelProject(
			String modelId, String projectName) throws TigerstripeException {
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		if (modelId != null) {
			details.setModelId(modelId);
		}
		IAbstractTigerstripeProject aProject = TigerstripeCore.createProject(
				projectName, details, null, ITigerstripeModelProject.class,
				null, new NullProgressMonitor());
		return aProject;
	}

	protected void deleteModelProject(IAbstractTigerstripeProject project)
			throws TigerstripeException {
		project.delete(true, new NullProgressMonitor());
	}

	protected int createEachArtifactType(IAbstractTigerstripeProject aProject)
			throws TigerstripeException {
		return createEachArtifactType(aProject, false);
	}
	protected int createEachArtifactType(IAbstractTigerstripeProject aProject,
			boolean createSubComponents) throws TigerstripeException {
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);

		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();
		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		for (String supportedArtifact : supportedArtifacts) {
			if (!supportedArtifact.endsWith("IPackageArtifact")) {
				IAbstractArtifact artifact = artifactHelper.createArtifact(
						supportedArtifact, getArtifactName(supportedArtifact),
						TEST_PACKAGE_NAME);
				if (createSubComponents) {
					IField field = artifact.makeField();
					String fieldName = "field1";
					field.setName(fieldName);
					artifact.addField(field);

					ILiteral literal1 = artifact.makeLiteral();
					String literalName = "literal1";
					literal1.setName(literalName);
					artifact.addLiteral(literal1);

					IMethod method1 = artifact.makeMethod();
					String methodName = "method1";
					method1.setName(methodName);
					artifact.addMethod(method1);

					artifact.doSave(new NullProgressMonitor());
				}
			}
		}
		return supportedArtifacts.size();
	}

	protected boolean removeEachArtifactType(
			IAbstractTigerstripeProject aProject) throws TigerstripeException {
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);
		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();

		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		for (String supportedArtifact : supportedArtifacts) {
			if (!supportedArtifact.endsWith("IPackageArtifact")) {
				String fqn = TEST_PACKAGE_NAME + "."
						+ getArtifactName(supportedArtifact);
				IAbstractArtifact artifact = mgrSession
						.getArtifactByFullyQualifiedName(fqn);
				artifactHelper.remove(artifact);
			}
		}
		return true;
	}

	protected Collection<IAbstractArtifact> getArtifacts(
			IAbstractTigerstripeProject aProject) throws TigerstripeException {
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();

		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		Collection<IAbstractArtifact> artifacts = new LinkedList<IAbstractArtifact>();
		for (String supportedArtifact : supportedArtifacts) {
			if (!supportedArtifact.endsWith("IPackageArtifact")) {
				String fqn = TEST_PACKAGE_NAME + "."
						+ getArtifactName(supportedArtifact);
				IAbstractArtifact artifact = mgrSession
						.getArtifactByFullyQualifiedName(fqn);
				artifacts.add(artifact);
			}
		}
		return artifacts;
	}

	private String getArtifactName(String artifactType) {
		return "InstanceOf"
				+ artifactType.substring(artifactType.lastIndexOf(".") + 1,
						artifactType.length());
	}

	protected Collection<IAbstractArtifact> getAllArtifacts(
			IAbstractTigerstripeProject aProject) throws TigerstripeException {
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();

		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		Collection<IAbstractArtifact> artifacts = new LinkedList<IAbstractArtifact>();
		for (String supportedArtifact : supportedArtifacts) {
			IQueryArtifactsByType query = (IQueryArtifactsByType) mgrSession
					.makeQuery(IQueryArtifactsByType.class.getName());
			query.setIncludeDependencies(true);
			query.setArtifactType(supportedArtifact);

			Collection<IAbstractArtifact> entities = mgrSession
					.queryArtifact(query);
			// System.out.println(supportedArtifact+": "+entities);
			if (entities != null && !entities.isEmpty())
				artifacts.addAll(entities);
		}
		return artifacts;
	}
}
