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
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.UnresolvedStereotypeInstance;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

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

		IType aEndType = artifact.getAEnd().getType();
		boolean aEndDefined = false;
		if (aEndType == null || aEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError("Undefined "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName())
							.getLabel() + " end (aEnd) in '"
					+ artifact.getName() + "'.", getIResource(), 222);
		} else
			aEndDefined = true;

		IType zEndType = artifact.getZEnd().getType();
		boolean zEndDefined = false;
		if (zEndType == null || zEndType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError("Undefined "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName())
							.getLabel() + " end (zEnd) in '"
					+ artifact.getName() + "'.", getIResource(), 222);
		} else
			zEndDefined = true;

		if (aEndDefined && zEndDefined) {
			checkForOutboundRelationship(); // Bug 925
			checkStereotypes(artifact.getAEnd(), "artifact '"+getArtifact().getName()+"' endA");
			checkStereotypes(artifact.getZEnd(), "artifact '"+getArtifact().getName()+"' endZ");
			
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

		IType aEndType = artifact.getAEnd().getType();
		boolean aEndNavigable = artifact.getAEnd().isNavigable();
		IType zEndType = artifact.getZEnd().getType();
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
				ITigerstripeModelProject aEndProject = (ITigerstripeModelProject) aEndArt
						.getTigerstripeProject();
				ITigerstripeModelProject zEndProject = (ITigerstripeModelProject) zEndArt
						.getTigerstripeProject();

				ITigerstripeModelProject localProject = getTSProject();

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
									"Illegal "
											+ ArtifactMetadataFactory.INSTANCE
													.getMetadata(
															IAssociationArtifactImpl.class
																	.getName())
													.getLabel()
											+ " navigability across projects: '"
											+ artifact.getFullyQualifiedName()
											+ "'. (Can't navigate from referenced project/Dependency)",
									getIResource(), 222);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
	
	
	private void checkStereotypes(IStereotypeCapable capable, String location){
		for (IStereotypeInstance instance : capable.getStereotypeInstances()){
			if (instance instanceof UnresolvedStereotypeInstance){
				TigerstripeProjectAuditor.reportWarning(
						"Stereotype '"+instance.getName()+"' on "+location+" not defined in the current profile",
						TigerstripeProjectAuditor
								.getIResourceForArtifact(getIProject(),
										getArtifact()), 222);
			}
		}
	}
}
