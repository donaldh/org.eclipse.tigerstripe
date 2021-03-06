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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.refactoring.IElementChanges;
import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;

/**
 * @author Yuri Strot
 * 
 */
public class JavaChanges implements IElementChanges {

	private JavaElementTree tree;
	private IPath path;

	public JavaChanges(IJavaElement element) {
		tree = JavaElementTree.build(element);
		path = element.getPath();
	}

	public IPath getPath() {
		return path;
	}

	public Map<URI, URI> getChanges(Object newElement) {
		Map<URI, URI> changes = new HashMap<URI, URI>();
		IJavaElement newJavaElement = getJavaElement(newElement);
		if (newJavaElement != null) {
			collectChanges(newJavaElement, tree, changes);
		}
		return changes;
	}

	private static IJavaElement getJavaElement(Object element) {
		return (IJavaElement) Platform.getAdapterManager().getAdapter(element,
				IJavaElement.class);
	}

	private static void collectChanges(IJavaElement element,
			JavaElementTree tree, Map<URI, URI> changes) {
		URI newUri = JavaURIConverter.toURI(element);
		changes.put(tree.getElement(), newUri);
		if (element instanceof IParent) {
			IJavaElement[] children;
			try {
				children = JavaElementTree.getChildren(element);

				JavaElementTree[] elements = tree.getChildren().toArray(
						new JavaElementTree[tree.getChildren().size()]);
				Map<URI, JavaElementTree> elementsMap = new HashMap<URI, JavaElementTree>(
						elements.length);

				for (JavaElementTree javaElementTree : elements) {
					URI el = javaElementTree.getElement();
					if (el != null) {
						elementsMap.put(el, javaElementTree);
					}
				}

				for (int i = 0; i < children.length; i++) {
					IJavaElement child = children[i];
					URI childUri = JavaURIConverter.toURI(child);
					if (childUri == null)
						continue;

					JavaElementTree elementTree = elementsMap.get(childUri);
					if (elementTree != null) {
						collectChanges(child, elementTree, changes);
					}
				}
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

}
