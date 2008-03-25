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

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * Tests for basic operations on the NewProject Wizard
 * 
 * @author Eric Dillon
 * 
 */
public class TestNewProjectWizardOperations extends TestCase {

	private static final String PROJECT_NAME = "DummyProject";

	private static final String OTHER_PROJECT_NAME = "OtherProject";

	private IProject projectHandle;

	private TestNewProjectWizardPage pageOne;


	public final void setUp() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		projectHandle = root.getProject(PROJECT_NAME);

		pageOne = new TestNewProjectWizardPage(null, null);
		pageOne.setProjectHandle(projectHandle);
	}


	public final void tearDown() throws Exception {
		NewProjectWizardHelper.removeProject(PROJECT_NAME);
		//NewProjectWizardHelper.removeProject(OTHER_PROJECT_NAME);
	}


	public final void testNewProjectWizardNoModule()
			throws TigerstripeException, InvocationTargetException,
			InterruptedException {
		NewProjectWizardHelper.newProjectWizardCreate(pageOne);
		NewProjectWizardHelper.assertValidTSProject(pageOne.getProjectHandle().getName());

	}
}
