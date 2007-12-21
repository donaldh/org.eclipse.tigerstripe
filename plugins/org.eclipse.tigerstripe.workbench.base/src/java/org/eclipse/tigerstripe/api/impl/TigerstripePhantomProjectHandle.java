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
package org.eclipse.tigerstripe.api.impl;

import java.net.URI;

import org.eclipse.tigerstripe.api.artifacts.model.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.modules.IModulePackager;
import org.eclipse.tigerstripe.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.api.publish.IProjectCSVCreator;
import org.eclipse.tigerstripe.api.publish.IProjectPublisher;
import org.eclipse.tigerstripe.core.module.packaging.ModulePackager;
import org.eclipse.tigerstripe.core.profile.PhantomTigerstripeProject;
import org.eclipse.tigerstripe.core.profile.PhantomTigerstripeProjectMgr;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

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

	public IPrimitiveTypeArtifact[] getReservedPrimitiveTypes()
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

	public IProjectPublisher getPublisher() {
		return null;
	}

	public IProjectCSVCreator getCSVCreator() {
		return new ProjectCSVCreator(this);
	}

	public static URI getPhantomURI() {
		return PhantomTigerstripeProjectMgr.getInstance().getPhantomURI();
	}
}
