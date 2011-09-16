/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     Anton Salnik (xored software, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.core.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestContextProjectAwareArtifact extends
		AbstractTigerstripeTestCase {

	private static final String PROJECT_ID = "TestProject1";

	private static final String REFERENCED_PROJECT_ID = "TestProject2";

	private ITigerstripeModelProject project;
	private ITigerstripeModelProject referencedProject;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		project = (ITigerstripeModelProject) createEmptyModelProject(
				PROJECT_ID, PROJECT_ID);

		referencedProject = (ITigerstripeModelProject) createEmptyModelProject(
				REFERENCED_PROJECT_ID, REFERENCED_PROJECT_ID);
		createEachArtifactType(referencedProject, true);

		// add reference
		ITigerstripeModelProject wc = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addModelReference(ModelReference
				.referenceFromProject(referencedProject));
		wc.commit(new NullProgressMonitor());
		assertTrue(project.getReferencedProjects().length == 1);
	}

	@Override
	protected void tearDown() throws Exception {
		deleteModelProject(project);
		deleteModelProject(referencedProject);
		super.tearDown();
	}

	public void testArtifactManager() throws Exception {
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) project
				.getArtifactManagerSession();
		ArtifactManager manager = session.getArtifactManager();

		IAbstractArtifactInternal[] models = new IAbstractArtifactInternal[] {
				ManagedEntityArtifact.MODEL, DatatypeArtifact.MODEL,
				EventArtifact.MODEL, EnumArtifact.MODEL,
				ExceptionArtifact.MODEL, QueryArtifact.MODEL,
				SessionFacadeArtifact.MODEL, UpdateProcedureArtifact.MODEL,
				AssociationArtifact.MODEL, DependencyArtifact.MODEL,
				AssociationClassArtifact.MODEL, PackageArtifact.MODEL };
		for (IAbstractArtifactInternal model : models) {
			checkExpectedContextProjectAwareArtifacts(manager
					.getArtifactsByModel(model, true, false,
							new NullProgressMonitor()));
		}

		Collection<IAbstractArtifact> allArtifacts = manager.getAllArtifacts(
				true, false, new NullProgressMonitor());
		checkExpectedContextProjectAwareArtifacts(allArtifacts);

		for (IAbstractArtifact artifact : allArtifacts) {
			IAbstractArtifact result = manager.getArtifactByFullyQualifiedName(
					artifact.getFullyQualifiedName(), true,
					new NullProgressMonitor());
			assertTrue(result instanceof IContextProjectAware);
		}
	}

	public void testEqualsHasCodeMethods() throws Exception {
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) project
				.getArtifactManagerSession();
		ArtifactManager manager = session.getArtifactManager();

		Set<Object> result = new HashSet<Object>();
		Collection<IAbstractArtifact> allArtifacts = manager.getAllArtifacts(
				true, false, new NullProgressMonitor());

		result.addAll(allArtifacts);
		for (IAbstractArtifact artifact : allArtifacts) {
			result.addAll(artifact.getChildren());
		}
		int size = result.size();

		allArtifacts = manager.getAllArtifacts(true, false,
				new NullProgressMonitor());
		result.addAll(allArtifacts);
		for (IAbstractArtifact artifact : allArtifacts) {
			result.addAll(artifact.getChildren());
		}
		assertEquals(result.size(), size);
	}

	private void checkExpectedContextProjectAwareArtifacts(
			Collection<IAbstractArtifact> artifacts) {
		assertNotNull(artifacts);
		assertTrue(artifacts.size() > 0);
		for (IAbstractArtifact artifact : artifacts) {
			assertTrue(artifact instanceof IContextProjectAware);
		}
	}
}
