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
package org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.ISimplePluggablePluginProject;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;

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

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		throw new TigerstripeException("Operation not supported.");
	}

}
