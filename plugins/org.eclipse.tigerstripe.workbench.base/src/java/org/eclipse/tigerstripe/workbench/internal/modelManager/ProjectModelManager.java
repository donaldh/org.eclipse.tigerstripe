/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.manager.ModelManager;
import org.eclipse.tigerstripe.repository.manager.ModelRepositoryFactory;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A model manager provides a single interface to manage a logical model,
 * composed of multiple model repositories.
 * 
 * @author erdillon
 * 
 */
public class ProjectModelManager extends ModelManager {

	private ITigerstripeModelProject project;
	private MultiFileArtifactRepository localRepository;

	public ProjectModelManager(ITigerstripeModelProject project) {
		this.project = project;
	}

	public ITigerstripeModelProject getProject() {
		return this.project;
	}

	public IModelRepository getDefaultRepository() throws ModelCoreException {
		if (localRepository == null) {
			IProject proj = (IProject) project.getAdapter(IProject.class);
			IPath path = proj.getFullPath().append("src");
			URI uri = URI.createPlatformResourceURI(path.toOSString(), true);
			localRepository = (MultiFileArtifactRepository) ModelRepositoryFactory.INSTANCE
					.createRepository(uri);
		}
		return localRepository;
	}

	public void store(IAbstractArtifact artifact, boolean forceReplace)
			throws ModelCoreException {
		getDefaultRepository().store(artifact, forceReplace);
	}

	public Collection<IAbstractArtifact> getAllArtifacts()
			throws ModelCoreException {
		return localRepository.getAllArtifacts();
	}
}