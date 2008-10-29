/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ProjectDependencyChangeDelta implements IProjectDependencyDelta {

	private ITigerstripeModelProject project;
	private int kind = -1;
	private IPath path;

	public ProjectDependencyChangeDelta(ITigerstripeModelProject project,
			int kind, IPath path) {
		this.project = project;
		this.kind = kind;
		this.path = path;
	}

	public int getKind() {
		return kind;
	}

	public IPath getPath() {
		return path;
	}

	public ITigerstripeModelProject getProject() {
		return project;
	}

}