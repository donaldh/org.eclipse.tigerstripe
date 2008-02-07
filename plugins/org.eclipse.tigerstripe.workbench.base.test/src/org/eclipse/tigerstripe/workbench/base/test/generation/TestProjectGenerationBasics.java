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
package org.eclipse.tigerstripe.workbench.base.test.generation;

import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IRunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

import junit.framework.TestCase;

public class TestProjectGenerationBasics extends TestCase {

	private ITigerstripeProject project;

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("TestProjectGenerationBasics");
		project = (ITigerstripeProject) TigerstripeCore.createProject(
				projectDetails, null, ITigerstripeProject.class, null, null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public void testGetRunConfig() throws TigerstripeException {
		IRunConfig config = project.makeDefaultRunConfig();
		assertNotNull(config);
	}

	public void testEmptyGenerate() throws TigerstripeException {
		IRunConfig config = project.makeDefaultRunConfig();
		PluginRunStatus[] status = project.generate(config, null);

		assertTrue(status.length == 0);

		// Check that an empty dir was created as a result of the generation
		String outputDir = project.getProjectDetails().getOutputDirectory();
		IPath projectLocation = project.getLocation();
		IPath outputLocation = projectLocation.append(outputDir);
		assertTrue(outputLocation.toFile().exists());
	}
}
