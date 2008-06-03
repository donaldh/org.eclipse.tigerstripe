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
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring.ChangesTracker;
import org.eclipse.tigerstripe.annotation.resource.ResourceURIConverter;

/**
 * @author Yuri Strot
 *
 */
public class ResourceRefactoringSupport implements IRefactoringChangesListener {
	
	private Map<ILazyObject, ResourceChanges> changes = new HashMap<ILazyObject, ResourceChanges>();
	
	private static ResourceRefactoringSupport resourceRefactoringSupport;
	
	private ResourceRefactoringSupport() {
	}
	
	public static void initialize() {
		if (resourceRefactoringSupport == null) {
			resourceRefactoringSupport = new ResourceRefactoringSupport();
			ChangesTracker.getInstance().addListener(resourceRefactoringSupport);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.jdt.refactoring.IRefactoringChangesListener#changed(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath, int)
	 */
	public void changed(ILazyObject oldPath, ILazyObject newPath, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			IResource resource = getResource(oldPath);
			if (resource != null) {
				ResourceChanges change = new ResourceChanges(resource);
				changes.put(oldPath, change);
			}
		}
		else if (kind == CHANGED) {
			IResource newResource = getResource(newPath);
			ResourceChanges change = changes.get(oldPath);
			if (newResource != null && change != null) {
				changed( change.getChanges(newResource));
			}
		}
	}
	
	protected void changed(Map<URI, URI> uris) {
		for (URI uri : uris.keySet())
			AnnotationPlugin.getManager().getRefactoringSupport().changed(uri, uris.get(uri));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.jdt.refactoring.IRefactoringChangesListener#deleted(org.eclipse.core.runtime.IPath)
	 */
	public void deleted(ILazyObject object) {
		IResource resource = getResource(object);
		if (resource != null) {
			URI uri = ResourceURIConverter.toURI(resource);
			if (uri != null)
				AnnotationPlugin.getManager().getRefactoringSupport().deleted(uri);
		}
	}
	
	protected IResource getResource(ILazyObject object) {
		Object obj = object.getObject();
		return (IResource)Platform.getAdapterManager().getAdapter(obj, IResource.class);
	}

}
