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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IStandardSpecifics;

public class StandardSpecifics implements IStandardSpecifics {

	private AbstractArtifact artifact;

	public StandardSpecifics(AbstractArtifact artifact) {
		setArtifact(artifact);
	}

	private void setArtifact(AbstractArtifact artifact) {
		this.artifact = artifact;
	}

	protected AbstractArtifact getArtifact() {
		return this.artifact;
	}

	public void build() {

	}
}
