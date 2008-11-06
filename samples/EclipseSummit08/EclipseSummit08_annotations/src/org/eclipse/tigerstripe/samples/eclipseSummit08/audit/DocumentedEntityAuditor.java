/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.samples.eclipseSummit08.audit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation;
import org.eclipse.tigerstripe.workbench.internal.builder.IArtifactAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class DocumentedEntityAuditor implements IArtifactAuditor {

	static private String FAULT_PREFIX = "Documentation: ";

	public void reportError(String msg, IResource srcFile, int violation) {
		TigerstripeProjectAuditor.reportError(FAULT_PREFIX + msg, srcFile,
				violation);
	}

	private IAbstractArtifact artifact;
	private IProject project;

	public void run(IProgressMonitor monitor) {
		if (!(artifact instanceof IManagedEntityArtifact))
			return;

		Documentation doc = (Documentation) artifact
				.getAnnotation("Documentation");
		if (doc == null || doc.getContent().length() == 0) {
			reportError("Missing documentation on "
					+ artifact.getFullyQualifiedName(), getIResource(), 222);
		}
	}

	public void setDetails(IProject project, IAbstractArtifact artifact) {
		this.artifact = artifact;
		this.project = project;
	}

	protected IResource getIResource() {
		return TigerstripeProjectAuditor.getIResourceForArtifact(project,
				artifact);
	}

}
