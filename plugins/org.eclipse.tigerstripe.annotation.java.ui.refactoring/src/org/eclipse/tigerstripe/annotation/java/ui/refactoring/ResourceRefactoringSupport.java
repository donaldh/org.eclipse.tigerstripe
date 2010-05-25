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
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringDelegate;
import org.eclipse.tigerstripe.annotation.resource.ResourceURIConverter;

/**
 * @author Yuri Strot
 * 
 */
public class ResourceRefactoringSupport implements IRefactoringChangesListener {

	private Map<ILazyObject, ResourceChanges> changes = new HashMap<ILazyObject, ResourceChanges>();

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.jdt.refactoring.
	 * IRefactoringChangesListener#changed(org.eclipse.core.runtime.IPath,
	 * org.eclipse.core.runtime.IPath, int)
	 */
	public void changed(IRefactoringDelegate delegate, ILazyObject oldObject,
			ILazyObject newObject, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			IResource resource = getResource(oldObject);
			if (resource != null) {
				ResourceChanges change = new ResourceChanges(resource);
				changes.put(oldObject, change);
			}
		} else if (kind == CHANGED) {
			IResource newResource = getResource(newObject);
			ResourceChanges change = changes.remove(oldObject);
			if (newResource != null && change != null) {
				changed(delegate, change.getChanges(newResource));
			}
		}
	}

	public void moved(IRefactoringDelegate delegate, ILazyObject[] objects, ILazyObject destination, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			for (int i = 0; i < objects.length; i++) {
				IResource element = getResource(objects[i]);
				if (element != null) {
					ResourceChanges change = new ResourceChanges(element);
					changes.put(objects[i], change);
				}
			}
		} else if (kind == CHANGED) {
			Map<URI, URI> allChanges = new HashMap<URI, URI>();
			for (int i = 0; i < objects.length; i++) {
				ResourceChanges change = changes.remove(objects[i]);
				IResource element = getResource(destination);
				if (element != null && change != null) {
					IPath oldPath = change.getPath();
					IPath newPath = element.getFullPath().append(
							oldPath.lastSegment());
					IResource newResource = ResourcesPlugin.getWorkspace()
							.getRoot().findMember(newPath);
					allChanges.putAll(change.getChanges(newResource));
				}
			}
			changed(delegate, allChanges);
		}
	}

	public void copied(IRefactoringDelegate delegate, ILazyObject[] objects, ILazyObject destination,
			Map<ILazyObject, String> newNames, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			for (int i = 0; i < objects.length; i++) {
				IResource element = getResource(objects[i]);
				if (element != null) {
					ResourceChanges change = new ResourceChanges(element);
					changes.put(objects[i], change);
				}
			}
		} else if (kind == CHANGED) {
			Map<URI, URI> allChanges = new HashMap<URI, URI>();
			for (int i = 0; i < objects.length; i++) {
				ResourceChanges change = changes.remove(objects[i]);
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
			copied(delegate, allChanges);
		}
	}

	private void changed(final IRefactoringDelegate delegate,
			final Map<URI, URI> uris) {
		// inform annotation framework about changes
		if (uris.size() == 0)
			return;
		new Thread() {
			public void run() {
				for (URI uri : uris.keySet())
					delegate.changed(uri, uris.get(uri), true);
			}
		}.start();
	}

	private void copied(final IRefactoringDelegate delegate,
			final Map<URI, URI> uris) {
		if (uris.size() == 0)
			return;
		new Thread() {
			public void run() {
				for (Entry<URI, URI> entry : uris.entrySet()) {
					URI fromUri = entry.getKey();
					URI toUri = entry.getValue();
					delegate.copied(fromUri, toUri, true);
				}
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.jdt.refactoring.
	 * IRefactoringChangesListener#deleted(org.eclipse.core.runtime.IPath)
	 */
	public void deleted(final IRefactoringDelegate delegate, ILazyObject object) {
		IResource resource = getResource(object);
		if (resource != null) {
			final URI uri = ResourceURIConverter.toURI(resource);
			if (uri != null) {
				new Thread() {
					public void run() {
						delegate.deleted(uri, true);
					}
				}.start();
			}
		}
	}

	protected IResource getResource(ILazyObject object) {
		Object obj = object.getObject();
		if (obj == null)
			return null;
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
