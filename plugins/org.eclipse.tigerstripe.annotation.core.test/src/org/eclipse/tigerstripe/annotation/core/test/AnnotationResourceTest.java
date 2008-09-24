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
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationResourceTest extends AbstractResourceTestCase {
	
	protected IProject project1;
	private static final String MIME_TYPE_HTML = "text/html";
	private static final String MIME_TYPE_XML = "text/xml";
	
	private static final boolean DISABLE = true;
	
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
	
	public void testReadWrite() throws AnnotationException {
		AnnotationPlugin.getManager();
		if (DISABLE) return;
		Resource resource = createResourceWithAnnotation(false);
		try {
			AnnotationPlugin.getManager().addAnnotations(resource, Mode.READ_WRITE);
			
			MimeType type = getAnnotationContent();
			type.setMimeType(MIME_TYPE_HTML);
			AnnotationPlugin.getManager().save((Annotation)type.eContainer());
			
			type = getAnnotationContent();
			assertEquals(type.getMimeType(), MIME_TYPE_HTML);
		}
		finally {
			AnnotationPlugin.getManager().removeAnnotations(resource);
		}
	}
	
	public void testReadOnly() throws AnnotationException {
		if (DISABLE) return;
		Resource resource = createResourceWithAnnotation(true);
		try {
			AnnotationPlugin.getManager().addAnnotations(resource, Mode.READ_ONLY);
			
			MimeType type = getAnnotationContent();
			type.setMimeType(MIME_TYPE_HTML);
			AnnotationPlugin.getManager().save((Annotation)type.eContainer());
			
			type = getAnnotationContent();
			assertEquals(type.getMimeType(), null);
		}
		finally {
			AnnotationPlugin.getManager().removeAnnotations(resource);
		}
	}
	
	public void testReadOnlyOverride() throws AnnotationException {
		if (DISABLE) return;
		Resource resource = createResourceWithAnnotation(true);
		try {
			AnnotationPlugin.getManager().addAnnotations(resource, Mode.READ_ONLY_OVERRIDE);
			
			MimeType type = getAnnotationContent();
			type.setMimeType(MIME_TYPE_HTML);
			AnnotationPlugin.getManager().save((Annotation)type.eContainer());
			
			type = getAnnotationContent();
			assertEquals(type.getMimeType(), MIME_TYPE_HTML);
			
			type.setMimeType(MIME_TYPE_XML);
			AnnotationPlugin.getManager().save((Annotation)type.eContainer());
			
			type = getAnnotationContent();
			assertEquals(type.getMimeType(), MIME_TYPE_XML);
		}
		finally {
			AnnotationPlugin.getManager().removeAnnotations(resource);
		}
	}
	
	protected MimeType getAnnotationContent() {
		Annotation[] annotations = AnnotationPlugin.getManager().getAnnotations(project1, false);
		assertNotNull(annotations);
		assertEquals(annotations.length, 1);
		return (MimeType)annotations[0].getContent();
	}
	
	protected Resource createResourceWithAnnotation(boolean readOnly) {
		File file = project1.getLocation().toFile();
		File parent = file.getParentFile();
		
		File newAnnotationFile = new File(parent, "ann." + EObjectRouter.ANNOTATION_FILE_EXTENSION);
		if (newAnnotationFile.exists())
			newAnnotationFile.delete();
		
		URI uri = URI.createFileURI(newAnnotationFile.getAbsolutePath());
		Resource resource = getResource(uri);
		
		Annotation annotation = AnnotationFactory.eINSTANCE.createAnnotation();
		annotation.setUri(URI.createURI("resource:/project1"));
		annotation.setId(EcoreUtil.generateUUID());
		annotation.setContent(ModelFactory.eINSTANCE.createMimeType());
		
		resource.getContents().add(annotation);
		save(resource);

		if (readOnly)
			newAnnotationFile.setReadOnly();
		
		return resource;
	}
	
	protected void save(Resource resource) {
		try {
            resource.save(null);
        }
        catch (IOException e) {
        	//ignore exception
        }
	}
	
	protected Resource getResource(URI uri) {
		ResourceSetImpl rs = new ResourceSetImpl();
		Resource resource = rs.getResource(uri, false);
		if (resource == null) {
			resource = rs.createResource(uri);
		}
		if (resource != null) {
			try {
	            resource.load(null);
            }
            catch (IOException e) {
            	//ignore exception
            }
		}
		return resource;
	}

}
