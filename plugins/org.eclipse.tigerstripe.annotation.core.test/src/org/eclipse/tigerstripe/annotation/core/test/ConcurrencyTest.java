/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class ConcurrencyTest extends AbstractResourceTestCase {
	
	protected IProject project1;
	protected IProject project2;
	private IAnnotationManager manager;;
	
	private static final int COUNT = 30;
	
	@Override
	protected void setUp() throws Exception {
		project1 = createProject("project1");
		project2 = createProject("project2");
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project1);
		deleteProject(project2);
	}
	
	private class AnnotationCreator implements Runnable {
		
		public void run() {
			for (int i = 0; i < COUNT; i++) {
				try {
					manager.addAnnotation(project1, ModelFactory.eINSTANCE.createMimeType());
					manager.addAnnotation(project2, ModelFactory.eINSTANCE.createMimeType());
				} catch (AnnotationException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class AnnotationRemover implements Runnable {
		
		public void run() {
			for (int i = 0; i < COUNT; i++) {
				manager.removeAnnotations(project1);
				manager.removeAnnotations(project2);
			}
		}
		
	}

	private class AnnotationAccessor implements Runnable {
		
		public void run() {
			for (int i = 0; i < COUNT; i++) {
				manager.getAnnotations(project1, false);
				manager.getAnnotations(project2, false);
			}
		}
		
	}
	
	public void testConcurrency() {
		manager = AnnotationPlugin.getManager();
		new Thread(new AnnotationRemover()).start();
		new Thread(new AnnotationAccessor()).start();
		new AnnotationCreator().run();
	}

}
