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
import org.eclipse.tigerstripe.api.external.queries.IQueryCapabilitiesArtifacts;
import org.eclipse.tigerstripe.core.model.ArtifactManager;

public class QueryCapabilitiesArtifacts extends ArtifactQueryBase implements
		IQueryCapabilitiesArtifacts {

	public QueryCapabilitiesArtifacts() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection run(IArtifactManagerSession managerSession) {
		ArtifactManagerSessionImpl impl = (ArtifactManagerSessionImpl) managerSession;
		ArtifactManager mgr = impl.getArtifactManager();

		return mgr.getCapabilitiesArtifacts(includeDependencies(),
				getProgressMonitor());
	}

}
