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
package org.eclipse.tigerstripe.espace.core.tree.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.espace.core.tree.RBNode;
import org.eclipse.tigerstripe.espace.core.tree.RBTree;

/**
 * @author Yuri Strot
 *
 */
public class RBTreeViewer {
    
    public static void show(RBTree tree) {
    	show(tree.getRoot(), 0, new DefaultContentViewer(tree.getFeature()));
    }
    
    public static void show(RBNode node, int level, IContentViewer viewer) {
    	for (int i = 0; i < level; i++)
			System.out.print(" ");
    	if (node == null)
    		System.out.println("[NULL]");
    	else {
        	String color = node.getColor() == RBNode.BLACK ? "BLACK" : "RED";
        	System.out.print("[" + color + ", {");
        	int size = node.getObjects().size(), i = 0;
        	for (EObject object : node.getObjects()) {
        		System.out.print(viewer.getText(object));
        		if (i != size - 1) {
        			System.out.print(", ");
        		}
        		i++;
			}
        	System.out.println("} ]");
        	show(node.getLeft(), level + 1, viewer);
        	show(node.getRight(), level + 1, viewer);
    	}
    }

}
