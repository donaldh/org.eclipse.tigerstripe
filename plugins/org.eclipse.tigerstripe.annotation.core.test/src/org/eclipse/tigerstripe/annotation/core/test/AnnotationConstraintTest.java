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
import org.eclipse.tigerstripe.annotation.core.AnnotationConstraintException;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.constraint.AnnotationConstraintExample;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationConstraintTest extends AbstractResourceTestCase {
	
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
	
	public void testConstraint() throws AnnotationException {
		try {
			AnnotationConstraintExample.enable();
			IAnnotationManager manager = AnnotationPlugin.getManager();
			
			Annotation annotation1 = manager.addAnnotation(project1, ModelFactory.eINSTANCE.createMimeType());
			Annotation annotation2 = manager.addAnnotation(project2, ModelFactory.eINSTANCE.createMimeType());
			
			manager.removeAnnotation(annotation1);
			manager.removeAnnotation(annotation2);
			
			AnnotationConstraintException exception = null;
			
			try {
				annotation2 = manager.addAnnotation(project2, ModelFactory.eINSTANCE.createMimeType());
				annotation1 = manager.addAnnotation(project1, ModelFactory.eINSTANCE.createMimeType());
			}
			catch (AnnotationConstraintException e) {
				exception = e;
			}
			assertNotNull(exception);
		}
		finally {
			AnnotationConstraintExample.disable();
		}
	}

}
