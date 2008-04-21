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
package org.eclipse.tigerstripe.annotation.example.person.tests;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.example.person.PersonFactory;

/**
 * @author Yuri Strot
 *
 */
public class AddRemoveAnnotationTest extends TestCase {
	
	protected PersonID id1 = new PersonID("testPerson");
	protected PersonID id2 = new PersonID("testPerson2");
	protected IAnnotationManager manager;
	
	@Override
	protected void setUp() throws Exception {
		manager = AnnotationPlugin.getManager();
		id1 = new PersonID("testPerson");
		id2 = new PersonID("testPerson2");
	    super.setUp();
	}
	
	public void testAddRemove() {
		Annotation annotation1 = manager.addAnnotation(id1, PersonFactory.eINSTANCE.createPerson());
		assertNotNull(annotation1);
		
		Annotation annotation2 = manager.addAnnotation(id1, PersonFactory.eINSTANCE.createPerson());
		assertNotNull(annotation2);
		
		Annotation annotation3 = manager.addAnnotation(id2, PersonFactory.eINSTANCE.createPerson());
		assertNotNull(annotation3);
		
		assertEquals(annotation1.getUri(), annotation2.getUri());
		assertFalse(annotation2.getUri().equals(annotation3.getUri()));
		
		Annotation[] annotations = manager.getAnnotations(id1);
		assertNotNull(annotations);
		assertEquals(annotations.length, 2);
		
		manager.removeAnnotations(id1);
		annotations = manager.getAnnotations(id1);
		assertNotNull(annotations);
		assertEquals(annotations.length, 0);
		
		annotations = manager.getAnnotations(id2);
		assertNotNull(annotations);
		assertEquals(annotations.length, 1);
		
		manager.removeAnnotation(annotations[0]);
		annotations = manager.getAnnotations(id2);
		assertNotNull(annotations);
		assertEquals(annotations.length, 0);
	}

}
