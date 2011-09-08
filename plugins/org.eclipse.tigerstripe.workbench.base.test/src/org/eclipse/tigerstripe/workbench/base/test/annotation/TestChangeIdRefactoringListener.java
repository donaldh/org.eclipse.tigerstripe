/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.annotation;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringDelegate;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.annotation.ChangeIdLazyObject;
import org.eclipse.tigerstripe.workbench.internal.annotation.ChangeIdRefactoringListener;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestChangeIdRefactoringListener extends
		AbstractTigerstripeAnnotationsTestCase {

	private static final String MODULE_ID = "TestProject1";

	private static final String REFERENCED_MODULE_ID = "TestProject2";

	private static final String NEW_MODULE_ID = "TestProjectNew";

	private ITigerstripeModelProject project;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		project = (ITigerstripeModelProject) createEmptyModelProject(MODULE_ID,
				MODULE_ID);
	}

	@Override
	protected void tearDown() throws Exception {
		deleteModelProject(project);
		project = null;
		super.tearDown();
	}

	public void testChangeIdRefactoring() throws Exception {
		IRefactoringChangesListener listener = new ChangeIdRefactoringListener();
		ILazyObject oldObject = new ChangeIdLazyObject(project);

		IRefactoringDelegate nonCalledDelegate = new RefactoringDelegateMock();
		listener.changed(nonCalledDelegate, oldObject, null,
				IRefactoringChangesListener.ABOUT_TO_CHANGE);

		// update module id
		IProjectDetails details = project.getProjectDetails();
		details.setModelId(NEW_MODULE_ID);

		ILazyObject newObject = new ChangeIdLazyObject(project);
		IRefactoringDelegate delegate = new RefactoringDelegateMock() {

			@Override
			public void changed(URI oldUri, URI newUri, boolean affectChildren) {
				assertNotNull(oldUri);
				assertNotNull(newUri);
				assertTrue(affectChildren);
				assertEquals(createURI(new String[] { MODULE_ID }), oldUri);
				assertEquals(createURI(new String[] { NEW_MODULE_ID }), newUri);
			}
		};
		listener.changed(delegate, oldObject, newObject,
				IRefactoringChangesListener.CHANGED);

		listener.changed(nonCalledDelegate, oldObject, newObject,
				IRefactoringChangesListener.CHANGED);
	}

	public void testChangeIdRefactoringWithReferences() throws Exception {
		ITigerstripeModelProject referencedProject = (ITigerstripeModelProject) createEmptyModelProject(
				REFERENCED_MODULE_ID, REFERENCED_MODULE_ID);

		// add reference
		ITigerstripeModelProject wc = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addModelReference(ModelReference
				.referenceFromProject(referencedProject));
		wc.commit(new NullProgressMonitor());
		assertTrue(project.getReferencedProjects().length == 1);

		IRefactoringChangesListener listener = new ChangeIdRefactoringListener();
		ILazyObject oldObject = new ChangeIdLazyObject(referencedProject);

		IRefactoringDelegate nonCalledDelegate = new RefactoringDelegateMock();
		listener.changed(nonCalledDelegate, oldObject, null,
				IRefactoringChangesListener.ABOUT_TO_CHANGE);

		// update module id
		IProjectDetails details = referencedProject.getProjectDetails();
		details.setModelId(NEW_MODULE_ID);

		ILazyObject newObject = new ChangeIdLazyObject(referencedProject);
		IRefactoringDelegate delegate = new RefactoringDelegateMock() {

			private int callCount;

			@Override
			public void changed(URI oldUri, URI newUri, boolean affectChildren) {
				assertNotNull(oldUri);
				assertNotNull(newUri);
				assertTrue(affectChildren);
				switch (callCount) {
				case 0:
					assertEquals(
							createURI(new String[] { REFERENCED_MODULE_ID }),
							oldUri);
					assertEquals(createURI(new String[] { NEW_MODULE_ID }),
							newUri);
					break;
				case 1:
					assertEquals(createURI(new String[] { MODULE_ID,
							REFERENCED_MODULE_ID }), oldUri);
					assertEquals(createURI(new String[] { MODULE_ID,
							NEW_MODULE_ID }), newUri);
					break;
				default:
					fail("The method call isn't expected");
					break;
				}
				callCount++;
			}
		};
		listener.changed(delegate, oldObject, newObject,
				IRefactoringChangesListener.CHANGED);

		listener.changed(nonCalledDelegate, oldObject, newObject,
				IRefactoringChangesListener.CHANGED);

		deleteModelProject(referencedProject);
	}

	private URI createURI(String[] segments) {
		return URI.createHierarchicalURI(
				TigerstripeURIAdapterFactory.SCHEME_TS, null, null, segments,
				null, null);
	}

	private class RefactoringDelegateMock implements IRefactoringDelegate {

		public void deleted(URI uri, boolean affectChildren) {
			fail("The method call isn't expected");
		}

		public void changed(URI oldUri, URI newUri, boolean affectChildren) {
			fail("The method call isn't expected");
		}

		public void copied(URI fromUri, URI toUri, boolean affectChildren) {
			fail("The method call isn't expected");
		}
	}
}
