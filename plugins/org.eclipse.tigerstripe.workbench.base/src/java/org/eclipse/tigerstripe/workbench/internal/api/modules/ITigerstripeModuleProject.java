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
package org.eclipse.tigerstripe.workbench.internal.api.modules;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A Tigerstripe Module Project is a temporary project derived from a module for
 * generation purpose. Since 2.1, modules contained in a project can be
 * generated locally. To do so, we create a temporary project
 * (ITigerstripeModuleProject) and trigger generation on this one.
 * 
 * @author Eric Dillon
 * @since 2.1
 */
public interface ITigerstripeModuleProject extends ITigerstripeModelProject {

	/**
	 * When generating modules, we need to temporarily complete the path of
	 * dependencies so all symbols are resolved. This is only used during
	 * generation, on the fly, and is not persisted.
	 * 
	 * Once the module has been generated the
	 * {@link #clearTemporaryDependencies()} is called to clean that state.
	 * 
	 * @param dependency
	 * @throws TigerstripeException
	 */
	public void addTemporaryDependency(IDependency dependency)
			throws TigerstripeException;

	/**
	 * Once temporarty dependencies have all been added the dependencies content
	 * cache needs to be updated accordingly. It'll be re-built upon
	 * {@link #clearTemporaryDependencies()};
	 * 
	 * Note: this method needs to be manually called before triggering a
	 * generation of the module as the
	 * {@link #addTemporaryDependency(IDependency)} does not do it.
	 */
	public void updateDependenciesContentCache(
			IProgressMonitor monitor);

	/**
	 * Remove all temporary dependencies that were added for generation.
	 * 
	 * @see #addTemporaryDependency(IDependency)
	 */
	public void clearTemporaryDependencies(IProgressMonitor monitor);

	public URI getProjectContainerURI();
}
