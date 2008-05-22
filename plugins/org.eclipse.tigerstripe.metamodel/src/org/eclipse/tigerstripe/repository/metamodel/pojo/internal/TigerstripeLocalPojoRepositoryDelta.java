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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;
import org.eclipse.tigerstripe.repository.metamodel.pojo.PojoResourceImpl;

/**
 * This gathers the delta on a Repository based on the underlying IResources
 * 
 * @author erdillon
 * 
 */
public class TigerstripeLocalPojoRepositoryDelta {

	private MultiFileArtifactRepository repository;

	private List<IResource> addedArtifacts = new ArrayList<IResource>();
	private List<IResource> changedArtifacts = new ArrayList<IResource>();
	private List<IResource> deletedArtifacts = new ArrayList<IResource>();

	public TigerstripeLocalPojoRepositoryDelta(
			MultiFileArtifactRepository repository) {
		this.repository = repository;
	}

	public boolean isArtifact(IResource resource) {
		return repository.isArtifact(resource);
	}

	public void acceptAdded(IResource resource) {
		if (isArtifact(resource)) {
			// we ignore resource that are already in sync in the repository, as
			// it means we're getting notified by the very store that was
			// performed by the repository
			URI uri = URI.createPlatformResourceURI(resource.getFullPath()
					.toOSString(), true);
			PojoResourceImpl res = (PojoResourceImpl) repository
					.getURIResourceMap().get(uri);
			if (res == null
					|| res.getStamp() != resource.getModificationStamp())
				addedArtifacts.add(resource);
		}
	}

	public void acceptChanged(IResource resource) {
		if (isArtifact(resource)) {
			// we ignore resource that are already in sync in the repository, as
			// it means we're getting notified by the very store that was
			// performed by the repository
			URI uri = URI.createPlatformResourceURI(resource.getFullPath()
					.toOSString(), true);
			PojoResourceImpl res = (PojoResourceImpl) repository
					.getURIResourceMap().get(uri);
			if (res == null
					|| res.getStamp() != resource.getModificationStamp())
				changedArtifacts.add(resource);
		}
	}

	public void acceptDeleted(IResource resource) {
		if (isArtifact(resource))
			deletedArtifacts.add(resource);
	}

	public List<IResource> getAddedArtifacts() {
		return addedArtifacts;
	}

	public List<IResource> getDeletedArtifacts() {
		return deletedArtifacts;
	}

	public List<IResource> getChangedArtifacts() {
		return changedArtifacts;
	}

	public boolean isEmpty() {
		return addedArtifacts.isEmpty() && deletedArtifacts.isEmpty()
				&& changedArtifacts.isEmpty();
	}

	public MultiFileArtifactRepository getRepository() {
		return this.repository;
	}
}
