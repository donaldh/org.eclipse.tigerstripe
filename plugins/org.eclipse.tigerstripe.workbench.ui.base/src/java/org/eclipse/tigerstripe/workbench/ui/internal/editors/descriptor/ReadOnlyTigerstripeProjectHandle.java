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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;

public class ReadOnlyTigerstripeProjectHandle extends TigerstripeProjectHandle {

	@Override
	public String getName() {
		return project.getProjectDetails().getName();
	}

	private TigerstripeProject project;

	public ReadOnlyTigerstripeProjectHandle(TigerstripeProject project) {
		super(null);
		setTSProject(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject#
	 * getReferencingModels()
	 */
	public ModelReference[] getReferencingModels(int level) throws TigerstripeException {
		//TODO should return the containing projects?
		return new ModelReference[0];
	}

	@Override
	protected void setTSProject(TigerstripeProject project) {
		this.project = project;
	}

	public IModulePackager getPackager() {
		return null;
	}

	@Override
	public TigerstripeProject getTSProject() throws TigerstripeException {
		return project;
	}

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		throw new TigerstripeException("Operation not supported.");
	}

	@Override
	public boolean isDirty() {
		return false;
	}
	
	
}
