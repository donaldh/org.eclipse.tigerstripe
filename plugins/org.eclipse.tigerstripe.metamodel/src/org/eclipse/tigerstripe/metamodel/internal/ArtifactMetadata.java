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

import java.net.URL;

import org.eclipse.tigerstripe.metamodel.IArtifactMetadata;

public class ArtifactMetadata extends ModelComponentMetadata implements
		IArtifactMetadata {

	private boolean hasFields = false;
	private boolean hasMethods = false;
	private boolean hasLiterals = false;

	@SuppressWarnings("unchecked")
	/* package */ArtifactMetadata(Class specifiedClass, boolean hasFields,
			boolean hasMethods, boolean hasLiterals, String artifactIcon,
			String artifactIcon_gs, String artifactIcon_new,
			String artifactLabel) {
		super(specifiedClass, artifactIcon, artifactIcon_gs, artifactIcon_new,
				artifactLabel);
		this.hasFields = hasFields;
		this.hasMethods = hasMethods;
		this.hasLiterals = hasLiterals;
	}

	@SuppressWarnings("unchecked")
	/* package */ArtifactMetadata(Class specifiedClass, boolean hasFields,
			boolean hasMethods, boolean hasLiterals, URL artifactIconURL,
			URL artifactIcon_gsURL, URL artifactIcon_newURL,
			String artifactLabel) {
		super(specifiedClass, artifactIconURL, artifactIcon_gsURL,
				artifactIcon_newURL, artifactLabel);
		this.hasFields = hasFields;
		this.hasMethods = hasMethods;
		this.hasLiterals = hasLiterals;
	}

	@Override
	public boolean hasFields() {
		return hasFields;
	}

	@Override
	public boolean hasLiterals() {
		return hasLiterals;
	}

	@Override
	public boolean hasMethods() {
		return hasMethods;
	}

}
