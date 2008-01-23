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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;

public class PostCreationDependencyArtifactUpdater extends
		BasePostCreationElementUpdater {

	protected Dependency dependency;

	public PostCreationDependencyArtifactUpdater(IAbstractArtifact iArtifact,
			Dependency dependency, Map map, ITigerstripeProject diagramProject) {
		super(iArtifact, map, diagramProject);
		this.dependency = dependency;
	}

	@Override
	public void updateConnections() throws TigerstripeException {

		try {
			BaseETAdapter.setIgnoreNotify(true);
			dependency.setIsReadonly(getIArtifact().isReadonly());
			updateStereotype(dependency);
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

}
