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
package org.eclipse.tigerstripe.workbench.base.test.migration;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IModelManager;
import org.eclipse.tigerstripe.workbench.model.IModelRepository;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestModelManager extends TestCase {

	private ITigerstripeModelProject projectA;

	protected void setUp() throws Exception {
		super.setUp();

		// Create a Test Project to use for this test
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		details.setName("projectA");
		projectA = (ITigerstripeModelProject) TigerstripeCore.createProject(
				details, null, ITigerstripeModelProject.class, null,
				new NullProgressMonitor());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (projectA != null)
			projectA.delete(true, null);
	}

	public void testGetModelManager() throws TigerstripeException {
		IModelManager manager = projectA.getModelManager();
		assertNotNull(manager);
	}

	public void testBasicLoad() throws TigerstripeException {
		// IModelManager manager = projectA.getModelManager();
		// assertNotNull(manager);
		//
		// IProject proj = (IProject) projectA.getAdapter(IProject.class);
		// assertNotNull(proj);
		// IFile manuallyCreated = proj.getFile("src/com/truc/Lalala.java");
		// String content = "com.truc.Lalala";
		// try {
		// manuallyCreated.create(new StringBufferInputStream(content), true,
		// null);
		// } catch (CoreException e) {
		// throw new TigerstripeException("While creating manually: "
		// + e.getMessage(), e);
		// }
		//
		// AbstractArtifact[] arts = manager.findAllArtifacts(content);
		// for (AbstractArtifact art : arts) {
		// System.out.println("art=" + art);
		// }
	}

	public void testBasicSave() throws TigerstripeException {
		IModelManager manager = projectA.getModelManager();
		assertNotNull(manager);
		IModelRepository repo = manager.getDefaultRepository();

		IManagedEntityArtifact other = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		other.setName("Inh");
		other.setPackage("com.truc");
		repo.store(other, true);

		IManagedEntityArtifact mea = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		mea.setName("Meuh");
		mea.setPackage("com.mycompany");
		mea.setExtendedArtifact(other);
		repo.store(mea, true);

		// DatatypeArtifact dta = MetamodelFactory.eINSTANCE
		// .createDatatypeArtifact();
		// dta.setName("Data");
		// dta.setPackage("com.truc");
		// dta.setExtends(mea);
		// repo.store(dta, true);

		IAbstractArtifact[] allArts = manager.getAllArtifacts(false);
		assertTrue(allArts.length == 2);
		for (IAbstractArtifact art : allArts) {
			System.out.println(art);
		}

		repo.refresh(null);

		IManagedEntityArtifact art = (IManagedEntityArtifact) repo
				.getArtifactByFullyQualifiedName("com.mycompany.Meuh");
		assertNotNull(art);
		IManagedEntityArtifact extArt = (IManagedEntityArtifact) art
				.getExtendedArtifact();
		System.out.println("extArt=" + extArt.getFullyQualifiedName());
	}

}
