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
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;

/**
 * @author Yuri Strot
 *
 */
public class RebuildIndexTest extends AbstractResourceTestCase {
	
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
	
	public void testIndexRebuild() {
		AnnotationManager manager = (AnnotationManager)AnnotationPlugin.getManager();
		Annotation annotation1 = null, annotation2 = null, annotation3 = null;
		try {
			annotation1 = manager.addAnnotation(project1, ModelFactory.eINSTANCE.createMimeType());
			assertNotNull(annotation1);
			
			annotation2 = manager.addAnnotation(project1, ModelFactory.eINSTANCE.createMimeType());
			assertNotNull(annotation2);
			
			annotation3 = manager.addAnnotation(project2, ModelFactory.eINSTANCE.createMimeType());
			assertNotNull(annotation3);
			
			manager.rebuildIndex();
			
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(annotations.length, 2);
			
			annotations = manager.getAnnotations(project2, false);
			assertEquals(annotations.length, 1);
		}
		finally {
			if (annotation1 != null)
				manager.remove(annotation1);
			if (annotation2 != null)
				manager.remove(annotation2);
			if (annotation3 != null)
				manager.remove(annotation3);
		}
	}

}
