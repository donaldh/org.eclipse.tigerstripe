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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
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
		List<Record> records = new ArrayList<Record>();
		IAnnotationManager annManager = makeMock(IAnnotationManager.class, records);

		IRefactoringChangesListener listener = new ChangeIdRefactoringListener(annManager);
		ILazyObject oldObject = new ChangeIdLazyObject(project);

		
		listener.changed(oldObject, null,
				IRefactoringChangesListener.ABOUT_TO_CHANGE);

		assertEquals(0, records.size());
		
		// update module id
		IProjectDetails details = project.getProjectDetails();
		details.setModelId(NEW_MODULE_ID);

		ILazyObject newObject = new ChangeIdLazyObject(project);
		listener.changed(oldObject, newObject,
				IRefactoringChangesListener.CHANGED);

		assertEquals(1, records.size());
		
		Record record = records.get(0);
		URI oldUri = (URI) record.args[0]; 
		URI newUri = (URI) record.args[1]; 
		boolean affectChildren = (Boolean) record.args[2];

		assertNotNull(oldUri);
		assertNotNull(newUri);
		assertTrue(affectChildren);
		assertEquals(createURI(new String[] { MODULE_ID }), oldUri);
		assertEquals(createURI(new String[] { NEW_MODULE_ID }), newUri);

		listener.changed(oldObject, newObject,
				IRefactoringChangesListener.CHANGED);
		assertEquals(1, records.size());
	}

	public void testChangeIdRefactoringWithReferences() throws Exception {
		ITigerstripeModelProject referencedProject = (ITigerstripeModelProject) createEmptyModelProject(
				REFERENCED_MODULE_ID, REFERENCED_MODULE_ID);

		List<Record> records = new ArrayList<Record>();
		IAnnotationManager annManager = makeMock(IAnnotationManager.class, records);
		
		// add reference
		ITigerstripeModelProject wc = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addModelReference(ModelReference
				.referenceFromProject(referencedProject));
		wc.commit(new NullProgressMonitor());
		assertTrue(project.getReferencedProjects().length == 1);

		IRefactoringChangesListener listener = new ChangeIdRefactoringListener(annManager);
		ILazyObject oldObject = new ChangeIdLazyObject(referencedProject);

		listener.changed(oldObject, null,
				IRefactoringChangesListener.ABOUT_TO_CHANGE);

		assertTrue(records.isEmpty());
		
		// update module id
		IProjectDetails details = referencedProject.getProjectDetails();
		details.setModelId(NEW_MODULE_ID);

		ILazyObject newObject = new ChangeIdLazyObject(referencedProject);

		listener.changed(oldObject, newObject,
				IRefactoringChangesListener.CHANGED);

		assertEquals(2, records.size());
		ListIterator<Record> it = records.listIterator();
		
		while (it.hasNext()) {
			
			Record record = it.next();
			
			URI oldUri = (URI) record.args[0]; 
			URI newUri = (URI) record.args[1]; 
			boolean affectChildren = (Boolean) record.args[2];
				
			assertNotNull(oldUri);
			assertNotNull(newUri);
			assertTrue(affectChildren);
			switch (it.nextIndex()) {
			case 1:
				assertEquals(
						createURI(new String[] { REFERENCED_MODULE_ID }),
						oldUri);
				assertEquals(createURI(new String[] { NEW_MODULE_ID }),
						newUri);
				break;
			case 2:
				assertEquals(createURI(new String[] { MODULE_ID,
						REFERENCED_MODULE_ID }), oldUri);
				assertEquals(createURI(new String[] { MODULE_ID,
						NEW_MODULE_ID }), newUri);
				break;
			default:
				fail("The method call isn't expected");
				break;
			}
		}
		
		listener.changed(oldObject, newObject,
				IRefactoringChangesListener.CHANGED);
		assertEquals(2, records.size()); //not changed
		
		deleteModelProject(referencedProject);
	}

	private URI createURI(String[] segments) {
		return URI.createHierarchicalURI(
				TigerstripeURIAdapterFactory.SCHEME_TS, null, null, segments,
				null, null);
	}
	
	public static <T> T makeMock(Class<T> iface, final List<Record> records) {
		
		return iface.cast(Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[] { iface }, new InvocationHandler() {

					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						records.add(new Record(method, args));
						if (method.getReturnType().isPrimitive()) {
							return 0;
						}
						return null;
					}
				}));
	}
	
	public static class Record {
		private final Method method;
		private final Object[] args;
		public Record(Method method, Object[] args) {
			this.method = method;
			this.args = args;
		}
	}
}
