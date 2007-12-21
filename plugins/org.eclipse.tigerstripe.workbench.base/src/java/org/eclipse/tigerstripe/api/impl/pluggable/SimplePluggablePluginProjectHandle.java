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
package org.eclipse.tigerstripe.api.impl.pluggable;

import java.net.URI;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.plugins.pluggable.ISimplePluggablePluginProject;
import org.eclipse.tigerstripe.api.project.ITigerstripeVisitor;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class SimplePluggablePluginProjectHandle extends
		AbstractPluggablePluginProjectHandle implements
		ISimplePluggablePluginProject {

	public final static String DESCRIPTOR_FILENAME = "ts-plugin.xml";

	public String getDescriptorFilename() {
		return DESCRIPTOR_FILENAME;
	}

	public SimplePluggablePluginProjectHandle(URI projectURI) {
		super(projectURI);
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		// TODO Auto-generated method stub

	}

}
