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

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.queries.IQuerySessionArtifacts;

public class QuerySessionArtifacts extends ArtifactQueryBase implements
		IQuerySessionArtifacts {

	public QuerySessionArtifacts() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection run(IArtifactManagerSession managerSession) {
		ArtifactManagerSessionImpl impl = (ArtifactManagerSessionImpl) managerSession;
		return impl.getArtifactManager().getArtifactsByModel(
				SessionFacadeArtifact.MODEL, includeDependencies(),
				getProgressMonitor());
	}

}
