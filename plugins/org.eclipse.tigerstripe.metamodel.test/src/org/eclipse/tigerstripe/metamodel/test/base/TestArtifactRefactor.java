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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.test.utils.Helper;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.repository.manager.internal.SafeExecutableAbstractCommand;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestArtifactRefactor {

	private static String PROJECTNAME = "ArtifactRefactorTest";
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
	public void testSimpleArtifactRename() throws Exception {
		final IManagedEntityArtifact me1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me1.setName("Me1");
		me1.setPackage("com.mycompany");

		repo.store(me1, true);

		IResource me1Res = repo.getIResource(me1);

		TransactionalEditingDomain domain = repo.getEditingDomain();
		domain.getCommandStack().execute(new SafeExecutableAbstractCommand() {
			@Override
			protected void run() throws Exception {
				me1.setName("Mee1");
			}
		});

		IResource mee1Res = repo.getIResource(me1);
		Assert.assertTrue(me1Res != null && !me1Res.exists());
		Assert.assertTrue(mee1Res != null && mee1Res.exists());
		Assert.assertNull(repo
				.getArtifactByFullyQualifiedName("com.mycompany.Me1"));
		Assert.assertNotNull(repo
				.getArtifactByFullyQualifiedName("com.mycompany.Mee1"));
	}

	@Test
	public void testRefactorWithinReference() throws Exception {
		final IManagedEntityArtifact me1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me1.setName("Me1");
		me1.setPackage("com.mycompany");

		final IManagedEntityArtifact me2 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me2.setName("Me2");
		me2.setPackage("com.mycompany");
		me1.setExtendedArtifact(me2);
		
		repo.store(me1, true);
		repo.store(me2, true);

		IResource me1Res = repo.getIResource(me1);
		long tstamp = me1Res.getModificationStamp();

		// rename me2 and expect me1Res to be touched
		TransactionalEditingDomain domain = repo.getEditingDomain();
		domain.getCommandStack().execute(new SafeExecutableAbstractCommand() {
			@Override
			protected void run() throws Exception {
				me2.setName("Mee2");
			}
		});
		
		// Check that me1 was updated
		Assert.assertTrue( me1Res.getModificationStamp() != tstamp );

	}
}
