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
import org.eclipse.tigerstripe.api.external.queries.IQueryModelArtifacts;

public class QueryModelArtifacts extends ArtifactQueryBase implements
		IQueryModelArtifacts {

	public QueryModelArtifacts() {
		super();
	}

	@Override
	public Collection run(IArtifactManagerSession managerSession) {
		ArtifactManagerSessionImpl impl = (ArtifactManagerSessionImpl) managerSession;

		return impl.getArtifactManager().getModelArtifacts(
				includeDependencies(), getProgressMonitor());
	}

}
