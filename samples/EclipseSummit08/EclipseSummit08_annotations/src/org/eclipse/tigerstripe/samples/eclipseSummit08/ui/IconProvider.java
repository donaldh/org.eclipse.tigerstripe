/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.samples.eclipseSummit08.ui;

import java.net.URL;

import org.eclipse.tigerstripe.repository.metamodel.providers.IModelComponentIconProvider;
import org.eclipse.tigerstripe.samples.eclipseSummit08.Activator;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;

public class IconProvider implements IModelComponentIconProvider {

	protected URL baseURL = Activator.getDefault().getBundle().getEntry(
			"icons/"); //$NON-NLS-1$

	protected URL docEntity_url = null;

	public IconProvider() {
		try {
			docEntity_url = new URL(baseURL, "cube.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public URL getCreationToolIconURL(Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCreationToolLabel(Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	public URL getGreyedoutIconURL(Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	public URL getIconURL(Object context) {
		if (context == null)
			return null;
		if (context instanceof IAnnotationCapable) {
			IAnnotationCapable cap = (IAnnotationCapable) context;
			if (cap.hasAnnotations("Documentation")) {
				return docEntity_url;
			}
		}
		return null;
	}

}
