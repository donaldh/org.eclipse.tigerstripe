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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ProjectModelManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Test all aspects of a ManagedEntity making sure whatever was true on the
 * deprecated interface is still true after migration to EMF-based metamodel.
 * 
 * @author erdillon
 * 
 */
public class TestManagedEntityMigration extends TestCase {

	private ITigerstripeModelProject project;

	protected void setUp() throws Exception {
		super.setUp();

		// Create a Test Project to use for this test
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		project = (ITigerstripeModelProject) TigerstripeCore
				.createProject("TestManagedEntityMigration", details, null,
						ITigerstripeModelProject.class, null,
						new NullProgressMonitor());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (project != null)
			project.delete(true, null);
	}

	/**
	 * Simple create from Old to New API
	 * 
	 * @throws Exception
	 */
	public void testSimpleOtoNCreate() throws Exception {
		IArtifactManagerSession session = project.getArtifactManagerSession();

		IManagedEntityArtifact mea = (IManagedEntityArtifact) session
				.makeArtifact(IManagedEntityArtifact.class.getName());
		mea.setName("Mea");
		mea.setPackage("testON");
		mea.doSave(new NullProgressMonitor());

		// THIS IS MANDATORY BECAUSE THE OLD API WAS OUTSIDE OF ECLIPSE
		// RESOURCES
		IProject iProject = (IProject) project.getAdapter(IProject.class);
		iProject.refreshLocal(IResource.DEPTH_INFINITE,
				new NullProgressMonitor());

		ProjectModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact nMea = (org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact) repo
				.getEObjectByKey("testON.Mea");
		assertNotNull(nMea);
	}

	public void testSimpleNtoOCreate() throws Exception {
		ProjectModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact nMea = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		nMea.setName("Mea");
		nMea.setPackage("testNO");
		repo.store(nMea, true);

		final org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact fnMea = nMea;

		// At this stage, you need to be in the edit domain to make changes to
		// nMea
		repo.getEditingDomain().getCommandStack().execute(
				new AbstractCommand() {

					@Override
					public boolean canExecute() {
						return true;
					}

					public void execute() {
						fnMea.setAbstract(true);
					}

					public void redo() {
						// TODO Auto-generated method stub

					}

				});

		IArtifactManagerSession session = project.getArtifactManagerSession();
		session.refresh(true, new NullProgressMonitor()); // required

		IManagedEntityArtifact oMea = (IManagedEntityArtifact) session
				.getArtifactByFullyQualifiedName("testNO.Mea");
		assertNotNull(oMea);
	}

}
