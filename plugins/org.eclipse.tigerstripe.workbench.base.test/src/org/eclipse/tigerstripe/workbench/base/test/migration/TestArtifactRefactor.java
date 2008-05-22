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

import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ProjectModelManager;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestArtifactRefactor extends TestCase {

	private ITigerstripeModelProject project;

	protected void setUp() throws Exception {
		super.setUp();

		// Create a Test Project to use for this test
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		details.setName("TestArtifactRefactor");
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				details, null, ITigerstripeModelProject.class, null,
				new NullProgressMonitor());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (project != null)
			project.delete(true, null);
	}

	public void testArtifactRename() throws Exception {
		ProjectModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		IManagedEntityArtifact nMea = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		nMea.setName("Mea");
		nMea.setPackage("com.mycompany.testNO");
		mMgr.store(nMea, true);

		URI oldUri = nMea.eResource().getURI();
		final IManagedEntityArtifact fMea = nMea;

		TransactionalEditingDomain editingDomain = repo.getEditingDomain();
		editingDomain.getCommandStack().execute(new AbstractCommand() {

			@Override
			public boolean canExecute() {
				return true;
			}

			public void execute() {
				fMea.setName("newName");
				fMea.setPackage("com.moo");
			}

			public void redo() {
				// TODO Auto-generated method stub

			}

		});

		URI newURI = nMea.eResource().getURI();

		// Check that the old POJO was removed from disk
		IResource oldPojo = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(oldUri.toPlatformString(true));
		assertTrue(oldPojo == null);

		// Check the new one is there
		IResource newPojo = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(newURI.toPlatformString(true));
		assertTrue(newPojo instanceof IFile && newPojo.exists());

		Collection<EObject> allArts = repo.getAllEObjects();
		assertTrue(allArts.size() == 1);
		assertTrue("com.moo.newName".equals(((IAbstractArtifact) allArts
				.iterator().next()).getFullyQualifiedName()));
	}
}
