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

import junit.framework.TestCase;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.base.test.utils.M1ProjectHelper;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public class TestTigerstripeWorkspaceNotifications extends TestCase implements
		ITigerstripeChangeListener {

	public void descriptorChanged(IResource changedDescriptor) {
		// TODO Auto-generated method stub
		
	}

	private final static int SLEEPTIME = 500;

	private IAbstractTigerstripeProject project = null;

	private IModelChangeDelta[] deltas;
	private IAbstractTigerstripeProject addedProject;
	private String removedProject;

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);

	}

	public void testProjectNotifications() throws Exception {

		resetNotifications();
		registerSelf(PROJECT);

		project = ModelProjectHelper
				.createModelProject("testProjectCreationNotification");
		assertFalse(checkDeletedProject());
		assertTrue(checkAddedProject());
		assertEquals(addedProject, project);
		resetNotifications();

		project.delete(true, null);
		assertFalse(checkAddedProject());
		assertTrue(checkDeletedProject());
		assertEquals(removedProject, "testProjectCreationNotification");

		project = M1ProjectHelper.createM1Project(
				"M1testProjectCreationNotification", false);
		assertTrue(checkAddedProject());
		assertEquals(addedProject, project);
		resetNotifications();

		project.delete(true, null);
		assertTrue(checkDeletedProject());
		assertEquals(removedProject, "M1testProjectCreationNotification");

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
		TigerstripeCore.addTigerstripeChangeListener(this, level);
	}

	protected void unregisterSelf() {
		TigerstripeCore.removeTigerstripeChangeListener(this);
	}

	private void resetNotifications() {
		deltas = null;
		addedProject = null;
		removedProject = null;
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		deltas = delta;
	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		addedProject = project;
	}

	public void projectDeleted(String project) {
		removedProject = project;
	}

	protected boolean checkAddedProject() throws Exception {
		Thread.sleep(SLEEPTIME);
		return addedProject != null;
	}

	protected boolean checkDeletedProject() throws Exception {
		Thread.sleep(SLEEPTIME);
		return removedProject != null;
	}

	protected boolean checkDeltas() throws Exception {
		Thread.sleep(SLEEPTIME);
		return deltas != null;
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		// TODO Auto-generated method stub

	}

	public void artifactResourceAdded(IResource addedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

	public void artifactResourceRemoved(IResource removedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

}
