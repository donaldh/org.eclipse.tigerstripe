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
import java.util.Map.Entry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;

/**
 * @author Yuri Strot
 * 
 */
public class JavaRefactoringSupport implements IRefactoringChangesListener {

	private final Map<ILazyObject, JavaChanges> changes = new HashMap<ILazyObject, JavaChanges>();

	public void changed(ILazyObject oldPath,
			ILazyObject newPath, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			IJavaElement element = getJavaElement(oldPath);
			if (element != null) {
				JavaChanges change = new JavaChanges(element);
				changes.put(oldPath, change);
			}
		} else if (kind == CHANGED) {
			IJavaElement newElement = getJavaElement(newPath);
			JavaChanges change = changes.remove(oldPath);
			if (newElement != null && change != null) {
				changed(change.getChanges(newElement));
			}
		}
	}

	public void moved(ILazyObject[] objects,
			ILazyObject destination, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			for (int i = 0; i < objects.length; i++) {
				IJavaElement element = getJavaElement(objects[i]);
				if (element != null) {
					JavaChanges change = new JavaChanges(element);
					changes.put(objects[i], change);
				}
			}
		} else if (kind == CHANGED) {
			Map<URI, URI> allChanges = new HashMap<URI, URI>();
			for (int i = 0; i < objects.length; i++) {
				JavaChanges change = changes.remove(objects[i]);
				IResource element = getResource(destination);
				if (element != null && change != null) {
					IPath oldPath = change.getPath();
					IPath newPath = element.getFullPath().append(
							oldPath.lastSegment());
					IResource newResource = ResourcesPlugin.getWorkspace()
							.getRoot().findMember(newPath);
					if (newResource != null) {
						allChanges.putAll(change.getChanges(newResource));
					}
				}
			}
			changed(allChanges);
		}
	}

	public void copied(ILazyObject[] objects,
			ILazyObject destination, Map<ILazyObject, String> newNames, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			for (int i = 0; i < objects.length; i++) {
				IJavaElement element = getJavaElement(objects[i]);
				if (element != null) {
					JavaChanges change = new JavaChanges(element);
					changes.put(objects[i], change);
				}
			}
		} else if (kind == CHANGED) {
			final Map<URI, URI> allChanges = new HashMap<URI, URI>();
			for (int i = 0; i < objects.length; i++) {
				JavaChanges change = changes.remove(objects[i]);
				String newName = newNames.get(objects[i]);
				IResource element = getResource(destination);
				if (element != null && change != null) {
					if (!(element instanceof IContainer)) {
						element = element.getParent();
					}
					IPath oldPath = change.getPath();
					String name = oldPath.lastSegment();
					if (newName != null && newName.length() > 0) {
						name = newName;
					}
					IPath newPath = element.getFullPath().append(name);
					IResource newResource = ResourcesPlugin.getWorkspace()
							.getRoot().findMember(newPath);
					if (newResource != null) {
						allChanges.putAll(change.getChanges(newResource));
					}
				}
			}
			copied(allChanges);
		}
	}

	private void changed(final Map<URI, URI> uris) {
		if (uris.size() == 0) {
			return;
		}
		for (URI uri : uris.keySet()) {
			AnnotationPlugin.getManager().changed(uri, uris.get(uri), false);
		}
	}

	private void copied(final Map<URI, URI> uris) {
		if (uris.size() == 0) {
			return;
		}
		for (Entry<URI, URI> entry : uris.entrySet()) {
			URI fromUri = entry.getKey();
			URI toUri = entry.getValue();
			AnnotationPlugin.getManager().copied(fromUri, toUri, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.jdt.refactoring.
	 * IRefactoringChangesListener#deleted(org.eclipse.core.runtime.IPath)
	 */
	public void deleted(ILazyObject object) {
		IJavaElement element = getJavaElement(object);
		if (element != null) {
			final URI uri = JavaURIConverter.toURI(element);
			if (uri != null) {
				AnnotationPlugin.getManager().deleted(uri, true);
			}
		}
	}

	protected IJavaElement getJavaElement(ILazyObject object) {
		Object obj = object.getObject();
		if (obj == null) {
			return null;
		}
		IJavaElement element = null;
		if (obj instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) obj;
			element = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
		}
		if (element == null) {
			element = (IJavaElement) Platform.getAdapterManager().getAdapter(
					obj, IJavaElement.class);
		}
		return element;
	}

	protected IResource getResource(ILazyObject object) {
		Object obj = object.getObject();
		if (obj == null) {
			return null;
		}
		IResource resource = null;
		if (obj instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) obj;
			resource = (IResource) adaptable.getAdapter(IResource.class);
		}
		if (resource == null) {
			resource = (IResource) Platform.getAdapterManager().getAdapter(obj,
					IResource.class);
		}
		return resource;
	}

}
