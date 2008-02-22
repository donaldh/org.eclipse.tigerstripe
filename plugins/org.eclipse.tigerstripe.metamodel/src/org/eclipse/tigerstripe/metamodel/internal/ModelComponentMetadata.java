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
package org.eclipse.tigerstripe.metamodel.internal;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.tigerstripe.metamodel.Activator;
import org.eclipse.tigerstripe.metamodel.IModelComponentMetadata;

public class ModelComponentMetadata implements IModelComponentMetadata {

	private URL artifactIcon_URL = null;
	private URL artifactIcon_gs_URL = null;
	private URL artifactIcon_new_URL = null;

	private String artifactLabel = null;

	@SuppressWarnings("unchecked")
	private Class specifiedClass = null;

	protected URL baseURL = Activator.getDefault().getBundle().getEntry(
			"icons/"); //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	/* package */ModelComponentMetadata(Class specifiedClass,
			String artifactIcon, String artifactIcon_gs,
			String artifactIcon_new, String artifactLabel) {
		this.specifiedClass = specifiedClass;
		try {
			this.artifactIcon_URL = new URL(baseURL, artifactIcon);
			this.artifactIcon_gs_URL = new URL(baseURL, artifactIcon_gs);
			this.artifactIcon_new_URL = new URL(baseURL, artifactIcon_new);
		} catch (MalformedURLException e) {
			// ignore here
		}
		this.artifactLabel = artifactLabel;
	}

	@SuppressWarnings("unchecked")
	/* package */ModelComponentMetadata(Class specifiedClass,
			URL artifactIconURL, URL artifactIcon_gsURL,
			URL artifactIcon_newURL, String artifactLabel) {
		this.specifiedClass = specifiedClass;
		this.artifactIcon_URL = artifactIconURL;
		this.artifactIcon_gs_URL = artifactIcon_gsURL;
		this.artifactIcon_new_URL = artifactIcon_newURL;
		this.artifactLabel = artifactLabel;
	}

	@SuppressWarnings("unchecked")
	public Class getSpecifiedClass() {
		return this.specifiedClass;
	}

	@Override
	public URL getGreyedoutIconURL() {
		return artifactIcon_gs_URL;
	}

	@Override
	public URL getIconURL() {
		return artifactIcon_URL;
	}

	@Override
	public String getLabel() {
		return artifactLabel;
	}

	@Override
	public URL getNewIconURL() {
		return artifactIcon_new_URL;
	}

}
