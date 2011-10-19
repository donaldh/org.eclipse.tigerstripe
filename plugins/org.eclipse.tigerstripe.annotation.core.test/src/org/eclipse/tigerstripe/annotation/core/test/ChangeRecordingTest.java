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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.InTransaction;
import org.eclipse.tigerstripe.annotation.core.InTransaction.Operation;
import org.eclipse.tigerstripe.annotation.core.test.model.Author;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage;

/**
 * @author Yuri Strot
 *
 */
public class ChangeRecordingTest extends AbstractResourceTestCase {
	
	private IProject project;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		project = createProject("Test Project");
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project);
	}
	
	public void test1() throws AnnotationException {
		try {
			//create
			addAnnotation();
			
			//read
			final Author author = read();
			assertNotNull(author);
			assertNull(author.getFirstName());
			assertNull(author.getLastName());
			
			InTransaction.write(new Operation() {
				
				public void run() throws Throwable {
					author.setFirstName("First");
					author.setLastName("Last");
				}
			});

			//read and check
			Author newAuthor = read();
			assertNotNull(newAuthor);
			assertEquals(newAuthor.getFirstName(), "First");
			assertEquals(newAuthor.getLastName(), "Last");
		}
		finally {
			removeAnnotation();
		}
	}
	
	public void test2() throws AnnotationException {
		try {
			//create author
			final Author author = ModelFactory.eINSTANCE.createAuthor();
			author.setFirstName("name");
			author.setLastName("name2");
			
			//add and change annotation
			Annotation annotation = AnnotationPlugin.getManager().addAnnotation(project, author);
			
			InTransaction.write(new Operation() {
				
				public void run() throws Throwable {
					author.setFirstName("name2");
				}
			});

			//save annotation
			AnnotationPlugin.getManager().save(annotation);
			
			//read and check
			Author author2 = read();
			assertEquals(author2.getFirstName(), "name2");
		}
		finally {
			removeAnnotation();
		}
	}
	
	private void addAnnotation() throws AnnotationException {
		Author author = ModelFactory.eINSTANCE.createAuthor();
		AnnotationPlugin.getManager().addAnnotation(project, author);
	}
	
	private void removeAnnotation() {
		AnnotationPlugin.getManager().removeAnnotations(project);
	}
	
	private Author read() {
		Annotation[] annotations = AnnotationPlugin.getManager().getAnnotations(project, false);
		assertNotNull(annotations);
		assertEquals(annotations.length, 1);
		assertNotNull(annotations[0]);
		EObject content = annotations[0].getContent();
		assertEquals(content.eClass(), ModelPackage.eINSTANCE.getAuthor());
		return (Author)content;
	}
}
