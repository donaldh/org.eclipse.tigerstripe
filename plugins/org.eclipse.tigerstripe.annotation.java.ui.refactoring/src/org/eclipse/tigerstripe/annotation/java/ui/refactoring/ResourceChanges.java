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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.resource.ResourceURIConverter;

/**
 * @author Yuri Strot
 *
 */
public class ResourceChanges implements IElementChanges {
	
	private IResource resource;
	
	public ResourceChanges(IResource resource) {
		this.resource = resource;
	}
	
	public IPath getPath() {
		return resource.getFullPath();
	}

	public Map<URI, URI> getChanges(Object newElement) {
		Map<URI, URI> changes = new HashMap<URI, URI>();
		IResource newResource = getResource(newElement);
		collectChanges(newResource, resource.getFullPath(), changes);
		return changes;
	}
	
	private static IResource getResource(Object element) {
		return (IResource)Platform.getAdapterManager(
				).getAdapter(element, IResource.class);
	}
	
	protected static void collectChanges(IResource resource, IPath oldPath, Map<URI, URI> changes) {
		changes.put(ResourceURIConverter.toURI(oldPath), ResourceURIConverter.toURI(resource));
		if (resource instanceof IContainer) {
			IContainer container = (IContainer)resource;
			try {
				//TODO how we going on to work with phantom and team private resources?
	            IResource[] members = container.members(0);
	            for (int i = 0; i < members.length; i++) {
	            	IResource child = members[i];
	            	IPath childOldPath = oldPath.append(child.getFullPath().lastSegment());
	            	collectChanges(child, childOldPath, changes);
                }
            }
            catch (CoreException e) {
	            e.printStackTrace();
            }
		}
	}

}
