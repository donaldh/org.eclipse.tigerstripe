package org.eclipse.tigerstripe.espace.core.tests;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.example.ExampleFactory;
import org.eclipse.tigerstripe.annotation.example.TextAnnotation;
import org.eclipse.tigerstripe.espace.core.tree.RBTree;
import org.eclipse.tigerstripe.espace.core.tree.TreeFactory;

import junit.framework.TestCase;

public class TreeTest extends TestCase {
	
	public void testInsert() {
		
		TextAnnotation text1 = ExampleFactory.eINSTANCE.createTextAnnotation();
		text1.setText("Text1");
		
		TextAnnotation text2 = ExampleFactory.eINSTANCE.createTextAnnotation();
		text2.setText("Text2");
		
		TextAnnotation text3 = ExampleFactory.eINSTANCE.createTextAnnotation();
		text3.setText("Text3");
		
		RBTree tree = TreeFactory.eINSTANCE.createRBTree();
		tree.setFeature(text3.eClass().getEStructuralFeature("text"));
		tree.insert(text1);
		tree.insert(text2);
		tree.insert(text3);
		
		EObject[] objects = tree.find("Text2");
		assertNotNull(objects);
		assertEquals(objects.length, 1);
		assertEquals(objects[0], text2);
		
		tree.remove(text2);
		
		objects = tree.find("Text2");
		assertNotNull(objects);
		assertEquals(objects.length, 0);
	}

}
