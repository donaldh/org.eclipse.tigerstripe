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
package org.eclipse.tigerstripe.annotation.java.ui.refactoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;

/**
 * @author Yuri Strot
 *
 */
public class JavaElementTree {
	
	private List<JavaElementTree> children = new ArrayList<JavaElementTree>();
	private JavaElementTree parent;
	private URI uri;
	
	public JavaElementTree(JavaElementTree parent, URI uri) {
		this.parent = parent;
		this.uri = uri;
	}
	
	public List<JavaElementTree> getChildren() {
	    return children;
    }
	
	public JavaElementTree getParent() {
	    return parent;
    }
	
	public URI getElement() {
	    return uri;
    }
	
	public static JavaElementTree build(IJavaElement element) {
		JavaElementTree root = new JavaElementTree(null, JavaURIConverter.toURI(element));
		addChildren(root, element);
		return root;
	}
	
	public static void show(JavaElementTree tree, int level) {
		for (int i = 0; i < level; i++)
			System.out.print(" ");
		System.out.println(tree.getElement());
		Iterator<JavaElementTree> it = tree.getChildren().iterator();
		while (it.hasNext()) {
			JavaElementTree child = it.next();
			show(child, level + 1);
		}
	}
	
	protected static boolean isJar(IJavaElement element) {
		try {
			if (element instanceof IClassFile) {
				return true;
			}
			if (element instanceof IPackageFragment) {
				IPackageFragment fragment = (IPackageFragment)element;
				return fragment.getKind() == IPackageFragmentRoot.K_BINARY;
			}
			if (element instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot root = (IPackageFragmentRoot)element;
				return root.getKind() == IPackageFragmentRoot.K_BINARY;
			}
		}
		catch (Exception e) {
		}
		return false;
	}
	
	private static void addChildren(JavaElementTree element, IJavaElement javaElement) {
		if (javaElement instanceof IParent) {
			IParent parent = (IParent)javaElement;
			try {
	            IJavaElement[] children = parent.getChildren();
	            for (int i = 0; i < children.length; i++) {
	            	URI uri = JavaURIConverter.toURI(children[i]);
	            	JavaElementTree child = new JavaElementTree(element, uri);
	            	element.getChildren().add(child);
	            	addChildren(child, children[i]);
                }
            }
            catch (JavaModelException e) {
	            e.printStackTrace();
            }
		}
	}

}
