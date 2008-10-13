/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.util.Map;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;

/**
 * Generic interface for Project Creator classes that the
 * TigerstripeProjectFactory uses to create empty projects
 * 
 * @author erdillon
 * 
 */
public interface IProjectCreator {

	public IWorkspaceRunnable getRunnable(final String projectName,
			final IProjectDetails projectDetails, final IPath folder,
			final Map<String, Object> properties) throws TigerstripeException;

	public void assertValidProperties(Map<String, Object> properties)
			throws TigerstripeException;
}
