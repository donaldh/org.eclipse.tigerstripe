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
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage;

/**
 * @author Yuri Strot
 *
 */
public class QueryTest extends AbstractResourceTestCase {
	
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
	
	public void testQuery() {
		MimeType type1 = createMimeType("text/html");
		MimeType type2 = createMimeType("text/plain");
		MimeType type3 = createMimeType(null);
		
		try {
			AnnotationPlugin.getManager().addAnnotation(project1, type1);
			AnnotationPlugin.getManager().addAnnotation(project1, type2);
			AnnotationPlugin.getManager().addAnnotation(project2, type3);
			
			checkQuery(AnnotationPackage.eINSTANCE.getAnnotation(), 3);
			checkQuery(ModelPackage.eINSTANCE.getMimeType(), 3);
		}
		finally {
			AnnotationPlugin.getManager().removeAnnotations(project1);
			AnnotationPlugin.getManager().removeAnnotations(project2);
		}
	}
	
	private MimeType createMimeType(String type) {
		MimeType mimeType = ModelFactory.eINSTANCE.createMimeType();
		mimeType.setMimeType(type);
		return mimeType;
	}
	
	private void checkQuery(EClassifier classifier, int size) {
		EObject[] objects = AnnotationPlugin.getManager().query(classifier);
		
		assertNotNull(objects);
		assertEquals(objects.length, size);
		
		for (int i = 0; i < objects.length; i++)
			assertEquals(objects[i].eClass(), classifier);
	}

}
