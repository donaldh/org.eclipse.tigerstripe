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
package org.eclipse.tigerstripe.workbench.internal.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public interface IArtifactAuditor {

	public static final IArtifactAuditor EMPTY = new IArtifactAuditor() {

		public void setDetails(IProject project, IAbstractArtifact artifact) {
		}

		public void run(IProgressMonitor monitor) {
		}
	};
	
	public void setDetails(IProject project, IAbstractArtifact artifact);

	public void run(IProgressMonitor monitor);
}
