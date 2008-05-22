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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.core.test.sample.Region;
import org.eclipse.tigerstripe.repository.core.test.sample.SampleFactory;
import org.eclipse.tigerstripe.repository.core.test.sample.Village;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.repository.manager.KeyService;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.test_common.TestHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDefaultStore {

	public final static String PROJECTNAME = "testRegistration";

	public final static String LORRAINE = PROJECTNAME + "/lorraine.region";
	public final static String CHAMPAGNE = PROJECTNAME + "/champagne.region";

	private static URI LORRAINE_URI = URI.createPlatformResourceURI(LORRAINE,
			true);

	private static IProject handle = null;

	@BeforeClass
	public static void setupModelFiles() throws Exception {

		// Create an empty project for this
		handle = TestHelper.createProject(PROJECTNAME);
	}

	@AfterClass
	public static void cleanUp() throws Exception {
		TestHelper.tearDownProject(handle);
	}

	@Test(expected = ModelCoreException.class)
	public void testNoKeyNoStore() throws Exception {

		IModelRepository repo = ModelRepositoryFactory.INSTANCE
				.createRepository(LORRAINE_URI);
		Village metz = SampleFactory.eINSTANCE.createVillage();

		repo.store(metz, true);
	}

	@Test
	public void testSimpleStore_n_Remove() throws Exception {

		IModelRepository repo = ModelRepositoryFactory.INSTANCE
				.createRepository(LORRAINE_URI);
		Village metz = SampleFactory.eINSTANCE.createVillage();
		metz.setName("Metz");
		repo.store(metz, true);

		Object metzKey = KeyService.INSTANCE.getKey(metz);
		Village m = (Village) repo.getEObjectByKey(metzKey);
		assertTrue("Metz".equals(m.getName()));

		repo.removeEObjectByKey(metzKey);
		m = (Village) repo.getEObjectByKey(metzKey);
		assertNull(m);
	}

	@Test
	public void testDisjointSave() throws Exception {
		Region meuh = SampleFactory.eINSTANCE.createRegion();
		meuh.setName("meuh");
		Region beeeh = SampleFactory.eINSTANCE.createRegion();
		beeeh.setName("beeeh");
		beeeh.setNextTo(meuh);

		IModelRepository repo1 = ModelRepositoryFactory.INSTANCE
				.createRepository(URI.createPlatformResourceURI(PROJECTNAME
						+ "/beeeh.region", true));
		repo1.store(beeeh, true);
	}

}
