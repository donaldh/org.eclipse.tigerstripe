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
package org.eclipse.tigerstripe.repository.core.test.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.core.test.sample.Region;
import org.eclipse.tigerstripe.repository.core.test.sample.SampleFactory;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.repository.manager.KeyService;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestProviders {

	private final static String PROJECTNAME = "TestProviders";
	private static IProject project;

	public final static String REGION1 = PROJECTNAME + "/r1.region";

	/**
	 * Set up a project to use for the tests
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setupProject() throws Exception {

		// Create an empty project for this
		IProjectDescription desc = ResourcesPlugin.getWorkspace()
				.newProjectDescription(PROJECTNAME);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PROJECTNAME);
		project.create(desc, null);
		project.open(null);
	}

	@AfterClass
	public static void clean() throws Exception {
		if (project != null) {
			project.delete(true, null);
		}
	}

	@Test
	public void testRegionProvider() throws Exception {
		URI r1URI = URI.createPlatformResourceURI(REGION1, true);
		IModelRepository repo = ModelRepositoryFactory.INSTANCE
				.createRepository(r1URI);
		assertNotNull("Repository could be created for: " + r1URI, repo);

		Region r1 = SampleFactory.eINSTANCE.createRegion();
		r1.setName("lorraine");
		Object key = KeyService.INSTANCE.getKey(r1);
		assertTrue("Unexpected key for: " + r1 + ", got: " + key, "lorraine"
				.equals(key));
	}
}
