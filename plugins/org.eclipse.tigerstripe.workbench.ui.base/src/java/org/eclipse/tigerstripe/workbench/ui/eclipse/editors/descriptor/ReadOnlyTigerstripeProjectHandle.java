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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.modules.IModulePackager;
import org.eclipse.tigerstripe.api.publish.IProjectCSVCreator;
import org.eclipse.tigerstripe.api.publish.IProjectPublisher;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public class ReadOnlyTigerstripeProjectHandle extends TigerstripeProjectHandle {

	private TigerstripeProject project;

	public ReadOnlyTigerstripeProjectHandle(TigerstripeProject project) {
		super(null);
		setTSProject(project);
	}

	@Override
	protected void setTSProject(TigerstripeProject project) {
		this.project = project;
	}

	public IProjectCSVCreator getCSVCreator() {
		return null;
	}

	public IModulePackager getPackager() {
		return null;
	}

	public IProjectPublisher getPublisher() {
		return null;
	}

	@Override
	public TigerstripeProject getTSProject() throws TigerstripeException {
		return project;
	}
}
