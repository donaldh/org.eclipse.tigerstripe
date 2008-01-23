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
package org.eclipse.tigerstripe.eclipse.utils;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * This is an Eclipse-specific project locator. It can map the project label as
 * stored in tigerstripe.xml descriptor into a proper URI
 * 
 * @author Eric Dillon
 * 
 */
public class ProjectLocatorFacilityForEclipse implements IProjectLocator {

	public URI locate(ITigerstripeProject projectContext, String projectLabel)
			throws TigerstripeException {

		IResource res = EclipsePlugin.getWorkspace().getRoot().findMember(
				projectLabel);
		if (res == null)
			throw new TigerstripeException("Unable to locate '" + projectLabel
					+ "'");
		return res.getLocation().toFile().toURI();
	}

	public String getLocalLabel(URI uri) throws TigerstripeException {

		URI phantomURI = PhantomTigerstripeProjectMgr.getInstance()
				.getPhantomURI();

		if (uri.equals(phantomURI))
			return "Phantom Project";
		IPath path = new Path(uri.getPath());

		IContainer[] containers = EclipsePlugin.getWorkspace().getRoot()
				.findContainersForLocation(path);
		// we're expecting a project here
		if (containers.length == 1 && containers[0] instanceof IProject) {
			IProject prj = (IProject) containers[0];
			return prj.getName();
		}

		throw new TigerstripeException("No valid Tigerstripe project at '"
				+ uri.toASCIIString() + "'.");
	}

}
