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
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class CreateManyAnnotations extends AbstractResourceTestCase {
	
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
	
	public void testManyAnnotationsCreation() throws AnnotationException {
		int i = 0;
		try {
			for (; i < 500; i++) {
				annotateProject(project1, i + "");
				annotateProject(project2, i + "");
			}
		}
		finally {
			AnnotationPlugin.getManager().removeAnnotations(project1);
			AnnotationPlugin.getManager().removeAnnotations(project2);
		}
	}
	
	protected void annotateProject(IProject project, String text) throws AnnotationException {
		MimeType type = ModelFactory.eINSTANCE.createMimeType();
		type.setMimeType(text);
		AnnotationPlugin.getManager().addAnnotation(project, type);
	}

}
