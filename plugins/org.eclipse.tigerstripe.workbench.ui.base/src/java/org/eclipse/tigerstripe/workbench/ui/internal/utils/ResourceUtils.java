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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ResourceUtils {

	/**
	 * Create this folder recursively if necessary
	 * 
	 * @param folder
	 */
	public static void createFolders(IFolder folder, IProgressMonitor monitor)
			throws CoreException {
		if (!folder.exists()) {
			if (folder.getParent() != null && !folder.getParent().exists()) {
				IProject project = folder.getProject();
				IFolder parentFolder = project.getFolder(folder.getParent()
						.getProjectRelativePath());
				createFolders(parentFolder, monitor);
			}
			folder.create(IResource.FORCE, true, monitor);
		}
	}
}
