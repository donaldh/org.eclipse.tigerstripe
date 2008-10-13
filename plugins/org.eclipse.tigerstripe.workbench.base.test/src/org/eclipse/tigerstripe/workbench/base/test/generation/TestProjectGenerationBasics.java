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

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestProjectGenerationBasics extends TestCase {

	private ITigerstripeModelProject project;

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				"TestProjectGenerationBasics", projectDetails, null,
				ITigerstripeModelProject.class, null, null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public void testGetRunConfig() throws TigerstripeException {
		IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(
				project, RunConfig.M1);
		assertNotNull(config);
	}

	public void testEmptyGenerate() throws TigerstripeException {
		IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(
				project, RunConfig.M1);
		PluginRunStatus[] status = project.generate(config, null);

		assertTrue(status.length == 0);
	}
}
