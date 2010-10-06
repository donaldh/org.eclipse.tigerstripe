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
package org.eclipse.tigerstripe.metamodel.test.base;

import junit.framework.Assert;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.test.utils.Helper;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestBasicLocalPojoRepository {

	private static String PROJECTNAME = "BasicLocalPojoRepositoryTests";

	private static IJavaProject jProject = null;

	@BeforeClass
	public static void setup() throws Exception {
		jProject = Helper.createJavaProject(PROJECTNAME);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Helper.tearDown(jProject);
	}

	@Test
	public void testCreateLocalPojoRepository() throws Exception {
		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		IModelRepository repo = ModelRepositoryFactory.INSTANCE
				.createRepository(uri);
		Assert.assertTrue(repo instanceof MultiFileArtifactRepository);
	}

	@Test
	public void testBasicLocalPojoStore() throws Exception {
		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		IModelRepository repo = ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		IManagedEntityArtifact me = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me.setName("Me");
		me.setPackage("com.mycompany");

		repo.store(me, true);

		IModelRepository repo_r = ModelRepositoryFactory.INSTANCE
				.createRepository(uri);
		IManagedEntityArtifact me2 = (IManagedEntityArtifact) repo_r
				.getEObjectByKey("com.mycompany.Me");

		org.junit.Assert.assertNotNull(me2);

	}
}
