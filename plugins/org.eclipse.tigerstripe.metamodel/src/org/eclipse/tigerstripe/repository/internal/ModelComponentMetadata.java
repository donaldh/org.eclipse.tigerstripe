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
package org.eclipse.tigerstripe.repository.internal;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.tigerstripe.repository.metamodel.providers.IModelComponentIconProvider;

public class ModelComponentMetadata implements IModelComponentMetadata {

	private URL artifactIcon_URL = null;
	private URL artifactIcon_gs_URL = null;
	private URL artifactIcon_new_URL = null;

	private IModelComponentIconProvider provider;

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

	public void setIconProvider(IModelComponentIconProvider provider) {
		this.provider = provider;
	}

	@SuppressWarnings("unchecked")
	public Class getSpecifiedClass(Object context) {
		return this.specifiedClass;
	}

	public URL getGreyedoutIconURL(Object context) {
		if (provider != null) {
			URL result = provider.getGreyedoutIconURL(context);
			if (result != null) {
				return result;
			}
		}
		return artifactIcon_gs_URL;
	}

	public URL getIconURL(Object context) {
		if (provider != null) {
			URL result = provider.getIconURL(context);
			if (result != null)
				return result;
		}
		return artifactIcon_URL;
	}

	public String getLabel(Object context) {
		if (provider != null) {
			String result = provider.getCreationToolLabel(context);
			if (result != null)
				return result;
		}

		return artifactLabel;
	}

	public URL getNewIconURL(Object context) {
		if (provider != null) {
			URL result = provider.getCreationToolIconURL(context);
			if (result != null)
				return result;
		}
		return artifactIcon_new_URL;
	}

	public void setNewIconURL(URL url) {
		this.artifactIcon_new_URL = url;
	}

	public void setGreyedoutIconURL(URL url) {
		this.artifactIcon_gs_URL = url;
	}

	public void setIconURL(URL url) {
		this.artifactIcon_URL = url;
	}
}
