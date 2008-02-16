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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.IModelManager;
import org.eclipse.tigerstripe.workbench.model.IModelRepository;
import org.eclipse.tigerstripe.workbench.model.IModuleRepository;
import org.eclipse.tigerstripe.workbench.model.IReferencedProjectRepository;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A model manager provides a single interface to manage a logical model,
 * composed of multiple model repositories.
 * 
 * @author erdillon
 * 
 */
public class ModelManager implements IModelManager {

	private ITigerstripeModelProject project;
	private Map<URI, IModelRepository> repositories = new HashMap<URI, IModelRepository>();

	private Map<String, IAbstractArtifact> artifactMap = new HashMap<String, IAbstractArtifact>();

	public ModelManager(ITigerstripeModelProject project) {
		this.project = project;
		try {
			load(null);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	// ===================================

	@Override
	public IModelRepository getDefaultRepository() {
		// FIXME: simply returning the first repository for now.
		return repositories.values().iterator().next();
	}

	protected void load(IProgressMonitor monitor) throws TigerstripeException {

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		URI[] uris = new URI[] { URI.createURI("platform:/resource/"
				+ project.getProjectLabel() + "/src/") };
		monitor.beginTask("Loading repositories", uris.length);
		for (URI uri : uris) {
			try {
				IModelRepository repository = ModelRepositoryFactory.INSTANCE
						.createRepository(uri, this);
				repositories.put(uri, repository);
				monitor.worked(1);
			} catch (TigerstripeException e) {

			}
		}
		monitor.done();
	}

	@Override
	public IAbstractArtifact[] findAllArtifacts(String fullyQualifiedName)
			throws TigerstripeException {

		((ModelRepository) getDefaultRepository()).getResourceSet()
				.getAllContents();
		return null;
	}

	@Override
	public IAbstractArtifact[] getAllArtifacts(boolean includeDependencies)
			throws TigerstripeException {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		for (IModelRepository repository : repositories.values()) {
			if (RepositoryUtils.isLocal(repository))
				result.addAll(repository.getAllArtifacts());
			else if (includeDependencies)
				result.addAll(repository.getAllArtifacts());
		}
		return result.toArray(new IAbstractArtifact[result.size()]);
	}

	@Override
	public void setRepositories(IModelRepository[] repositories)
			throws TigerstripeException {

	}

	@Override
	public List<IModelRepository> getRepositories() {

		return null;
	}

}