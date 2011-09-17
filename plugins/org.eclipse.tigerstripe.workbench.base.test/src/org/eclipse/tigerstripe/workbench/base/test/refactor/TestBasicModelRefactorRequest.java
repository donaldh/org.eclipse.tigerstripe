/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.refactor;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.base.test.utils.CvsHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;

public class TestBasicModelRefactorRequest extends TestCase {

	private String TS_REPOSITORY = ":pserver:anonymous@dev.eclipse.org:/cvsroot/technology";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		checkoutProject();
	}

	public void checkoutProject() throws Exception {
		CvsHelper helper = new CvsHelper(TS_REPOSITORY);
		helper
				.checkout(
						"org.eclipse.tigerstripe/samples/test-models/model-refactoring",
						"model-refactoring");
	}

	public void testCreateValidModelRefactorRequests() throws Exception {
		IProject testProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject("model-refactoring");
		ITigerstripeModelProject project = (ITigerstripeModelProject) testProject
				.getAdapter(ITigerstripeModelProject.class);
		assertNotNull(project);
		IAbstractArtifact ent1 = project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName("simple.Ent1");
		assertNotNull(ent1);

		ModelRefactorRequest req = new ModelRefactorRequest();
		req.setOriginal(project, "simple.Ent1");
		req.setDestination(project, "simple.Ent11");
		assertEquals(IStatus.OK, req.isValid().getSeverity());

		req.setOriginal(project, "simple.Ent1");
		req.setDestination(project, "");
		assertEquals(IStatus.ERROR, req.isValid().getSeverity());

		req.setOriginal(project, "simple.Ent1");
		req.setDestination(project, "simple.Ent2");
		assertEquals(IStatus.ERROR, req.isValid().getSeverity());

		req.setOriginal(project, "simple.Ent1");
		req.setDestination(project, "simple.enT1");
		assertEquals(IStatus.ERROR, req.isValid().getSeverity());
	}

	public void testTargetsForEnt2Rename() throws Exception {
		// This is expected to touch
		// Ent2, Ent1 (extends from Ent2)

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.findProject("model-refactoring");
		ModelRefactorRequest req = new ModelRefactorRequest();
		req.setOriginal(project, "simple.Ent2");
		req.setDestination(project, "simple.Ent22");

		IRefactorCommand cmd = req.getCommand(null);
		cmd.execute(null);

		assertNull(project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName("simple.Ent2"));
		assertNotNull(project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName("simple.Ent22"));
	}

	public void testComplexEnt1Rename() throws Exception {
		// This is expected to touch
		// Ent2, Ent1 (extends from Ent2)

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.findProject("model-refactoring");
		ModelRefactorRequest req = new ModelRefactorRequest();
		req.setOriginal(project, "simple.Ent1");
		req.setDestination(project, "simple.Ent11");

		IRefactorCommand cmd = req.getCommand(null);
		cmd.execute(null);

		assertNull(project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName("simple.Ent1"));
		assertNotNull(project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName("simple.Ent11"));
	}

	public void testTargetsForSimplePackageRename() throws Exception {
		// This is expected to touch
		// Ent2, Ent1 (extends from Ent2)

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.findProject("model-refactoring");
		ModelRefactorRequest req = new ModelRefactorRequest();
		req.setOriginal(project, "simple");
		req.setDestination(project, "complex");

		IRefactorCommand cmd = req.getCommand(null);
		cmd.execute(null);
		assertNull(project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName("simple.Ent22"));
		assertNotNull(project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName("complex.Ent22"));
	}
}
