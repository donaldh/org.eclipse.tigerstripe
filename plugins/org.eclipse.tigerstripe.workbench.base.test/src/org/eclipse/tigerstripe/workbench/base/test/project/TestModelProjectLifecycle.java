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
package org.eclipse.tigerstripe.workbench.base.test.project;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestModelProjectLifecycle extends TestCase {

	public void testGetWorkingCopy() throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject("testGetWorkingCopy", projectDetails, null,
						ITigerstripeModelProject.class, null,
						new NullProgressMonitor());
		assertNotNull(project);

		ITigerstripeModelProject workingCopy = (ITigerstripeModelProject) project
				.makeWorkingCopy(null);

		assertNotNull(workingCopy);
		assertTrue(project != workingCopy);
		assertTrue(workingCopy.isWorkingCopy());

		IProjectDetails detailsCopy = workingCopy.getProjectDetails();
		assertTrue(detailsCopy != projectDetails);

		project.delete(true, null);
	}

	//TODO tests of the lifecycle are useless for now as this is not implemented.
//	public void testSetProjectDetails() throws TigerstripeException {
//		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
//
//		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
//				.createProject("testSetOnOriginal", projectDetails, null,
//						ITigerstripeModelProject.class, null,
//						new NullProgressMonitor());
//		assertNotNull(project);
//
//		assertTrue(!project.isWorkingCopy());
//
//		try {
//			project.getProjectDetails().setDescription("changed");
//			fail("Shouldn't allow to set project details on original.");
//		} catch (WorkingCopyException e) {
//			// let's try on a working copy now
//			ITigerstripeModelProject workingCopy = (ITigerstripeModelProject) project
//					.makeWorkingCopy(null);
//			try {
//				workingCopy.getProjectDetails().setDescription("NowChanged");
//			} catch (WorkingCopyException ee) {
//				fail("Set on working copy shouldn't fail.");
//			}
//
//			workingCopy.commit(null);
//
//			IProjectDetails finalDetails = project.getProjectDetails();
//			assertTrue(finalDetails.getDescription().equals("NowChanged"));
//		}
//	}

//	public void testOriginalChangedCallback() throws TigerstripeException {
//
//	}
}
