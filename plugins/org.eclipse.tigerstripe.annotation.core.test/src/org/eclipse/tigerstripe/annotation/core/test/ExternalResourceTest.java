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

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.osgi.framework.Bundle;

/**
 * @author Yuri Strot
 * 
 */
public class ExternalResourceTest extends AbstractResourceTestCase {

	private static final String ANNOTATION_FOLDER = "annotations/";
	
	public void testExternalResource() throws Exception {

		Bundle baseBundle = Platform
				.getBundle("org.eclipse.tigerstripe.annotation.core.test");

		// create EMF resource from the "annotations/.ann",
		// which contains annotation for the "project1" resource
		URL url = baseBundle.getResource(ANNOTATION_FOLDER + "."
				+ IAnnotationManager.ANNOTATION_FILE_EXTENSION);
		Resource resource = createResource(url);

		// get annotation from the resource
		Annotation annotation = getAnnotation(resource);
		try {
			// register this resource in the TAF
			AnnotationPlugin.getManager().addAnnotations(resource);
			// check that resource successfully added
			Annotation[] annotations = AnnotationPlugin.getManager()
					.getAnnotations(project1, false);
			assertNotNull(annotations);
			assertEquals(annotations.length, 1);
			assertEquals(annotation, annotations[0]);
		} finally {
			// unregister resource
			AnnotationPlugin.getManager().removeAnnotations(resource);
		}
	}

	private Resource createResource(URL url) throws IOException {
		URL rUrl = FileLocator.resolve(url);
		URI uri = URI.createURI(rUrl.toExternalForm());
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		resource.load(null);
		return resource;
	}

	private Annotation getAnnotation(Resource resource) {
		List<EObject> objects = resource.getContents();
		assertEquals(objects.size(), 1);
		EObject object = objects.get(0);
		assertEquals(object.eClass(),
				AnnotationPackage.eINSTANCE.getAnnotation());
		return (Annotation) object;
	}

	@Override
	protected void setUp() throws Exception {
		project1 = createProject("project1");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project1);
	}

	private IProject project1;

}
