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
import org.eclipse.tigerstripe.metamodel.IAssociationArtifact;
import org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact;
import org.eclipse.tigerstripe.metamodel.IDatatypeArtifact;
import org.eclipse.tigerstripe.metamodel.IDependencyArtifact;
import org.eclipse.tigerstripe.metamodel.IEnumArtifact;
import org.eclipse.tigerstripe.metamodel.IEventArtifact;
import org.eclipse.tigerstripe.metamodel.IExceptionArtifact;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.IPrimitiveType;
import org.eclipse.tigerstripe.metamodel.IQueryArtifact;
import org.eclipse.tigerstripe.metamodel.ISessionArtifact;
import org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.test.utils.TestHelper;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AllArtifactsTests {

	private static String PROJECTNAME = "AllArtifactsTests";
	private static IJavaProject jProject = null;

	private static MultiFileArtifactRepository repo = null;

	@BeforeClass
	public static void setup() throws Exception {
		jProject = TestHelper.createJavaProject(PROJECTNAME);
		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		repo = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		TestHelper.tearDown(jProject);
	}

	@Test
	public void testManagedEntity() throws Exception {
		IManagedEntityArtifact art = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		art.setName("Me1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.Me1") instanceof IManagedEntityArtifact);
	}

	@Test
	public void testDatatype() throws Exception {
		IDatatypeArtifact art = MetamodelFactory.eINSTANCE
				.createIDatatypeArtifact();
		art.setName("D1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.D1") instanceof IDatatypeArtifact);
	}

	@Test
	public void testException() throws Exception {
		IExceptionArtifact art = MetamodelFactory.eINSTANCE
				.createIExceptionArtifact();
		art.setName("E1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.E1") instanceof IExceptionArtifact);
	}

	@Test
	public void testEnum() throws Exception {
		IEnumArtifact art = MetamodelFactory.eINSTANCE.createIEnumArtifact();
		art.setName("En1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.En1") instanceof IEnumArtifact);
	}

	@Test
	public void testEvent() throws Exception {
		IEventArtifact art = MetamodelFactory.eINSTANCE.createIEventArtifact();
		art.setName("Ev1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.Ev1") instanceof IEventArtifact);
	}

	@Test
	public void testQuery() throws Exception {
		IQueryArtifact art = MetamodelFactory.eINSTANCE.createIQueryArtifact();
		art.setName("Q1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.Q1") instanceof IQueryArtifact);
	}

	@Test
	public void testPrimitiveType() throws Exception {
		IPrimitiveType art = MetamodelFactory.eINSTANCE.createIPrimitiveType();
		art.setName("P1");
		art.setPackage("primitive");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("primitive.P1") instanceof IPrimitiveType);
	}

	@Test
	public void testSession() throws Exception {
		ISessionArtifact art = MetamodelFactory.eINSTANCE
				.createISessionArtifact();
		art.setName("S1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.S1") instanceof ISessionArtifact);
	}

	@Test
	public void testUpdateProcedure() throws Exception {
		IUpdateProcedureArtifact art = MetamodelFactory.eINSTANCE
				.createIUpdateProcedureArtifact();
		art.setName("Up1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.Up1") instanceof IUpdateProcedureArtifact);
	}

	@Test
	public void testDependency() throws Exception {
		IDependencyArtifact art = MetamodelFactory.eINSTANCE
				.createIDependencyArtifact();
		art.setName("Dep1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.Dep1") instanceof IDependencyArtifact);
	}

	@Test
	public void testAssociation() throws Exception {
		IAssociationArtifact art = MetamodelFactory.eINSTANCE
				.createIAssociationArtifact();
		art.setName("A1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.A1") instanceof IAssociationArtifact);
	}

	@Test
	public void testAssociationClass() throws Exception {
		IAssociationClassArtifact art = MetamodelFactory.eINSTANCE
				.createIAssociationClassArtifact();
		art.setName("Ac1");
		art.setPackage("com.mycompany");

		repo.store(art, true);

		URI uri = URI.createPlatformResourceURI(jProject.getProject()
				.findMember("src").getFullPath().toString(), true);
		MultiFileArtifactRepository repo_r = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
				.createRepository(uri);

		Assert
				.assertTrue(repo_r.getEObjectByKey("com.mycompany.Ac1") instanceof IAssociationClassArtifact);
	}
}
