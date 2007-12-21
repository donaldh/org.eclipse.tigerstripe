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
package org.eclipse.tigerstripe.core.module;

import java.net.URI;

import org.eclipse.tigerstripe.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;

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

	/**
	 * Parse the given URI (jar) into a module ref.
	 * 
	 * @return the newly created module ref.
	 */
	public ModuleRef parseModule(URI jarURI, ITigerstripeProgressMonitor monitor)
			throws InvalidModuleException {
		return new ModuleRef(jarURI, monitor);
	}

	public IModuleHeader parseModuleHeader(URI jarURI,
			ITigerstripeProgressMonitor monitor) throws InvalidModuleException {
		UnparsedModuleRef ref = new UnparsedModuleRef(jarURI, monitor);
		return ref.getModuleHeader();
	}

	public IProjectDetails parseModuleDetails(URI jarURI,
			ITigerstripeProgressMonitor monitor) throws InvalidModuleException {
		UnparsedModuleRef ref = new UnparsedModuleRef(jarURI, monitor);
		return ref.getProjectDetails();
	}

}
