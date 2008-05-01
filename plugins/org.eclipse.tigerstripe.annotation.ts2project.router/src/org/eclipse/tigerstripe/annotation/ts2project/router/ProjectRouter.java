/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Worrell (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ts2project.router;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;


/**
 * @author Yuri Strot
 *
 */
public class ProjectRouter implements EObjectRouter {
	
	private static final String ANNOTATIONS_FILE_NAME = ".annotations";

	
	public URI route(EObject object) {
		if (object instanceof Annotation) {
			Annotation annotation = (Annotation)object;
			URI uri = annotation.getUri();
			Object annotable = AnnotationPlugin.getManager().getObject(uri);
			if (annotable instanceof IModelComponent) {
				IModelComponent resource = (IModelComponent)annotable;
				return getUri(resource);
			}
		}
	    return null;
    }
	
	protected URI getUri(IModelComponent res) {
		try {
			IPath path = res.getProject().getLocation();
			if (path != null) {
				path = path.append(ANNOTATIONS_FILE_NAME);
				// Seem to need to use createFileURI rather than createPlatformURI to get proper behaviour...
				// not too sure why
				URI uri = URI.createFileURI(path.toString());
				return uri;
			}
		} catch (TigerstripeException e) {
			AnnotationPlugin.log(e);
		}

		return null;
	}
}
