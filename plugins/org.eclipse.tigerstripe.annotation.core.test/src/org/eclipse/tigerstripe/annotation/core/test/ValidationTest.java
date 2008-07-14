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
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.core.test.validation.MimeTypeValidator;

/**
 * @author Yuri Strot
 *
 */
public class ValidationTest extends AbstractResourceTestCase {
	
	protected IProject project1;
	
	@Override
	protected void setUp() throws Exception {
		project1 = createProject("project1");
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project1);
	}
	
	public void testValidation() throws AnnotationException {
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, ModelFactory.eINSTANCE.createMimeType());
			
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(annotations.length, 1);
			
			MimeType type = (MimeType)annotations[0].getContent();
			type.setMimeType(MimeTypeValidator.INVALID_TYPE);
			assertNull(type.getMimeType());
			
			type.setMimeType(MimeTypeValidator.VALID_TYPE);
			assertEquals(type.getMimeType(), MimeTypeValidator.VALID_TYPE);
		}
		finally {
			manager.removeAnnotations(project1);
		}
	}

}
