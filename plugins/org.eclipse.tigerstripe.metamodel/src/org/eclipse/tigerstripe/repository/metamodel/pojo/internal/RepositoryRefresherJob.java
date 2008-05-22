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
package org.eclipse.tigerstripe.repository.metamodel.pojo.internal;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.repository.internal.Activator;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;

public class RepositoryRefresherJob extends Job {

	private MultiFileArtifactRepository repository;
	private TigerstripeLocalPojoRepositoryDelta delta;
	private AllOutOfSyncPojosVisitor visitor;

	public RepositoryRefresherJob(TigerstripeLocalPojoRepositoryDelta delta,
			String name) {
		super(name);
		this.delta = delta;
		this.visitor = null;
		repository = delta.getRepository();
	}

	public RepositoryRefresherJob(TigerstripeLocalPojoRepositoryDelta delta) {
		this(delta, "Repository Refresher");
	}

	public RepositoryRefresherJob(AllOutOfSyncPojosVisitor visitor, String name) {
		super(name);
		this.delta = null;
		this.visitor = visitor;
		repository = visitor.getRepository();
	}

	public RepositoryRefresherJob(AllOutOfSyncPojosVisitor visitor) {
		this(visitor, "Repository Refresher");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (delta != null)
			return runFromDelta(monitor);
		else
			return runFromVisitor(monitor);
	}

	protected IStatus runFromDelta(IProgressMonitor monitor) {
		throw new UnsupportedOperationException();
	}

	protected IStatus runFromVisitor(IProgressMonitor monitor) {

		IStatus statusAdded = handleAddedArtifacts(visitor.getNewPojos());
		IStatus statusChanged = handleChangedArtifacts(visitor
				.getPojosToRefresh());
		IStatus statusDeleted = handleDeletedArtifacts(visitor
				.getDeletedPojos());
		return new MultiStatus(Activator.PLUGIN_ID, 222, new IStatus[] {
				statusAdded, statusChanged, statusDeleted }, "", null);
	}

	private IStatus handleAddedArtifacts(List<IResource> addedArtifacts) {
		for (IResource artifact : addedArtifacts) {
			URI resURI = URI.createPlatformResourceURI(artifact.getFullPath()
					.toOSString(), true);
			repository.getResource(resURI, true);
		}
		return Status.OK_STATUS;
	}

	private IStatus handleChangedArtifacts(List<IResource> changedArtifacts) {
		for (IResource artifact : changedArtifacts) {
			URI resURI = URI.createPlatformResourceURI(artifact.getFullPath()
					.toOSString(), true);
			Resource res = repository.getURIResourceMap().get(resURI);
			if (res != null) {
				res.unload();
			}
		}
		return Status.OK_STATUS;
	}

	private IStatus handleDeletedArtifacts(List<URI> deletedArtifacts) {
		for (URI uri : deletedArtifacts) {
			Resource res = repository.getURIResourceMap().get(uri);
			if (res != null) {
				res.unload();
				repository.getResources().remove(res);
			}
		}
		return Status.OK_STATUS;
	}
}
