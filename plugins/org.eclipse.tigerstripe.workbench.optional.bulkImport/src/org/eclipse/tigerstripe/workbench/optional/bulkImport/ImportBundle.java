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
package org.eclipse.tigerstripe.workbench.optional.bulkImport;

import java.util.Map;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;

public class ImportBundle {

	private Map<String, IAbstractArtifact> extractedArtifacts;
	private IArtifactManagerSession mgrSession;

	public ImportBundle() {

	}

	public Map<String, IAbstractArtifact> getExtractedArtifacts() {
		return extractedArtifacts;
	}

	public void setExtractedArtifacts(
			Map<String, IAbstractArtifact> extractedArtifacts) {
		this.extractedArtifacts = extractedArtifacts;
	}

	public IArtifactManagerSession getMgrSession() {
		return mgrSession;
	}

	public void setMgrSession(IArtifactManagerSession mgrSession) {
		this.mgrSession = mgrSession;
	}

}
