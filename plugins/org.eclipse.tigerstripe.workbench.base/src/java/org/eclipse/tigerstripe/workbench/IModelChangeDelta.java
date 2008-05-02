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
package org.eclipse.tigerstripe.workbench;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IModelChangeDelta {

	public class RenamedArtifact {
		public IAbstractArtifact artifact;
		public String oldFQN;
	}

	/**
	 * Returns all the affected projects affected by the model change
	 * 
	 * Note that if a model change occurs in a project referenced by other
	 * Tigerstripe Model project, all referencing projects will be included
	 * here.
	 * 
	 * @return
	 */
	public ITigerstripeModelProject[] getAffectedProjects()
			throws TigerstripeException;

	/**
	 * Returns the project where this delta originated.
	 * 
	 * @return
	 */
	public ITigerstripeModelProject getProject();

	public IAbstractArtifact[] getAddedArtifacts();

	public IAbstractArtifact[] getDeletedArtifacts();

	public IAbstractArtifact[] getChangedArtifacts();

	public RenamedArtifact[] getRenamedArtifacts();
}
