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
package org.eclipse.tigerstripe.annotation.java;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.annotation.core.IAnnotationProvider;

/**
 * @author Yuri Strot
 *
 */
public class JavaAnnotationProvider implements IAnnotationProvider {

	public Object getObject(URI uri) {
	    return JavaURIConverter.toJava(uri);
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationProvider#getURI(java.lang.Object)
	 */
	public URI getUri(Object object) {
		if (object instanceof IJavaElement) {
			IJavaElement element = (IJavaElement)object;
			IJavaProject project = element.getJavaProject();
			IResource res = project.getResource();
			if (res != null) {
				IProject pr = res.getProject();
				if (pr != null && !pr.isOpen())
					return null;
			}
			return JavaURIConverter.toURI(element);
		}
		return null;
	}

}
