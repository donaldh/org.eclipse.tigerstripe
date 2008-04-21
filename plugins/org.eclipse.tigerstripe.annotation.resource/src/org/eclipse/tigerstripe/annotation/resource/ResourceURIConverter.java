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
package org.eclipse.tigerstripe.annotation.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;

/**
 * @author Yuri Strot
 *
 */
public class ResourceURIConverter {
	
	protected static final String SCHEME_RESOURCE = "resource";
	
	public static boolean isRelated(URI uri) {
		return SCHEME_RESOURCE.equals(uri.scheme());
	}
	
	public static URI toURI(IResource resource) {
		return toURI(resource.getFullPath());
	}
	
	public static URI toURI(IPath path) {
		try {
			return URI.createHierarchicalURI(SCHEME_RESOURCE, null, null, path.segments(), null, null);
        }
        catch (IllegalArgumentException e) {
	        e.printStackTrace();
        }
        return null;
	}
	
	public static IResource toResource(URI uri) {
		IPath path = toPath(uri);
		if (path != null)
			return ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		return null;
	}
	
	public static IPath toPath(URI uri) {
		if (isRelated(uri))
			return new Path(uri.path());
		return null;
	}

}
