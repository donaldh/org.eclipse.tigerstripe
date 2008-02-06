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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.io.File;
import java.net.URI;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * This is the default project locator that will be used by the API unless a
 * specific one is used.
 * 
 * The location is basically computed as a local directory ref "../<project-label>".
 * 
 * @author Eric Dillon
 */
public class DefaultProjectLocator implements IProjectLocator {

	public URI locate(ITigerstripeProject projectContext, String projectLabel)
			throws TigerstripeException {
		URI uriContext = projectContext.getLocation().toFile().toURI();
		String newPath = uriContext.getPath() + File.separator + ".."
				+ File.separator + projectLabel;
		File newFile = new File(newPath);
		return newFile.toURI();
	}

	// in this default version the local label is only the last segment in the
	// path
	public String getLocalLabel(URI uri) throws TigerstripeException {
		String path = uri.getPath();
		int index = path.lastIndexOf(File.separator);
		if (index == -1)
			return path;
		return path.substring(index, path.length());
	}
}
