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

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.AnnotationPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.example.person.Name;
import org.eclipse.tigerstripe.annotation.example.person.Person;
import org.eclipse.tigerstripe.annotation.example.person.PersonFactory;
import org.eclipse.tigerstripe.annotation.example.person.PersonPackage;

/**
 * @author Yuri Strot
 *
 */
public class QueryTest extends TestCase {
	
	protected PersonID id1 = new PersonID("testPerson");
	protected PersonID id2 = new PersonID("testPerson2");
	protected IAnnotationManager manager;
	
	@Override
	protected void setUp() throws Exception {
		manager = AnnotationPlugin.getManager();
		id1 = new PersonID("testPerson");
		id2 = new PersonID("testPerson2");
		
		Person person1 = createPerson("Yuri", "Strot", 21);
		Person person2 = createPerson("John", "Adams", 45);
		Person person3 = createPerson(null, null, 25);
		
		manager.addAnnotation(id1, person1);
		manager.addAnnotation(id1, person2);
		manager.addAnnotation(id2, person3);
	}
	
	private Person createPerson(String firstName, String lastName, int age) {
		Person person = PersonFactory.eINSTANCE.createPerson();
		if (firstName != null || lastName != null) {
			Name name = PersonFactory.eINSTANCE.createName();
			name.setFirstName(firstName);
			name.setLastName(lastName);
			person.setName(name);
		}
		person.setAge(age);
		return person;
	}
	
	private void checkQuery(EClassifier classifier, int size) {
		EObject[] objects = manager.query(classifier);
		
		assertNotNull(objects);
		assertEquals(objects.length, size);
		
		for (int i = 0; i < objects.length; i++)
			assertEquals(objects[i].eClass(), classifier);
	}
	
	public void testQuery() {
		checkQuery(AnnotationPackage.eINSTANCE.getAnnotation(), 3);
		checkQuery(PersonPackage.eINSTANCE.getPerson(), 3);
		checkQuery(PersonPackage.eINSTANCE.getName_(), 2);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		manager.removeAnnotations(id1);
		manager.removeAnnotations(id2);
	}

}
