/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import junit.framework.Assert;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class NewProjectWizardHelper {

	/**
	 * Creates a new TS model project using the NewProjectWizard
	 * 
	 * @param pageOne
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	public static void newProjectWizardCreate(
			final TestNewProjectWizardPage pageOne)
			throws InterruptedException, InvocationTargetException {
		TestNewProjectWizard wizard = new TestNewProjectWizard();
		wizard.addPage(pageOne);
		wizard.setPageOne(pageOne);

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InvocationTargetException,
					InterruptedException {
				pageOne.getRunnable(new HashMap()).run(monitor);
			}
		};

		op.run(new NullProgressMonitor());
	}

	/**
	 * Asserts that the given project is a valid TS project
	 * 
	 * @param projectName
	 */
	public static void assertValidTSProject(String projectName) {
		try {
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();

			IPath path = workspace.getRoot().getProject(projectName).getFullPath();
			IAbstractTigerstripeProject project = TigerstripeCore.findProject(path);
			if (project != null && project.exists())
				return;
			Assert.fail("Failed to find project "+projectName);

		} catch (TigerstripeException e) {
			Assert.fail("Can't determine whether project '" + projectName
					+ "' exists: " + e.getMessage());
		}
	}

	/**
	 * Returns the Tigerstripe Project corresponding to project 'projectName' in
	 * the current workspace.
	 * 
	 * If this is not a valid TS project, an exception is thrown.
	 * 
	 * @param projectName
	 * @return
	 * @throws TigerstripeException
	 */
	public static IAbstractTigerstripeProject getProject(String projectName)
			throws TigerstripeException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath path = workspace.getRoot().getProject(projectName).getFullPath();
		IAbstractTigerstripeProject project = TigerstripeCore.findProject(path);
			if (project != null && project.exists())
				return (IAbstractTigerstripeProject) project;

			throw new TigerstripeException("Invalid Tigerstripe project '"
					+ projectName + "'.");
		
	}

	/**
	 * A convenience method to create a model project in the current workspace.
	 * 
	 * This uses the NewProjectWizard behind the scenes.
	 * 
	 * @param projectName
	 * @throws TigerstripeException
	 */
	public static void createModelProject(String projectName)
			throws TigerstripeException {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projectHandle = root.getProject(projectName);
		TestNewProjectWizardPage pageOne = new TestNewProjectWizardPage(null,
				null);
		pageOne.setProjectHandle(projectHandle);

		try {
			newProjectWizardCreate(pageOne);
		} catch (Exception e) {
			throw new TigerstripeException("Couldn't create project '"
					+ projectName + "': " + e.getMessage(), e);
		}
	}

	/**
	 * Tries to remove the given project from the current workspace
	 * 
	 * @param projectName
	 * @throws CoreException
	 */
	public static void removeProject(String projectName) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		if (project.exists()) {
			project.delete(true, null);
		}
	}
}
