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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.module.InvalidModuleException;

/**
 * A Tigerstripe Dependency represents a .tsm file that contains Artifacts that
 * have been "exported to a module" so they can be shared, as a library, between
 * other projects.
 * 
 * Once a Dependency is attached to a Tigerstripe project, all the Artifacts
 * contained in this dependency are available within the current project.
 * 
 * <b>NOTE: The path to a dependency is considered relative to the project it is
 * being attached to.</b>
 * 
 * @author Eric Dillon
 * 
 */
public interface IDependency {

	/** Extension for a Tigerstripe module (JAR) file * */
	public final String EXTENSION = "jar";

	/**
	 * Default Dependency required on all projects This is to be compared with
	 * the module ID.
	 * 
	 * @since 1.0.3
	 */
	public final String DEFAULT_CORE_MODEL_DEPENDENCY = "distrib.core.model";

	public IProjectDetails getIProjectDetails();

	public IModuleHeader getIModuleHeader();

	/**
	 * This is a minimalistic method that is only intended to be used by the
	 * GUI, so the whole module doesn't have to be parsed to provide feedback to
	 * the user.
	 * 
	 * @return
	 */
	public IModuleHeader parseIModuleHeader() throws InvalidModuleException;

	/**
	 * This is a minimalistic method that is only intended to be used by the
	 * GUI, so the whole module doesn't have to be parsed to provide feedback to
	 * the user.
	 * 
	 * @return
	 */
	public IProjectDetails parseIProjectDetails() throws InvalidModuleException;

	/**
	 * The relative path to this dependency
	 * 
	 * @return the relative path from the project is attached to (or "to be"
	 *         attached to).
	 */
	public String getPath();

	/**
	 * Whether this is a valid dependency or not
	 * 
	 * @return
	 */
	public boolean isValid(ITigerstripeProgressMonitor monitor);

	/**
	 * Whether this is a valid dependency or not This is equivalent to
	 * isValid(null)
	 * 
	 * @return
	 */
	public boolean isValid();

	/**
	 * Make module project
	 */
	public ITigerstripeModuleProject makeModuleProject(
			ITigerstripeProject containingProject) throws TigerstripeException;
}
