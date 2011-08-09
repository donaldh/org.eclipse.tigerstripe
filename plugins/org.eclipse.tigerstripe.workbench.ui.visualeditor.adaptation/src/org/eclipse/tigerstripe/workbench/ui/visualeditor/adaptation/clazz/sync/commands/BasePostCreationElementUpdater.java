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

import java.util.ArrayList;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;

public abstract class BasePostCreationElementUpdater {

	private final ITigerstripeModelProject tsProject;

	private final IAbstractArtifact iArtifact;

	private final Map map;

	public BasePostCreationElementUpdater(IAbstractArtifact iArtifact, Map map,
			ITigerstripeModelProject tsProject) {
		this.map = map;
		this.iArtifact = iArtifact;
		this.tsProject = tsProject;
	}

	protected IAbstractArtifact getIArtifact() {
		return this.iArtifact;
	}

	protected Map getMap() {
		return this.map;
	}

	protected ITigerstripeModelProject getDiagramProject() {
		return this.tsProject;
	}

	public abstract void updateConnections() throws TigerstripeException;

	protected void updateStereotype(NamedElement element) {
		ArrayList<String> strs = new ArrayList<String>();
		for (IStereotypeInstance instance : getIArtifact()
				.getStereotypeInstances()) {
			strs.add(instance.getName());
		}
		if (strs.size() > 0) {
			element.getStereotypes().addAll(strs);
		} else {
			element.resetStereotypes();
		}
	}
}
