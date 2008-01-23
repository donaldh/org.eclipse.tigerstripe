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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;

public class AssociationArtifactAuditor extends AbstractArtifactAuditor
		implements IArtifactAuditor {

	public AssociationArtifactAuditor(IProject project,
			IAbstractArtifact artifact) {
		super(project, artifact);
	}

	@Override
	public void run(IProgressMonitor monitor) {
		super.run(monitor);

		IAssociationArtifact artifact = (IAssociationArtifact) getArtifact();

		IType aEndType = artifact.getAEnd().getIType();
		boolean aEndDefined = false;
		if (aEndType == null || aEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError(
					"Undefined Association end (aEnd) in '"
							+ artifact.getName() + "'.", getIResource(), 222);
		} else
			aEndDefined = true;

		IType zEndType = artifact.getZEnd().getIType();
		boolean zEndDefined = false;
		if (zEndType == null || zEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError(
					"Undefined Association end (zEnd) in '"
							+ artifact.getName() + "'.", getIResource(), 222);
		} else
			zEndDefined = true;

		if (aEndDefined && zEndDefined) {
			checkForOutboundRelationship(); // Bug 925
		}
	}

	/**
	 * Check that this association is not creating an outbound relationship from
	 * a referenced project/module
	 * 
	 */
	protected void checkForOutboundRelationship() {
		// We need to check on the ends on the association, in particular focus
		// on which projects they live in
		IAssociationArtifact artifact = (IAssociationArtifact) getArtifact();

		IType aEndType = artifact.getAEnd().getIType();
		boolean aEndNavigable = artifact.getAEnd().isNavigable();
		IType zEndType = artifact.getZEnd().getIType();
		boolean zEndNavigable = artifact.getZEnd().isNavigable();
		try {
			IAbstractArtifact aEndArt = getTSProject()
					.getArtifactManagerSession()
					.getArtifactByFullyQualifiedName(
							aEndType.getFullyQualifiedName());
			IAbstractArtifact zEndArt = getTSProject()
					.getArtifactManagerSession()
					.getArtifactByFullyQualifiedName(
							zEndType.getFullyQualifiedName());
			if (aEndArt != null && zEndArt != null) {
				ITigerstripeProject aEndProject = (ITigerstripeProject) aEndArt
						.getITigerstripeProject();
				ITigerstripeProject zEndProject = (ITigerstripeProject) zEndArt
						.getITigerstripeProject();

				ITigerstripeProject localProject = getTSProject();

				boolean illegalAssoc = false;
				if (aEndProject == null && zEndProject == null) {
					// both live in dependencies. Can't do
					illegalAssoc = true;
				} else if (aEndProject == null) {
					if (zEndNavigable)
						illegalAssoc = true;
				} else if (zEndProject == null) {
					if (aEndNavigable)
						illegalAssoc = true;
				} else if (aEndProject.equals(zEndProject)) {
					// any direction is allowed in this case
				} else if ((aEndProject.equals(localProject) && aEndNavigable)
						|| (zEndProject.equals(localProject) && zEndNavigable)) {
					illegalAssoc = true;
				}

				if (illegalAssoc) {
					TigerstripeProjectAuditor
							.reportError(
									"Illegal Association navigability across projects: '"
											+ artifact.getFullyQualifiedName()
											+ "'. (Can't navigate from referenced project/Dependency)",
									getIResource(), 222);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
}
