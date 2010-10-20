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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class DelegatesTest extends AbstractResourceTestCase {
	
	protected IProject project;
	protected IJavaProject javaProject;
	
	@Override
	protected void setUp() throws Exception {
		project = createProject("project");
		javaProject = JavaCore.create(project);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project);
	}
	
	public void testAddRemove() throws AnnotationException {
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			Annotation annotation1 = manager.addAnnotation(project,
					ModelFactory.eINSTANCE.createAuthor());
			assertNotNull(annotation1);
			
			Annotation[] annotations = manager.getAnnotations(project, false);
			assertEquals(annotations.length, 1);

			//TODO: Java Annotation not packaged up by default with Tigerstripe features.
//			Annotation annotation2 = manager.addAnnotation(javaProject,
//					ModelFactory.eINSTANCE.createAuthor());
//			assertNotNull(annotation2);
//			
//			annotations = manager.getAnnotations(javaProject, false);
//			assertEquals(annotations.length, 1);
//			
//			annotations = manager.getAnnotations(javaProject, true);
//			assertEquals(annotations.length, 2);
		}
		finally {
			manager.removeAnnotations(project);
			manager.removeAnnotations(javaProject);
		}
	}

}
