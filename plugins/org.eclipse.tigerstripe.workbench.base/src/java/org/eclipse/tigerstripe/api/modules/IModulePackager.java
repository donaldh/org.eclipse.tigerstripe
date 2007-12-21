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
package org.eclipse.tigerstripe.api.modules;

import java.io.File;
import java.net.URI;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.module.InvalidModuleException;

public interface IModulePackager {

	public IModuleHeader makeHeader();

	public void packageUp(URI jarURI, File classesDir, IModuleHeader header,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException,
			InvalidModuleException;
}
