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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestBasicModelProjectAuditor extends AbstractTigerstripeTestCase {

	private ITigerstripeModelProject project;

	@Override
	protected void setUp() throws Exception {
		cleanup();
		
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				"TestBasicModelProjectAuditor", projectDetails, null,
				ITigerstripeModelProject.class, null, null);
	}

	@Override
	protected void tearDown() throws Exception {
		cleanup();
	}

	/**
	 * Upon creation a Model project has no version/description which should
	 * trigger a warning and an info.
	 * 
 	 * Bug 294901 adds a default Version, so this will not be triggered at first pass.
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

		runInWorkspace(new SafeRunnable() {

			public void run() throws Exception {

				// Then add a description
				ITigerstripeModelProject p = (ITigerstripeModelProject) project
						.makeWorkingCopy(null);
				p.getProjectDetails().setDescription("A description");
				p.commit(null);
			}
		});

		waitForUpdates();
		
		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				AuditorHelper.forceFullBuildNow(project);
			}
		});
		
		waitForUpdates();
		
		warning = AuditorHelper.getMarkers(IMarker.SEVERITY_WARNING, project).length;
		IMarker[] infoMarkers = AuditorHelper.getMarkers(IMarker.SEVERITY_INFO, project);
		info = infoMarkers.length;
		StringBuffer infoData = new StringBuffer();
		for (IMarker marker : infoMarkers) {
		    infoData.append(marker.toString() + "\n");
		}
		assertEquals("Info Recieved: " + infoData.toString(), 0, info);
//		assertTrue(warning == 1);

		// and a version
//		p = (ITigerstripeModelProject) project.makeWorkingCopy(null);
//		p.getProjectDetails().setVersion("1.0");
//		p.commit(null);
//
//		AuditorHelper.forceFullBuildNow(project);
//		warning = AuditorHelper.getMarkers(IMarker.SEVERITY_WARNING, project).length;
//		info = AuditorHelper.getMarkers(IMarker.SEVERITY_INFO, project).length;
//		assertTrue(info == 0);
//		assertTrue(warning == 0);
	}

}
