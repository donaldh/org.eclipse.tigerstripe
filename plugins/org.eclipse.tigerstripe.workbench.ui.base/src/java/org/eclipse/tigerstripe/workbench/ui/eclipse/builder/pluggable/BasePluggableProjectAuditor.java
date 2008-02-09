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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder.pluggable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;

public abstract class BasePluggableProjectAuditor {

	private ITigerstripePluginProject pProject;
	private IProject project;
	protected IResource projectDescriptor;

	protected ITigerstripePluginProject getPProject() {
		return pProject;
	}

	protected IProject getProject() {
		return project;
	}

	public BasePluggableProjectAuditor(ITigerstripePluginProject pProject,
			IProject project) {
		this.project = project;
		this.pProject = pProject;

		this.projectDescriptor = project
				.findMember(ITigerstripeConstants.PLUGIN_DESCRIPTOR);
		if (this.projectDescriptor == null || !this.projectDescriptor.exists()) {
			this.projectDescriptor = project;
		}
	}
}
