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
import org.eclipse.tigerstripe.metamodel.test.utils.TestHelper;
import org.eclipse.tigerstripe.repository.manager.ModelManager;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReferenceResolutionsTests {

	private static String PROJECTNAME1 = "ReferenceResolutionsTests";
	private static String PROJECTNAME2 = "ReferenceResolutionsTests2";
	private static IJavaProject jProject1 = null;
	private static IJavaProject jProject2 = null;

	private static MultiFileArtifactRepository repo1 = null;
	private static MultiFileArtifactRepository repo2 = null;

	@BeforeClass
	public static void setup() throws Exception {
		jProject1 = TestHelper.createJavaProject(PROJECTNAME1);
		URI uri1 = URI.createPlatformResourceURI(jProject1.getProject()
				.findMember("src").getFullPath().toString(), true);
		repo1 = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri1);

		jProject2 = TestHelper.createJavaProject(PROJECTNAME2);
		URI uri2 = URI.createPlatformResourceURI(jProject2.getProject()
				.findMember("src").getFullPath().toString(), true);
		repo2 = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri2);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		TestHelper.tearDown(jProject1);
		TestHelper.tearDown(jProject2);
	}

	@Test
	public void testSingleRepositoryResolution() throws Exception {
		IManagedEntityArtifact me1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me1.setName("Me1");
		me1.setPackage("com.mycompany");

		IManagedEntityArtifact me2 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me2.setName("Me2");
		me2.setPackage("com.yourcompany");
		me1.setExtendedArtifact(me2);

		repo1.store(me1, true);
		repo1.store(me2, true);

		URI uri1 = URI.createPlatformResourceURI(jProject1.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo1_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri1);

		// Test late resolution (thru proxy first)
		IManagedEntityArtifact me1_r = (IManagedEntityArtifact) repo1_r
				.getArtifactByFullyQualifiedName("com.mycompany.Me1");
		IManagedEntityArtifact me2_rl = (IManagedEntityArtifact) me1_r
				.getExtendedArtifact();
		Assert.assertTrue(me2_rl == repo1_r
				.getArtifactByFullyQualifiedName("com.yourcompany.Me2"));

		// Test direct resolution (Artifact already in repository)
		MultiFileArtifactRepository repo1_rr = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri1);
		IManagedEntityArtifact me1_rr = (IManagedEntityArtifact) repo1_rr
				.getArtifactByFullyQualifiedName("com.mycompany.Me1");
		IManagedEntityArtifact me2_rr = (IManagedEntityArtifact) repo1_rr
				.getArtifactByFullyQualifiedName("com.yourcompany.Me2");
		Assert.assertTrue(me2_rr == me1_rr.getExtendedArtifact());

	}

	@Test
	public void testMultiRepositoryResolution() throws Exception {

		ModelManager manager = new ModelManager();
		manager.appendRepository(repo1);
		manager.appendRepository(repo2);

		IManagedEntityArtifact me1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me1.setName("Me1");
		me1.setPackage("com.mycompany");

		IManagedEntityArtifact me2 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me2.setName("Me2");
		me2.setPackage("com.yourcompany");
		me1.setExtendedArtifact(me2);

		repo1.store(me1, true);
		repo2.store(me2, true);

		URI uri1 = URI.createPlatformResourceURI(jProject1.getProject()
				.findMember("src").getFullPath().toString(), true);
		URI uri2 = URI.createPlatformResourceURI(jProject2.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo1_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri1);
		MultiFileArtifactRepository repo2_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri2);
		ModelManager manager2 = new ModelManager();
		manager2.appendRepository(repo1_r);
		manager2.appendRepository(repo2_r);

		// Test late resolution (thru proxy first)
		IManagedEntityArtifact me1_r = (IManagedEntityArtifact) manager2
				.getEObjectByKey("com.mycompany.Me1");
		IManagedEntityArtifact me2_rl = (IManagedEntityArtifact) me1_r
				.getExtendedArtifact();
		Assert.assertTrue(me2_rl == manager2
				.getEObjectByKey("com.yourcompany.Me2"));

		// Test direct resolution (Artifact already in repository)
		MultiFileArtifactRepository repo1_rr = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri1);
		MultiFileArtifactRepository repo2_rr = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri2);
		ModelManager manager3 = new ModelManager();
		manager3.appendRepository(repo1_rr);
		manager3.appendRepository(repo2_rr);

		IManagedEntityArtifact me1_rr = (IManagedEntityArtifact) manager3
				.getEObjectByKey("com.mycompany.Me1");
		IManagedEntityArtifact me2_rr = (IManagedEntityArtifact) manager3
				.getEObjectByKey("com.yourcompany.Me2");
		Assert.assertTrue(me2_rr == me1_rr.getExtendedArtifact());

	}
}
