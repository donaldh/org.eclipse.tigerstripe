/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.refactor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.refactor.ModelRefactorCommandFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A Model refactor request captures a request for a change in the model that
 * requires refactoring (ie. propagation of changes across multiple model
 * elements).
 * 
 * @author erdillon
 * 
 */
public class ModelRefactorRequest extends RefactorRequest {

	private ITigerstripeModelProject originalProject;
	private ITigerstripeModelProject destinationProject;
	private String originalFQN;
	private String destinationFQN;

	public void setOriginal(ITigerstripeModelProject originalProject,
			String originalFQN) {
		this.originalFQN = originalFQN;
		this.originalProject = originalProject;
	}

	public void setDestination(ITigerstripeModelProject destinationProject,
			String destinationFQN) {
		this.destinationFQN = destinationFQN;
		this.destinationProject = destinationProject;
	}

	public ITigerstripeModelProject getOriginalProject() {
		return this.originalProject;
	}

	public ITigerstripeModelProject getDestinationProject() {
		return this.destinationProject;
	}

	public String getOriginalFQN() {
		return this.originalFQN;
	}

	public String getDestinationFQN() {
		return this.destinationFQN;
	}

	/**
	 * Returns true if the refactor request is valid, ie. if the original
	 * artifact exists and if the destination doesn't
	 * 
	 * @return
	 */
	@Override
	public IStatus isValid() {
		try {
			if (destinationFQN == null || destinationFQN.length() == 0) {
				IStatus status = new Status(IStatus.ERROR,
						BasePlugin.getPluginId(), "destination can't be empty");
				return status;
			}

			// check original exists
			IAbstractArtifact orig = originalProject
					.getArtifactManagerSession()
					.getArtifactByFullyQualifiedName(originalFQN);
			if (orig == null) {
				IStatus status = new Status(IStatus.ERROR,
						BasePlugin.getPluginId(), originalFQN
								+ " doesn't exist.");
				return status;
			}

			// check destination doesn't exist
			if (!(orig instanceof IPackageArtifact)) {
				IAbstractArtifact dest = destinationProject
						.getArtifactManagerSession()
						.getArtifactByFullyQualifiedName(destinationFQN);
				if (dest != null) {
					IStatus status = new Status(IStatus.ERROR,
							BasePlugin.getPluginId(), destinationFQN
									+ " already exist.");
					return status;
				}

			}

			// checks that it is not a change of case
			if (originalFQN.toUpperCase().equals(destinationFQN.toUpperCase())
					&& originalProject.equals(destinationProject)) {
				IStatus status = new Status(IStatus.ERROR,
						BasePlugin.getPluginId(),
						"Refactor is case-insensitive");
				return status;
			}

			return Status.OK_STATUS;
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR,
					BasePlugin.getPluginId(), e.getMessage(), e);
			return status;
		}
	}

	public IAbstractArtifact getOriginalArtifact() throws TigerstripeException {
		return originalProject.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName(originalFQN);
	}

	@Override
	public IRefactorCommand getCommand(IProgressMonitor monitor)
			throws TigerstripeException {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		// The computation of the command for this refactor is delegated
		// to the ModelRefactorCommandFactory, which will visit the model
		// and trigger all ITigerstripeModelRefactorParticipant
		return ModelRefactorCommandFactory.INSTANCE.getCommand(this, monitor);
	}

	@Override
	public String toString() {
		return "ModelRefactorRequest: from" + originalFQN + " ("
				+ originalProject.getName() + ") to " + destinationFQN + " ("
				+ destinationProject.getName() + ").";
	}

	/**
	 * Returns true is request implies moving artifact from one project to
	 * another.
	 * 
	 * @return
	 */
	public boolean isCrossProjectCmd() {
		return !originalProject.equals(destinationProject);
	}

}
