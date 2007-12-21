/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.core.versioning;

import java.net.URI;

import org.dom4j.Element;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.versioning.IVersionAwareElement;

public abstract class VersionAwareElement extends BaseElement implements
		IVersionAwareElement {

	private String version = DEFAULT_VERSION;

	public VersionAwareElement(URI uri) {
		super(uri);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	protected void appendHeader(Element rootElement) {
		super.appendHeader(rootElement);
		rootElement.addAttribute("version", getVersion());
	}

	@Override
	protected void parseHeader(Element rootElement) throws TigerstripeException {
		super.parseHeader(rootElement);

		String version = rootElement.attributeValue("version");
		if (version == null) {
			version = DEFAULT_VERSION;
		}
		setVersion(version);
	}

}
