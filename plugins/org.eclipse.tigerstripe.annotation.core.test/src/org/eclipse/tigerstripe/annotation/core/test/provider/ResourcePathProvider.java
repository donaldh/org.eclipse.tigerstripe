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
package org.eclipse.tigerstripe.annotation.core.test.provider;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.IAnnotationProvider;

/**
 * @author Yuri Strot
 *
 */
public class ResourcePathProvider implements IAnnotationProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationProvider#getObject(org.eclipse.emf.common.util.URI)
	 */
	public Object getObject(URI uri) {
		return toResourcePath(uri);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationProvider#getUri(java.lang.Object)
	 */
	public URI getUri(Object object) {
		if (object instanceof IResourcePath)
			return toURI((IResourcePath)object);
		return null;
	}
	
	protected static final String SCHEME_RESOURCE = "rpath";
	
	public static boolean isRelated(URI uri) {
		return SCHEME_RESOURCE.equals(uri.scheme());
	}
	
	public static URI toURI(IResourcePath path) {
		return toURI(path.getPath());
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
	
	public static IResourcePath toResourcePath(URI uri) {
		IResource resource = toResource(uri);
		if (resource == null)
			return null;
		return (IResourcePath)Platform.getAdapterManager().getAdapter(
				resource, IResourcePath.class);
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
