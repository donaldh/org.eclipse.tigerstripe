/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.ui.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotable;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;

public class DefaultAnnotable implements IAnnotable {

	private IResource resource;

	public DefaultAnnotable(IResource resource) {
		this.resource = resource;
	}

	public AnnotationStore getStore( IAnnotationScheme scheme ) throws AnnotationCoreException {
		return AnnotationStore.getDefaultFactory().getAnnotationStore(
				resource.getProject(), scheme);
	}

	public String getURI() throws AnnotationCoreException {
		IAdaptable adaptable = (IAdaptable) resource;
		if (adaptable.getAdapter(IProject.class) != null) {
			String uri = resource.getLocationURI().toString();
			uri = uri.replaceAll("file:/", "project:/");
			return uri;
		} else {
			return resource.getLocationURI().toString();
		}
	}

}
