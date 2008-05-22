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

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.core.test.Activator;
import org.eclipse.tigerstripe.repository.core.test.providers.RegionProvider;
import org.eclipse.tigerstripe.repository.core.test.sample.Region;
import org.eclipse.tigerstripe.repository.core.test.sample.SampleFactory;
import org.eclipse.tigerstripe.repository.core.test.sample.Street;
import org.eclipse.tigerstripe.repository.core.test.sample.Village;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.repository.manager.KeyService;
import org.eclipse.tigerstripe.repository.manager.ModelManager;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.test_common.ResourcesUtils;
import org.eclipse.tigerstripe.test_common.TestHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRepositoryRefs {

	private final static String PROJECTNAME = "TestRepositoryRefs";
	private static IProject project = null;

	public final static String REPO1 = PROJECTNAME + "/repo1."
			+ RegionProvider.REPO_EXT;
	public final static String REPO2 = PROJECTNAME + "/repo2."
			+ RegionProvider.REPO_EXT;
	public final static String REPO = PROJECTNAME + "/resources/repo."
			+ RegionProvider.REPO_EXT;

	private static URI REPO1_URI = URI.createPlatformResourceURI(REPO1, true);
	private static URI REPO2_URI = URI.createPlatformResourceURI(REPO2, true);
	private static URI REPO_URI = URI.createPlatformResourceURI(REPO, true);

	@BeforeClass
	public static void setupTestProject() throws Exception {
		project = TestHelper.createProject(PROJECTNAME);
	}

	@AfterClass
	public static void tearDownTestProject() throws Exception {
		TestHelper.tearDownProject(project);
	}

	/**
	 * A Single reference EFeature to an EObject where an IDEAttribute was
	 * defined.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSingleMultiIDRef() throws Exception {

		ModelManager manager = new ModelManager();

		Region r1 = SampleFactory.eINSTANCE.createRegion();
		r1.setName("r1");
		Village v1 = SampleFactory.eINSTANCE.createVillage();
		v1.setName("v1");
		r1.getVillages().add(v1);
		Village v2 = SampleFactory.eINSTANCE.createVillage();
		v2.setName("v2");
		r1.getVillages().add(v2);
		v1.getNeighboringVillages().add(v2);

		Region r2 = SampleFactory.eINSTANCE.createRegion();
		r2.setName("r2");
		r1.setNextTo(r2); // inside same repository
		Village v3 = SampleFactory.eINSTANCE.createVillage();
		v3.setName("v3");
		r2.getVillages().add(v3);
		v1.getNeighboringVillages().add(v3);

		IModelRepository repo1 = ModelRepositoryFactory.INSTANCE
				.createRepository(REPO1_URI);
		repo1.store(r1, true);

		IModelRepository repo2 = ModelRepositoryFactory.INSTANCE
				.createRepository(REPO2_URI);
		repo2.store(r2, true);

		IModelRepository repo1_r = ModelRepositoryFactory.INSTANCE
				.createRepository(REPO1_URI);
		manager.appendRepository(repo1_r);
		IModelRepository repo2_r = ModelRepositoryFactory.INSTANCE
				.createRepository(REPO2_URI);
		manager.appendRepository(repo2_r);

		Region r1_r = (Region) repo1_r.getEObjectByKey("r1");
		Assert.assertNotNull(r1_r);
		Assert.assertEquals(r1_r.getName(), "r1");

		Village v1_r = (Village) repo1_r.getEObjectByKey("v1");
		Assert.assertNotNull(v1_r);

		Village v2_r = (Village) repo1_r.getEObjectByKey("v2");
		Assert.assertNotNull(v2_r);
		EList<Village> villages = v1_r.getNeighboringVillages();
		Assert.assertTrue(villages.size() == 2);
		Assert.assertTrue(villages.get(0).equals(v2_r));

		Village v3_r = (Village) manager.getEObjectByKey("v3");
		Assert.assertNotNull(v3_r);
		Assert.assertEquals(villages.get(1), v3_r);
	}

	@Test
	public void testSingleMultiHRef() throws Exception {

		Region r1 = SampleFactory.eINSTANCE.createRegion();
		r1.setName("r1");
		Village v1 = SampleFactory.eINSTANCE.createVillage();
		v1.setName("v1");
		r1.getVillages().add(v1);

		Street s1 = SampleFactory.eINSTANCE.createStreet();
		s1.setName("main");
		v1.getStreets().add(s1);
		Street s2 = SampleFactory.eINSTANCE.createStreet();
		s2.setName("wall");
		v1.getStreets().add(s2);
		Street s3 = SampleFactory.eINSTANCE.createStreet();
		s3.setName("bell");
		v1.getStreets().add(s3);

		s1.getCrossStreets().add(s2);
		s1.getCrossStreets().add(s3);
		s2.getCrossStreets().add(s1);
		s3.getCrossStreets().add(s1);

		IModelRepository repo1 = ModelRepositoryFactory.INSTANCE
				.createRepository(REPO1_URI);
		repo1.store(r1, true);

		IModelRepository repo1_r = ModelRepositoryFactory.INSTANCE
				.createRepository(REPO1_URI);
		Village v1_r = (Village) repo1_r.getEObjectByKey("v1");
		Assert.assertNotNull(v1_r);
		Assert.assertTrue(v1_r.getStreets().size() == 3);
	}

	@Test
	public void testUnresolvedRef() throws Exception {
		ResourcesUtils.copyAll(project, Activator.getDefault().getBundleUtils()
				.getContext(), this);
		IModelRepository repo = ModelRepositoryFactory.INSTANCE
				.createRepository(REPO_URI);

		Region lorraine = (Region) repo.getEObjectByKey("lorraine");
		Assert.assertNotNull(lorraine);

		Region next = lorraine.getNextTo();
		Assert.assertNotNull(next);
		Object key = KeyService.INSTANCE.getKey(next);
		Assert.assertTrue("champagne".equals(key));
		Assert.assertTrue(next.eIsProxy());
	}
}
