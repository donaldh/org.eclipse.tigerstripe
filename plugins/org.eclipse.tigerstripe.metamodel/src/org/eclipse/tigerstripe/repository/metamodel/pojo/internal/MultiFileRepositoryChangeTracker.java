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

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;

/**
 * A tacker is attached to each LocalRepository in order to trigger updates to
 * the repository as underlying resources used for storage of repository are
 * changed.
 * 
 * @author erdillon
 * 
 */
public class MultiFileRepositoryChangeTracker implements
		IResourceChangeListener {

	private MultiFileArtifactRepository repository;

	public MultiFileRepositoryChangeTracker(
			MultiFileArtifactRepository repository) {
		this.repository = repository;
	}

	public void startListening() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public void stopListening() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	public void resourceChanged(IResourceChangeEvent event) {
		throw new UnsupportedOperationException();
	}

	protected MultiFileArtifactRepository getRepository() {
		return this.repository;
	}

	protected TigerstripeLocalPojoRepositoryDelta computeRepositoryDelta(
			IResourceChangeEvent event) throws CoreException {
		RepositoryResourceDeltaVisitor visitor = new RepositoryResourceDeltaVisitor(
				repository);
		IResourceDelta delta = event.getDelta();
		delta.accept(visitor);

		return visitor.getRepositoryDelta();
	}
}
