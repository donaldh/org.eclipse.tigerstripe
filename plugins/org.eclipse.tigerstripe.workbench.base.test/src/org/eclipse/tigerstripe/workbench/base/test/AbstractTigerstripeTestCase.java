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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.annotation.ArtifactTestHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
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
	protected final IWorkspace workspace = ResourcesPlugin.getWorkspace();

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
		IAbstractArtifact refArtifact = null;
		for (String supportedArtifact : supportedArtifacts) {
			if (!supportedArtifact.endsWith("IPackageArtifact")) {
				IAbstractArtifact artifact = artifactHelper.createArtifact(
						supportedArtifact, getArtifactName(supportedArtifact),
						TEST_PACKAGE_NAME);
				if (createSubComponents) {
					if (refArtifact == null) {
						refArtifact = artifactHelper.createArtifact(
								IManagedEntityArtifact.class.getName(),
								"RefEntity", TEST_PACKAGE_NAME);
						refArtifact.doSave(new NullProgressMonitor());
					}

					IField field = artifact.makeField();
					field.setName("field1");
					IType type = field.makeType();
					type.setFullyQualifiedName(refArtifact
							.getFullyQualifiedName());
					field.setType(type);
					artifact.addField(field);

					field = artifact.makeField();
					field.setName("field2");
					type = field.makeType();
					type.setFullyQualifiedName(artifact.getFullyQualifiedName());
					field.setType(type);
					artifact.addField(field);

					ILiteral literal1 = artifact.makeLiteral();
					String literalName = "literal1";
					literal1.setName(literalName);
					type = literal1.makeType();
					type.setFullyQualifiedName(refArtifact
							.getFullyQualifiedName());
					literal1.setType(type);
					artifact.addLiteral(literal1);

					IMethod method1 = artifact.makeMethod();
					method1.setName("method1");
					type = method1.makeType();
					type.setFullyQualifiedName(refArtifact
							.getFullyQualifiedName());
					method1.setReturnType(type);
					IArgument arg = method1.makeArgument();
					arg.setName("arg1");
					arg.setType(type);
					method1.addArgument(arg);
					artifact.addMethod(method1);

					if (artifact instanceof IAssociationArtifact) {
						IAssociationArtifact association = (IAssociationArtifact) artifact;
						IAssociationEnd aEnd = association.makeAssociationEnd();
						aEnd.setName(association.getName() + "_aEnd");
						aEnd.setNavigable(false);
						type = aEnd.makeType();
						type.setFullyQualifiedName(refArtifact
								.getFullyQualifiedName());
						aEnd.setType(type);
						association.setAEnd(aEnd);

						IAssociationEnd zEnd = association.makeAssociationEnd();
						zEnd.setName(association.getName() + "_zEnd");
						zEnd.setNavigable(true);
						type = zEnd.makeType();
						type.setFullyQualifiedName(refArtifact
								.getFullyQualifiedName());
						zEnd.setType(type);
						association.setZEnd(zEnd);
					} else if (artifact instanceof IDependencyArtifact) {
						IDependencyArtifact dependency = (IDependencyArtifact) artifact;
						type = dependency.makeType();
						type.setFullyQualifiedName(refArtifact
								.getFullyQualifiedName());
						dependency.setAEndType(type);
						dependency.setZEndType(type);
					} else if (artifact instanceof IEnumArtifact) {
						((IEnumArtifact) artifact).setBaseType(type);
					} else if (artifact instanceof IQueryArtifact) {
						((IQueryArtifact) artifact).setReturnedType(type);
					}
				}
				artifact.doSave(new NullProgressMonitor());
			}
		}
		return supportedArtifacts.size();
	}

	protected IAbstractArtifact createArtifact(
			IAbstractTigerstripeProject aProject, String artifactType,
			String artifactName, IAbstractArtifact toExtend,
			boolean createSubComponents) throws TigerstripeException {
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);

		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();
		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		IAbstractArtifact artifact = artifactHelper.createArtifact(
				artifactType, artifactName != null ? artifactName
						: getArtifactName(artifactType), TEST_PACKAGE_NAME);
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

			if (toExtend != null) {
				artifact.setExtendedArtifact(toExtend.getFullyQualifiedName());
			}

			artifact.doSave(new NullProgressMonitor());
		}

		return artifact;
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

	protected String getArtifactName(String artifactType) {
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

	protected static abstract class SafeRunnable implements ISafeRunnable {

		public void handleException(Throwable exception) {
			throw new RuntimeException(exception);
		}
	}

	protected void runInWorkspace(final ISafeRunnable runnable) {
		try {
			workspace.run(new IWorkspaceRunnable() {

				public void run(IProgressMonitor monitor) throws CoreException {
					SafeRunner.run(runnable);
				}
			}, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	protected void waitForUpdates() {
		IJobManager jobManager = Job.getJobManager();
		while (jobManager.find(ITigerstripeChangeListener.NOTIFY_JOB_FAMILY).length > 0
				|| jobManager.find(ResourcesPlugin.FAMILY_MANUAL_BUILD).length > 0) {
			try {
				jobManager.join(ITigerstripeChangeListener.NOTIFY_JOB_FAMILY,
						null);
				jobManager.join(ResourcesPlugin.FAMILY_MANUAL_BUILD, null);
			} catch (InterruptedException ie) {
				return;
			}
		}
	}

	protected void cleanup() {
		runInWorkspace(new SafeRunnable() {

			public void run() throws Exception {
				for (final IProject project : workspace.getRoot().getProjects()) {
					project.delete(true, null);
				}
			}
		});
		waitForUpdates();
	}
}
