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
package org.eclipse.tigerstripe.workbench.base.test.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeChangeAdapter;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.base.test.utils.M1ProjectHelper;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public class TestTigerstripeWorkspaceNotifications extends AbstractTigerstripeTestCase {

	private IAbstractTigerstripeProject project = null;

	private volatile IModelChangeDelta[] deltas;
	private volatile IAbstractTigerstripeProject addedProject;
	private volatile String removedProject;

	@Override
	protected void setUp() throws Exception {
		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				for(final IProject project : workspace.getRoot().getProjects()) {
					project.delete(true, null);
				}
			}
		});
		waitForUpdates();
	}

	@Override
	protected void tearDown() throws Exception {
		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				if (project != null && project.exists())
					project.delete(true, null);
			}
		});
		waitForUpdates();
	}

	public void testProjectNotifications() throws Exception {

		resetNotifications();
		registerSelf(ITigerstripeChangeListener.PROJECT);

		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				resetNotifications();
				project = ModelProjectHelper
						.createModelProject("testProjectCreationNotification");
			}
		});
		
		waitForUpdates();

		assertFalse("Project deletion must not be broadcasted", checkDeletedProject());
		assertTrue("Project deletion must be broadcasted", checkAddedProject());
		assertEquals(addedProject, project);
		resetNotifications();

		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				project.delete(true, null);
			}
		});

		waitForUpdates();
		assertFalse("Project addition must not be broadcasted", checkAddedProject());
		assertTrue("Project deletion must be broadcasted", checkDeletedProject());
		assertEquals(removedProject, "testProjectCreationNotification");
		resetNotifications();

		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				project = M1ProjectHelper.createM1Project(
						"M1testProjectCreationNotification", false);
			}
		});
		
		waitForUpdates();

		assertTrue("Project addition must be broadcasted", checkAddedProject());
		assertEquals(addedProject, project);
		resetNotifications();

		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				project.delete(true, null);
			}
		});

		waitForUpdates();
		assertTrue("Project deletion must be broadcasted", checkDeletedProject());
		assertEquals(removedProject, "M1testProjectCreationNotification");
		resetNotifications();

		unregisterSelf();
	}

	// public void testModelNotifications() throws Exception {
	//
	// resetNotifications();
	// registerSelf(MODEL);
	//
	// ITigerstripeModelProject mProj = ModelProjectHelper
	// .createModelProject("testModelNotifications");
	//
	// IArtifactManagerSession session = mProj.getArtifactManagerSession();
	// IAbstractArtifact art = session
	// .makeArtifact(IManagedEntityArtifact.class.getName());
	// art.setFullyQualifiedName("com.mycompany.Test");
	// art.doSave(null);
	// assertTrue(checkDeltas());
	//
	// IModelChangeDelta delta = deltas[0];
	// assertTrue(delta.getAddedArtifacts().length == 1);
	// assertTrue(delta.getDeletedArtifacts().length == 0);
	// assertTrue(delta.getRenamedArtifacts().length == 0);
	// assertTrue(delta.getChangedArtifacts().length == 0);
	//
	// session.renameArtifact(art, "com.mycompany.Truc");
	// assertTrue(checkDeltas());
	//
	// delta = deltas[0];
	// assertTrue(delta.getAddedArtifacts().length == 0);
	// assertTrue(delta.getDeletedArtifacts().length == 0);
	// assertTrue(delta.getRenamedArtifacts().length == 1);
	// assertTrue(delta.getChangedArtifacts().length == 0);
	//		
	// IField field = art.makeField();
	// field.setName("f");
	// IType type = field.makeType();
	// type.setFullyQualifiedName("int");
	// field.setType(type);
	// art.addField(field);
	// art.doSave(null);
	// assertTrue(checkDeltas());
	//
	// delta = deltas[0];
	// assertTrue(delta.getAddedArtifacts().length == 0);
	// assertTrue(delta.getDeletedArtifacts().length == 0);
	// assertTrue(delta.getRenamedArtifacts().length == 0);
	// assertTrue(delta.getChangedArtifacts().length == 1);
	//
	// session.removeArtifact(art);
	// assertTrue(checkDeltas());
	//
	// delta = deltas[0];
	// assertTrue(delta.getAddedArtifacts().length == 0);
	// assertTrue(delta.getDeletedArtifacts().length == 1);
	// assertTrue(delta.getRenamedArtifacts().length == 0);
	// assertTrue(delta.getChangedArtifacts().length == 0);
	//		
	// mProj.delete(true, null);
	// unregisterSelf();
	// }

	protected void registerSelf(int level) {
		TigerstripeCore.addTigerstripeChangeListener(tigerstripeListener, level);
	}

	protected void unregisterSelf() {
		TigerstripeCore.removeTigerstripeChangeListener(tigerstripeListener);
	}

	private void resetNotifications() {
		deltas = null;
		addedProject = null;
		removedProject = null;
	}

	protected boolean checkAddedProject() throws Exception {
		return addedProject != null;
	}

	protected boolean checkDeletedProject() throws Exception {
		return removedProject != null;
	}

	protected boolean checkDeltas() throws Exception {
		return deltas != null;
	}

    private final ITigerstripeChangeListener tigerstripeListener = new TigerstripeChangeAdapter() {
    	public void modelChanged(IModelChangeDelta[] delta) {
    		deltas = delta;
    	}

    	public void projectAdded(IAbstractTigerstripeProject project) {
    		addedProject = project;
    	}

    	public void projectDeleted(String project) {
    		removedProject = project;
    	}
    };
}
