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
package org.eclipse.tigerstripe.api.impl;

import java.util.Collection;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.external.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.core.model.ArtifactManager;

public class QueryAllArtifacts extends ArtifactQueryBase implements
		IQueryAllArtifacts {

	public QueryAllArtifacts() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection run(IArtifactManagerSession mgrSession) {
		ArtifactManagerSessionImpl impl = (ArtifactManagerSessionImpl) mgrSession;
		ArtifactManager mgr = impl.getArtifactManager();
		return mgr.getAllArtifacts(includeDependencies(), getProgressMonitor());
	}

}
