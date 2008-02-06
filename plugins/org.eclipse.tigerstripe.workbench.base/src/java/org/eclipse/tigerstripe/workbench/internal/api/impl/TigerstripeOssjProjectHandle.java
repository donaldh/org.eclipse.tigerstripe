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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.module.packaging.ModulePackager;

public class TigerstripeOssjProjectHandle extends TigerstripeProjectHandle {

	/**
	 * 
	 * @param projectURI -
	 *            URI of the project directory
	 */
	public TigerstripeOssjProjectHandle(URI projectURI)
			throws TigerstripeException {
		super(projectURI);
	}

	public IModulePackager getPackager() {
		return new ModulePackager(this);
	}

	@Override
	public WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		TigerstripeProjectHandle copy = new TigerstripeOssjProjectHandle(
				getURI());
		return copy;
	}

}
