/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.refactor.diagrams;

import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.HeadlessDiagramHandle;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.IDiagramChangeDelta;

public class DiagramChangeDelta implements IDiagramChangeDelta {

	private HeadlessDiagramHandle handle = null;
	private IPath newPath = null;
	private int type = UNKNOWN;

	public DiagramChangeDelta(int type) {
		this.type = type;
	}

	public void setAffectedDiagramHandle(HeadlessDiagramHandle handle) {
		this.handle = handle;
	}

	public void setNewPath(IPath path) {
		this.newPath = path;
	}

	public int getType() {
		return type;
	}

	public HeadlessDiagramHandle getAffDiagramHandle() {
		return this.handle;
	}

	public IPath getNewPath() {
		return this.newPath;
	}

}
