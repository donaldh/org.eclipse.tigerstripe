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

	private String artifactIcon = null;
	private String artifactIcon_gs = null;
	private String artifactIcon_new = null;

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
		this.artifactIcon = artifactIcon;
		this.artifactIcon_gs = artifactIcon_gs;
		this.artifactIcon_new = artifactIcon_new;
		this.artifactLabel = artifactLabel;
	}

	@SuppressWarnings("unchecked")
	public Class getSpecifiedClass() {
		return this.specifiedClass;
	}

	@Override
	public URL getGreyedoutIconURL() {
		try {
			return new URL(baseURL, artifactIcon_gs);
		} catch (MalformedURLException e) {
			// will never happen
			return baseURL;
		}
	}

	@Override
	public URL getIconURL() {
		try {
			return new URL(baseURL, artifactIcon);
		} catch (MalformedURLException e) {
			// will never happen
			return baseURL;
		}
	}

	@Override
	public String getLabel() {
		return artifactLabel;
	}

	@Override
	public URL getNewIconURL() {
		try {
			return new URL(baseURL, artifactIcon_new);
		} catch (MalformedURLException e) {
			// will never happen
			return baseURL;
		}
	}

}
