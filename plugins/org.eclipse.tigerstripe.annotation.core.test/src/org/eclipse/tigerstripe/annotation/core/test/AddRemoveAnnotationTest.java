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
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class AddRemoveAnnotationTest extends AbstractResourceTestCase {
	
	protected IProject project1;
	protected IProject project2;
	
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
	
	public void testAddRemove() {
		IAnnotationManager manager = AnnotationPlugin.getManager();
		Annotation annotation1 = manager.addAnnotation(project1, ModelFactory.eINSTANCE.createAuthor());
		assertNotNull(annotation1);
		
		Annotation annotation2 = manager.addAnnotation(project1, ModelFactory.eINSTANCE.createAuthor());
		assertNotNull(annotation2);
		
		Annotation annotation3 = manager.addAnnotation(project2, ModelFactory.eINSTANCE.createAuthor());
		assertNotNull(annotation3);
		
		assertEquals(annotation1.getUri(), annotation2.getUri());
		assertFalse(annotation2.getUri().equals(annotation3.getUri()));
		
		Annotation[] annotations = manager.getAnnotations(project1, false);
		assertNotNull(annotations);
		assertEquals(annotations.length, 2);
		
		manager.removeAnnotations(project1);
		annotations = manager.getAnnotations(project1, false);
		assertNotNull(annotations);
		assertEquals(annotations.length, 0);
		
		annotations = manager.getAnnotations(project2, false);
		assertNotNull(annotations);
		assertEquals(annotations.length, 1);
		
		manager.removeAnnotation(annotations[0]);
		annotations = manager.getAnnotations(project2, false);
		assertNotNull(annotations);
		assertEquals(annotations.length, 0);
	}

}
