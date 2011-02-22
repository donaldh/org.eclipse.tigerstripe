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
package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A Factory for new module refs.
 * 
 * @author Eric Dillon
 * @since 0.4
 */
public class ModuleRefFactory {

	// the singleton instance.
	private static ModuleRefFactory instance;

	public static ModuleRefFactory getInstance() {
		if (instance == null) {
			instance = new ModuleRefFactory();
		}
		return instance;
	}

	// Don't instanciate.
	private ModuleRefFactory() {
	}

	public ModuleRef parseModule(ITigerstripeModelProject container,
			URI jarURI, IProgressMonitor monitor) throws InvalidModuleException {
		return new ModuleRef(container, jarURI, monitor);
	}

	/**
	 * Parse the given URI (jar) into a module ref.
	 * 
	 * @return the newly created module ref.
	 */
	public ModuleRef parseModule(URI jarURI, IProgressMonitor monitor)
			throws InvalidModuleException {
		return parseModule(null, jarURI, monitor);
	}

	public IModuleHeader parseModuleHeader(URI jarURI,
			IProgressMonitor monitor) throws InvalidModuleException {
		UnparsedModuleRef ref = new UnparsedModuleRef(jarURI, monitor);
		return ref.getModuleHeader();
	}

	public IProjectDetails parseModuleDetails(URI jarURI,
			IProgressMonitor monitor) throws InvalidModuleException {
		UnparsedModuleRef ref = new UnparsedModuleRef(jarURI, monitor);
		return ref.getProjectDetails();
	}

}
