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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * 
 * @author erdillon
 * @deprecated
 */
public class ModelChangeDelta implements IModelChangeDelta {

	private ITigerstripeModelProject project;
	private IAbstractArtifact addedArtifact;
	private IAbstractArtifact deletedArtifact;
	private RenamedArtifact renamedArtifact;
	private IAbstractArtifact changedArtifact;

	public ModelChangeDelta(ITigerstripeModelProject project,
			IAbstractArtifact addedArtifact, IAbstractArtifact deletedArtifact,
			RenamedArtifact renamedArtifact, IAbstractArtifact changedArtifact) {
		this.project = project;
		this.addedArtifact = addedArtifact;
		this.deletedArtifact = deletedArtifact;
		this.renamedArtifact = renamedArtifact;
		this.changedArtifact = changedArtifact;
	}

	public IAbstractArtifact[] getAddedArtifacts() {
		if (addedArtifact != null)
			return new IAbstractArtifact[] { addedArtifact };
		return IAbstractArtifact.EMPTY_ARRAY;
	}

	public ITigerstripeModelProject[] getAffectedProjects() throws TigerstripeException {
		List<ITigerstripeModelProject> projects = new ArrayList<ITigerstripeModelProject>();
		
		projects.add(getProject());
		for( ITigerstripeModelProject other : getProject().getReferencedProjects()) {
			projects.add(other);
		}
		
		return projects.toArray(new ITigerstripeModelProject[projects.size()]);
	}

	public IAbstractArtifact[] getChangedArtifacts() {
		if (changedArtifact != null)
			return new IAbstractArtifact[] { changedArtifact };
		return IAbstractArtifact.EMPTY_ARRAY;
	}

	public IAbstractArtifact[] getDeletedArtifacts() {
		if (deletedArtifact != null)
			return new IAbstractArtifact[] { deletedArtifact };
		return IAbstractArtifact.EMPTY_ARRAY;
	}

	public ITigerstripeModelProject getProject() {
		return this.project;
	}

	public RenamedArtifact[] getRenamedArtifacts() {
		if (renamedArtifact != null)
			return new RenamedArtifact[] { renamedArtifact };
		return new RenamedArtifact[0];
	}

}
