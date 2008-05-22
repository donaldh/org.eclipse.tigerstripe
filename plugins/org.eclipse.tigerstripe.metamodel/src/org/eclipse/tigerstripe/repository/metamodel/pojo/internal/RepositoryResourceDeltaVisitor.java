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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;

/**
 * This is used to compute the delta on a repository based on the underlying
 * resource changes.
 * 
 * @author erdillon
 * 
 */
public class RepositoryResourceDeltaVisitor implements IResourceDeltaVisitor {

	private TigerstripeLocalPojoRepositoryDelta repositoryDelta;

	public RepositoryResourceDeltaVisitor(
			MultiFileArtifactRepository repository) {
		repositoryDelta = new TigerstripeLocalPojoRepositoryDelta(repository);
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource res = delta.getResource();
		if (res instanceof IFile) {
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				repositoryDelta.acceptAdded(res);
				break;
			case IResourceDelta.CHANGED:
				repositoryDelta.acceptChanged(res);
				break;
			case IResourceDelta.REMOVED:
				repositoryDelta.acceptDeleted(res);
			}
			return false;
		} else {
			return true;
		}
	}

	public TigerstripeLocalPojoRepositoryDelta getRepositoryDelta() {
		return this.repositoryDelta;
	}
}
