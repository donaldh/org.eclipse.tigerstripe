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
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;

/**
 * Top level handle for any Tigerstripe project
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IAbstractTigerstripeProject extends IAdaptable, IWorkingCopy {

	public String getProjectLabel();

	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException;

	public IProjectDetails getProjectDetails() throws TigerstripeException;

	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException;

	/**
	 * A proper comparison mechanism for IAbstractTigerstripeProject
	 * 
	 * @param obj -
	 *            the other tigerstripe project to compare with
	 * @return true, if the project passed as an argument as the same class type
	 *         and the same URI.
	 */
	public boolean equals(Object obj);

	/**
	 * Returns the descriptor filename for this type of Project
	 * 
	 * @return
	 */
	public String getDescriptorFilename();

	/**
	 * @return true if the project exists (i.e. a valid project descriptor was
	 *         found in the handle)
	 * 
	 */
	public boolean exists();

	public IPath getLocation();

	public void delete(boolean force, IProgressMonitor monitor)
			throws TigerstripeException;
}
