/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - jworrell
 *******************************************************************************//**
 * 
 */
package org.eclipse.tigerstripe.annotation.tsmodel;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A utility for locating a Tigerstripe project by name
 * 
 * @author jworrell
 *
 */
public class TigerstripeProjectLocator {

	/**
	 * Returns the Tigerstripe project named by the supplied parameter 
	 * @param name name of the Tigerstripe project required
	 * @return the Tigerstripe project named by the supplied parameter
	 */
	public static IAbstractTigerstripeProject getProject(String name) throws TigerstripeException
	{
		IPath path = getProjectPath(name);
		IAbstractTigerstripeProject project = TigerstripeCore.findProject(path);
		if(project instanceof ITigerstripeModelProject)
			return project;
		else
			throw new TigerstripeException("TigerstripeProjectLocator: Failed to find TigerstripeModelProject for: "+name);
	}
	
	protected static IPath getProjectPath(String name)
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource tsContainer = root.findMember(new Path(name));
		IPath path = tsContainer.getLocation();
		return path;
	}
}
