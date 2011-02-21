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
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class AbstractArtifactAuditor implements IArtifactAuditor {

	private IAbstractArtifact artifact;
	private IProject project;
	private IJavaProject jProject;
	private ITigerstripeModelProject tsProject;

	public void setDetails(IProject project, IAbstractArtifact artifact) {
		this.artifact = artifact;
		this.project = project;
		this.jProject = (IJavaProject) project.getAdapter(IJavaProject.class);
		this.tsProject = (ITigerstripeModelProject) getIProject().getAdapter(
				ITigerstripeModelProject.class);
	}

	protected IAbstractArtifact getArtifact() {
		return this.artifact;
	}

	protected IProject getIProject() {
		return this.project;
	}

	protected IJavaProject getJProject() {
		return jProject;
	}

	protected ITigerstripeModelProject getTSProject() {
		return tsProject;
	}

	protected IResource getIResource() {
		return TigerstripeProjectAuditor.getIResourceForArtifact(getIProject(),
				getArtifact());
	}

}