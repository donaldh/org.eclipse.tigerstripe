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

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.module.InvalidModuleException;

public interface IModulePackager {

	public IModuleHeader makeHeader();

	public void packageUp(URI jarURI, File classesDir, IModuleHeader header,
			boolean includeDiagrams, boolean includeAnnotations,
			IProgressMonitor monitor) throws TigerstripeException,
			InvalidModuleException;
}
