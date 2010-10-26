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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.test.utils.Helper;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.eclipse.tigerstripe.repository.metamodel.pojo.internal.AllOutOfSyncPojosVisitor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAllOutOfSyncPojosVisitor {

	private static String PROJECTNAME = "AllOutOfSyncPojosVisitorTests";
	private static IJavaProject jProject = null;

	private static MultiFileArtifactRepository repo = null;

	@BeforeClass
	public static void setup() throws Exception {
		jProject = Helper.createJavaProject(PROJECTNAME);
		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		repo = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Helper.tearDown(jProject);
	}

	@Test
	public void testInitialNewPojos() throws Exception {
		IManagedEntityArtifact me1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me1.setName("Me1");
		me1.setPackage("com.mycompany");

		repo.store(me1, true);

		AllOutOfSyncPojosVisitor visitor = new AllOutOfSyncPojosVisitor(repo);
		IResource topRepoRes = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(repo.getURI().toPlatformString(true));
		topRepoRes.accept(visitor);

		Assert.assertTrue(visitor.getNewPojos().size() == 0);
		Assert.assertTrue(visitor.getDeletedPojos().size() == 0);
		Assert.assertTrue(visitor.getPojosToRefresh().size() == 0);

		URI me1URI = repo.normalizedURI(me1);
		IResource me1Res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				me1URI.toPlatformString(true));
		// faking a change occured on res outside of EMF
		me1Res.touch(null);

		visitor.reset();
		topRepoRes.accept(visitor);
		Assert.assertTrue(visitor.getNewPojos().size() == 0);
		Assert.assertTrue(visitor.getDeletedPojos().size() == 0);
		Assert.assertTrue(visitor.getPojosToRefresh().size() == 1);

		// Faking a new artifact appeared thru a copy
		IPath newPath = new Path("try.java");
		me1Res.copy(newPath, true, null);

		visitor.reset();
		topRepoRes.accept(visitor);
		Assert.assertTrue(visitor.getNewPojos().size() == 1);
		Assert.assertTrue(visitor.getDeletedPojos().size() == 0);
		Assert.assertTrue(visitor.getPojosToRefresh().size() == 1);

		// Faking artifact disappeared thru delete
		me1Res.delete(true, null);

		visitor.reset();
		topRepoRes.accept(visitor);
		Assert.assertTrue(visitor.getNewPojos().size() == 1);
		Assert.assertTrue(visitor.getDeletedPojos().size() == 1);
		Assert.assertTrue(visitor.getPojosToRefresh().size() == 0);

	}

	@Test
	public void testRepositoryRefreshOnDelete() throws Exception {
		IManagedEntityArtifact me1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me1.setName("Me11");
		me1.setPackage("com.mycompany");

		repo.store(me1, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		IAbstractArtifact art = repo_r
				.getArtifactByFullyQualifiedName("com.mycompany.Me11");
		Assert.assertNotNull(art);

		URI me1URI = repo.normalizedURI(me1);
		IResource me1Res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				me1URI.toPlatformString(true));

		// faking a change occured on res outside of EMF
		me1Res.delete(true, null);

		repo_r.refresh();
		art = repo_r.getArtifactByFullyQualifiedName("com.mycompany.Me11");
		Assert.assertNull(art);
	}
}
