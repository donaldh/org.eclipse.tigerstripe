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

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage;
import org.eclipse.tigerstripe.espace.core.tree.RBTree;
import org.eclipse.tigerstripe.espace.core.tree.TreeFactory;

/**
 * @author Yuri Strot
 *
 */
public class TreeTest extends TestCase {
	
	public void testInsert() {
		
		MimeType type1 = ModelFactory.eINSTANCE.createMimeType();
		type1.setMimeType("text/plain");
		
		MimeType type2 = ModelFactory.eINSTANCE.createMimeType();
		type2.setMimeType("text/html");
		
		MimeType type3 = ModelFactory.eINSTANCE.createMimeType();
		type3.setMimeType("text/xml");
		
		RBTree tree = TreeFactory.eINSTANCE.createRBTree();
		tree.setFeature(type3.eClass().getEStructuralFeature("mimeType"));
		tree.insert(type1);
		tree.insert(type2);
		tree.insert(type3);
		
		EObject[] objects = tree.find("text/html");
		assertNotNull(objects);
		assertEquals(objects.length, 1);
		assertEquals(objects[0], type2);
		
		tree.remove(type2);
		
		objects = tree.find("text/html");
		assertNotNull(objects);
		assertEquals(objects.length, 0);
	}
	
	public void testManyInsert() {
		int i = 0;
		try {
			RBTree tree = TreeFactory.eINSTANCE.createRBTree();
			tree.setFeature(ModelPackage.eINSTANCE.getMimeType_MimeType());
			for (; i < 1000; i++) {
				String value = i + "";
				MimeType type = ModelFactory.eINSTANCE.createMimeType();
				type.setMimeType(value);
				tree.insert(type);
			}
		}
		finally {
			System.out.println(i);
		}
	}

}
