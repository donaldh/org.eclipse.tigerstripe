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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.util.Collection;

import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

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

		ExecutionContext context = getExecutionContext();

		if (context == null) {
			return mgr.getAllArtifacts(includeDependencies(),
					getProgressMonitor());
		} else {
			return mgr.getAllArtifacts(includeDependencies(), context);
		}
	}

}
