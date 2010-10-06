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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.test.utils.Helper;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRepositoryTracker {

	private static String PROJECTNAME = "RepositoryTrackerTests";
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
	public void testBasicChangesTracking() throws Exception {
		IManagedEntityArtifact me1 = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		me1.setName("Me1");
		me1.setPackage("com.mycompany");

		repo.store(me1, true);

		URI me1URI = repo.normalizedURI(me1);
		IResource me1Res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				me1URI.toPlatformString(true));
		// faking a change occured on res outside of EMF
		me1Res.touch(null);

		// Faking a new artifact appeared thru a copy
		IPath newPath = new Path("try.java");
		me1Res.copy(newPath, true, null);

		// Faking artifact disappeared thru delete
		me1Res.delete(true, null);

		System.out.println("truc");
	}
}
