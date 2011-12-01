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

import static org.eclipse.tigerstripe.annotation.core.IAnnotationManager.ANNOTATION_FILE_EXTENSION;

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
import org.eclipse.tigerstripe.annotation.core.InTransaction;
import org.eclipse.tigerstripe.annotation.core.InTransaction.Operation;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationResourceTest extends AbstractResourceTestCase {
	
	protected IProject project1;
	private static final String MIME_TYPE_HTML = "text/html";
	
	@Override
	protected void setUp() throws Exception {
		project1 = createProject("project1");
	}
	
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project1);
	}
	
	public void testReadWrite() throws AnnotationException {
		Resource resource = createResourceWithAnnotation(false);
		try {
			AnnotationPlugin.getManager().addAnnotations(resource);
			final MimeType type = getAnnotationContent();
			
			InTransaction.run(new Operation() {
				
				public void run() throws Throwable {
					type.setMimeType(MIME_TYPE_HTML);
				}
			});
			
			MimeType type2 = getAnnotationContent();
			assertEquals(type2.getMimeType(), MIME_TYPE_HTML);
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
		
		File newAnnotationFile = new File(parent, "ann." + ANNOTATION_FILE_EXTENSION);
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
