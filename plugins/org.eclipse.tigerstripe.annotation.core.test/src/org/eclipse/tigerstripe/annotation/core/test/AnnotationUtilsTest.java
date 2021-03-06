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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationUtilsTest extends AbstractResourceTestCase {
	
	protected IProject project1;
	protected IProject project2;
	
	private static final String MIME_TYPE_1 = "text/xml";
	private static final String MIME_TYPE_2 = "text/html";
	
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

		EList<Resource> resources = AnnotationPlugin.getManager()
				.getResourceSet().getResources();
		debugResources(resources);
		assertEquals(0, resources.size());
	}

	private void debugResources(EList<Resource> resources) {
		System.out.println("DEBUG ANNOTATION RESOURCES | " + Thread.currentThread().getId());
		for (Resource resource : resources) {
			System.out.println("[RESOURCE] " + resource.getURI());
		}
	}
	
	public void testCopyAnnotations() throws AnnotationException {
		IAnnotationManager manager = AnnotationPlugin.getManager();
		MimeType type1 = ModelFactory.eINSTANCE.createMimeType();
		type1.setMimeType(MIME_TYPE_1);
		MimeType type2 = ModelFactory.eINSTANCE.createMimeType();
		type2.setMimeType(MIME_TYPE_2);
		
		manager.addAnnotation(project1, type1);
		manager.addAnnotation(project1, type2);
		
		AnnotationUtils.copyAnnotations(project1, project2);
		Annotation[] annotations = manager.getAnnotations(project1, false);
		assertEquals(2, annotations.length);
		MimeType content1 = (MimeType)annotations[0].getContent();
		MimeType content2 = (MimeType)annotations[1].getContent();
		String mimeType1 = content1.getMimeType();
		String mimeType2 = content2.getMimeType();
		assertTrue((MIME_TYPE_1.equals(mimeType1) && MIME_TYPE_2.equals(mimeType2)) ||
				(MIME_TYPE_2.equals(mimeType1) && MIME_TYPE_1.equals(mimeType2)));
	}

}
