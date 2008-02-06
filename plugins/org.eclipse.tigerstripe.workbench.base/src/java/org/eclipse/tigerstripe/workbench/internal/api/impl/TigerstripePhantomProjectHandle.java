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
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.module.packaging.ModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;

public class TigerstripePhantomProjectHandle extends TigerstripeProjectHandle
		implements IPhantomTigerstripeProject {

	/**
	 * 
	 * @param projectURI -
	 *            URI of the project directory
	 */
	public TigerstripePhantomProjectHandle() throws TigerstripeException {
		super(getPhantomURI());
	}

	public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypes()
			throws TigerstripeException {
		return getArtifactManagerSession().getReservedPrimitiveTypes();
	}

	@Override
	public TigerstripeProject getTSProject() throws TigerstripeException {
		return getPhantomProject();
	}

	public PhantomTigerstripeProject getPhantomProject()
			throws TigerstripeException {
		// there's only one phantom project it's maintained exclusively by
		// the PhantomTigerstripeProjectMgr
		return PhantomTigerstripeProjectMgr.getInstance().getPhantomProject();
	}

	public IModulePackager getPackager() {
		return new ModulePackager(this);
	}

	public static URI getPhantomURI() {
		return PhantomTigerstripeProjectMgr.getInstance().getPhantomURI();
	}

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		throw new TigerstripeException("Operation not supported.");
	}

}
