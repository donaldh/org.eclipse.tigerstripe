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

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;

/**
 * @author Yuri Strot
 *
 */
public class ExternalResourceTest extends AbstractResourceTestCase {
	
	private static final String ANNOTATION_FOLDER = "annotations/";
	
	public void testExternalResource() throws Exception {
		//create EMF resource from the "annotations/.ann",
		//which contains annotation for the "project1" resource
		File file = new File(ANNOTATION_FOLDER + "." + EObjectRouter.ANNOTATION_FILE_EXTENSION);
		URI uri = URI.createFileURI(file.getAbsolutePath());
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		try {
			resource.load(null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//get annotation from the resource
		List<EObject> objects = resource.getContents();
		assertEquals(objects.size(), 1);
		EObject object = objects.get(0);
		assertEquals(object.eClass(), AnnotationPackage.eINSTANCE.getAnnotation());
		Annotation annotation = (Annotation)object;
		
		try {
			//register this resource in the TAF
			AnnotationPlugin.getManager().addAnnotations(resource, Mode.READ_ONLY);
			//check that resource successfully added
			Annotation[] annotations = AnnotationPlugin.getManager().getAnnotations(project1, false);
			assertNotNull(annotations);
			assertEquals(annotations.length, 1);
			assertEquals(annotation, annotations[0]);
		}
		finally {
			//unregister resource
			AnnotationPlugin.getManager().removeAnnotations(resource);
		}
	}
	
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
	
	private IProject project1;

}
