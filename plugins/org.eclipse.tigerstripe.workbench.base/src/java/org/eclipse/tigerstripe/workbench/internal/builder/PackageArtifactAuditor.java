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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;

public class PackageArtifactAuditor extends AbstractArtifactAuditor {

	public void run(IProgressMonitor monitor) {
		// TODO Check that the associated .package file actually exists
		IPackageArtifact packageArtifact = (IPackageArtifact) getArtifact();

		IResource myResource = getIResource();
		if (myResource == null) {
			TigerstripeProjectAuditor.reportWarning(
					"No .package file for package "
							+ packageArtifact.getFullyQualifiedName(),
					getIProject(), 222);
		}

	}

}
