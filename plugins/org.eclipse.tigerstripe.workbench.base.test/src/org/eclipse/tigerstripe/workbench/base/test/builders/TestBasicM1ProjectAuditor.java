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
package org.eclipse.tigerstripe.workbench.base.test.builders;

import junit.framework.TestCase;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class TestBasicM1ProjectAuditor extends TestCase {

	private ITigerstripeM1GeneratorProject project;

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("TestBasicM1ProjectAuditor");
		project = (ITigerstripeM1GeneratorProject) TigerstripeCore
				.createProject(projectDetails, null,
						ITigerstripeM1GeneratorProject.class, null, null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	/**
	 * Upon creation a Model project has no version/description which should
	 * trigger a warning and an info.
	 * 
	 */
	public void testInitialWarningInfo() throws CoreException,
			InterruptedException, TigerstripeException {

		// Force the auditor
		AuditorHelper.forceFullBuildNow(project);

		IMarker[] markers = AuditorHelper.getMarkers(project);
		assertNotNull(markers);
		assertTrue(markers.length == 2);

		int warning = AuditorHelper.getMarkers(IMarker.SEVERITY_WARNING,
				project).length;
		int info = AuditorHelper.getMarkers(IMarker.SEVERITY_INFO, project).length;
		assertTrue(info == 1);
		assertTrue(warning == 1);

		// Then add a description
		ITigerstripeM1GeneratorProject p = (ITigerstripeM1GeneratorProject) project
				.makeWorkingCopy(null);
		p.getProjectDetails().setDescription("A description");
		p.commit(null);

		AuditorHelper.forceFullBuildNow(project);
		warning = AuditorHelper.getMarkers(IMarker.SEVERITY_WARNING, project).length;
		info = AuditorHelper.getMarkers(IMarker.SEVERITY_INFO, project).length;
		assertTrue(info == 0);
		assertTrue(warning == 1);

		// and a version
		p = (ITigerstripeM1GeneratorProject) project.makeWorkingCopy(null);
		p.getProjectDetails().setVersion("1.0");
		p.commit(null);

		AuditorHelper.forceFullBuildNow(project);
		warning = AuditorHelper.getMarkers(IMarker.SEVERITY_WARNING, project).length;
		info = AuditorHelper.getMarkers(IMarker.SEVERITY_INFO, project).length;
		assertTrue(info == 0);
		assertTrue(warning == 0);
	}

}
