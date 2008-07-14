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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class IndexRebuldingTest extends AbstractResourceTestCase {
	
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
	
	public void test1() throws CoreException, AnnotationException {
		//Test is INDEX will be rebuilt after annotation file deletion
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, createMimeType("text/html"));
			removeAnnotationFile(project1);
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(annotations.length, 0);
		}
		finally {
			manager.removeAnnotations(project1);
		}
	}
	
	public void test2() throws CoreException, AnnotationException {
		//Test is annotations would be kept after another annotations
		//file will be corrupted
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, createMimeType("text/html"));
			manager.addAnnotation(project2, createMimeType("text/xml"));
			manager.addAnnotation(project2, createMimeType("text/plain"));
			removeAnnotationFile(project1);
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(annotations.length, 0);
			annotations = manager.getAnnotations(project2, false);
			assertEquals(annotations.length, 2);
		}
		finally {
			manager.removeAnnotations(project1);
			manager.removeAnnotations(project2);
		}
	}
	
	public void test3() throws Exception {
		//Test is INDEX will be rebuilt after annotation file modification
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, createMimeType("text/html"));
			manager.addAnnotation(project2, createMimeType("text/xml"));
			manager.addAnnotation(project2, createMimeType("text/plain"));
			replaceAnnotationFile(project1, project2);
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(annotations.length, 2);
			annotations = manager.getAnnotations(project2, false);
			assertEquals(annotations.length, 2);
		}
		finally {
			manager.removeAnnotations(project1);
			manager.removeAnnotations(project2);
		}
	}
	
	/**
	 * Remove annotations file from the project
	 * 
	 * @param project
	 * @throws CoreException 
	 */
	private void removeAnnotationFile(IProject project) throws CoreException {
		IFile file = getAnnotationFile(project);
		if (file != null) {
			file.delete(true, new NullProgressMonitor());
		}
	}
	
	/**
	 * Replace project1 annotations file with project2 annotations file
	 * (with URI replacing) 
	 * 
	 * @param project1
	 * @param project2
	 * @throws IOException
	 */
	private void replaceAnnotationFile(IProject project1, IProject project2) throws Exception {
		IFile file1 = getAnnotationFile(project1);
		IFile file2 = getAnnotationFile(project2);
		if (file1 == null || file2 == null)
			return;
		String content = getContent(file2);
		content = content.replaceAll("resource:/project2", "resource:/project1");
		saveContent(content, file1);
	}
	
	private IFile getAnnotationFile(IProject project) {
		return project.getFile(".ann");
	}
	
	private String getContent(IFile file) throws IOException, CoreException {
		InputStream stream = file.getContents();
		StringBuffer content = new StringBuffer();
		byte[] buffer = new byte[1024 * 1024];
		int size = 0;
		while((size = stream.read(buffer)) >= 0) {
			content.append(new String(buffer, 0, size));
		}
		stream.close();
		return content.toString();
	}
	
	private void saveContent(String content, IFile file) throws CoreException {
		file.setContents(new ByteArrayInputStream(content.getBytes()),
				true, true, new NullProgressMonitor());
	}
	
	private MimeType createMimeType(String text) {
		MimeType type = ModelFactory.eINSTANCE.createMimeType();
		type.setMimeType(text);
		return type;
	}

}
