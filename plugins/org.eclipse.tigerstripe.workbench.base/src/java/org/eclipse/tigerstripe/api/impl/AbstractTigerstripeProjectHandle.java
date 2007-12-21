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
package org.eclipse.tigerstripe.api.impl;

import java.io.File;
import java.net.URI;

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.core.cli.App;

public abstract class AbstractTigerstripeProjectHandle {

	public abstract String getProjectLabel();

	// we keep track of a TStamp on the hanlde when we create it to know
	// when the handle is invalid because the underlying URI actually changed
	private long handleTStamp;

	/** logger for output */
	private static Logger log = Logger.getLogger(App.class);

	protected URI projectContainerURI;

	// The folder for this project handle
	protected File projectContainer;

	public AbstractTigerstripeProjectHandle(URI projectContainerURI) {
		this.projectContainerURI = projectContainerURI;

		if (projectContainerURI == null)// Read-only tigerstripe project from
			// module
			return;

		File file = new File(projectContainerURI);
		if (file != null && file.exists()) {
			handleTStamp = file.lastModified();
		}
	}

	public File getBaseDir() {
		if (getProjectContainerURI() != null)
			return new File(getProjectContainerURI());
		return null;
	}

	public URI getProjectContainerURI() {
		return this.projectContainerURI;
	}

	public URI getURI() {
		return getProjectContainerURI();
	}

	public abstract boolean exists();

	/**
	 * Tries and locate the project descriptor for this project
	 * 
	 * @return
	 */
	protected abstract boolean findProjectDescriptor();

	public File getProjectDescriptorFile() {

		return projectContainer;
	}

	public abstract void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException;

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;

		if (arg0.getClass() == this.getClass())
			return ((AbstractTigerstripeProjectHandle) arg0).getURI().equals(
					this.getURI());
		return false;
	}

	public long handleTStamp() {
		return this.handleTStamp;
	}
}
