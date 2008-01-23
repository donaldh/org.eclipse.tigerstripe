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

import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.queries.IArtifactQuery;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public abstract class ArtifactQueryBase implements IArtifactQuery {

	private ITigerstripeProgressMonitor monitor = new TigerstripeNullProgressMonitor();

	private boolean includeDependencies = true;

	public boolean includeDependencies() {
		return includeDependencies;
	}

	public void setIncludeDependencies(boolean includeDependencies) {
		this.includeDependencies = includeDependencies;
	}

	public void setProgressMonitor(ITigerstripeProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public ITigerstripeProgressMonitor getProgressMonitor() {
		return this.monitor;
	}

	public ArtifactQueryBase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public abstract Collection run(IArtifactManagerSession managerSession);

}
