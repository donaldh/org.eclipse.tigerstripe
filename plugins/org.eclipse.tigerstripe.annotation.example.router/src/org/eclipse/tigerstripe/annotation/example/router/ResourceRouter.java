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
package org.eclipse.tigerstripe.annotation.example.router;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;

/**
 * @author Yuri Strot
 *
 */
public class ResourceRouter implements EObjectRouter {
	
	private static final String ANNOTATIONS_FILE_NAME = ".annotations";
	
	protected URI getUri(IResource res) {
		IPath path = res.getProject().getLocation();
		if (path != null) {
			path = path.append(ANNOTATIONS_FILE_NAME);
			return URI.createFileURI(path.toFile().getAbsolutePath());
		}
		return null;
	}

	public URI route(EObject object) {
		if (object instanceof Annotation) {
			Annotation annotation = (Annotation)object;
			Object annotable = AnnotationPlugin.getManager().getAnnotatedObject(annotation);
			if (annotable instanceof IResource) {
				IResource resource = (IResource)annotable;
				return getUri(resource);
			}
		}
	    return null;
    }

}
