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
package org.eclipse.tigerstripe.workbench.project;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IWorkingCopy;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;

/**
 * Top level handle for any Tigerstripe project
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IAbstractTigerstripeProject extends IAdaptable, IWorkingCopy {

	public String getProjectLabel();

	public IProjectDetails getProjectDetails() throws TigerstripeException;

	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException;

	/**
	 * @return true if the project exists (i.e. a valid project descriptor was
	 *         found in the handle)
	 * 
	 */
	public boolean exists();

	/**
	 * Returns the full absolute path of this project.
	 * 
	 * @return
	 */
	public IPath getLocation();

	/**
	 * Returns the full, absolute path of this project relative to the
	 * workspace.
	 * 
	 * @return
	 */
	public IPath getFullPath();

	public void delete(boolean force, IProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * Returns true if the project has any ERROR level marker (i.e. if any error
	 * appears for this project in the ProblemsView)
	 * 
	 * @return true if the project contains ERROR markers, false otherwise.
	 */
	public boolean containsErrors();
}
