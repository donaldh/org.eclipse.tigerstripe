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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;

public abstract class BasePostCreationElementUpdater {

	private ITigerstripeModelProject tsProject;

	private IAbstractArtifact iArtifact;

	private InstanceMap map;

	public BasePostCreationElementUpdater(IAbstractArtifact iArtifact,
			InstanceMap map, ITigerstripeModelProject tsProject) {
		this.map = map;
		this.iArtifact = iArtifact;
		this.tsProject = tsProject;
	}

	protected IAbstractArtifact getIArtifact() {
		return this.iArtifact;
	}

	protected InstanceMap getMap() {
		return this.map;
	}

	protected ITigerstripeModelProject getDiagramProject() {
		return this.tsProject;
	}

	public abstract void updateConnections() throws TigerstripeException;

}
