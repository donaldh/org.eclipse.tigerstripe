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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;

/**
 * @author Yuri Strot
 *
 */
public class JavaRefactoringSupport implements IRefactoringChangesListener {
	
	private Map<ILazyObject, JavaChanges> changes = new HashMap<ILazyObject, JavaChanges>();

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.java.ui.refactoring.IRefactoringChangesListener#changed(org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject, org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject, int)
	 */
	public void changed(ILazyObject oldPath, ILazyObject newPath, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			IJavaElement element = getJavaElement(oldPath);
			if (element != null) {
				JavaChanges change = new JavaChanges(element);
				changes.put(oldPath, change);
			}
		}
		else if (kind == CHANGED) {
			IJavaElement newElement = getJavaElement(newPath);
			JavaChanges change = changes.remove(oldPath);
			if (newElement != null && change != null) {
				changed( change.getChanges(newElement));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.java.ui.refactoring.IRefactoringChangesListener#moved(org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject[], org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject, int)
	 */
	public void moved(ILazyObject[] objects, ILazyObject destination, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			for (int i = 0; i < objects.length; i++) {
				IJavaElement element = getJavaElement(objects[i]);
				if (element != null) {
					JavaChanges change = new JavaChanges(element);
					changes.put(objects[i], change);
				}
			}
		}
		else if (kind == CHANGED) {
			Map<URI, URI> allChanges = new HashMap<URI, URI>();
			for (int i = 0; i < objects.length; i++) {
				JavaChanges change = changes.remove(objects[i]);
				IResource element = getResource(destination);
				if (element != null && change != null) {
					IPath oldPath = change.getPath();
					IPath newPath = element.getFullPath().append(oldPath.lastSegment());
					IResource newResource = ResourcesPlugin.getWorkspace().getRoot().findMember(newPath);
					allChanges.putAll(change.getChanges(newResource));
				}
			}
			changed(allChanges);
		}
	}
	
	protected void changed(Map<URI, URI> uris) {
		for (URI uri : uris.keySet())
			AnnotationPlugin.getManager().getRefactoringSupport().changed(uri, uris.get(uri), false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.jdt.refactoring.IRefactoringChangesListener#deleted(org.eclipse.core.runtime.IPath)
	 */
	public void deleted(ILazyObject object) {
		IJavaElement element = getJavaElement(object);
		if (element != null) {
			URI uri = JavaURIConverter.toURI(element);
			if (uri != null)
				AnnotationPlugin.getManager().getRefactoringSupport().deleted(uri, true);
		}
	}
	
	protected IJavaElement getJavaElement(ILazyObject object) {
		Object obj = object.getObject();
		return (IJavaElement)Platform.getAdapterManager().getAdapter(obj, IJavaElement.class);
	}
	
	protected IResource getResource(ILazyObject object) {
		Object obj = object.getObject();
		return (IResource)Platform.getAdapterManager().getAdapter(obj, IResource.class);
	}

}
