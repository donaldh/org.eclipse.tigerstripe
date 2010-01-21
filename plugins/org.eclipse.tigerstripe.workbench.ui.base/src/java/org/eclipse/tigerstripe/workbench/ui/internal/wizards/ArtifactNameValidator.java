/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    J. Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.tigerstripe.workbench.ui.internal.runtime.messages.NewWizardMessages;

@SuppressWarnings("restriction")
public class ArtifactNameValidator {

	@SuppressWarnings("deprecation")
	public static IStatus validateArtifactName(String artifactName) {

		StatusInfo status = new StatusInfo();

		if (artifactName == null || artifactName.length() == 0) {
			status
					.setError(NewWizardMessages
							.getString("NewArtifactWizardPage.error.EnterArtifactName")); //$NON-NLS-1$
			return status;
		}

		if (artifactName.indexOf('.') != -1) {
			status.setError(NewWizardMessages
					.getString("NewArtifactWizardPage.error.QualifiedName")); //$NON-NLS-1$
			return status;
		}

		IStatus val = JavaConventions.validateJavaTypeName(artifactName);
		if (val.getSeverity() == IStatus.ERROR) {
			status
					.setError(NewWizardMessages
							.getFormattedString(
									"NewArtifactWizardPage.error.InvalidArtifactName", val.getMessage())); //$NON-NLS-1$
			return status;
		} else if (val.getSeverity() == IStatus.WARNING) {
			status
					.setWarning(NewWizardMessages
							.getFormattedString(
									"NewArtifactWizardPage.warning.TypeNameDiscouraged", val.getMessage())); //$NON-NLS-1$
		}

		return status;

	}

	public static IStatus validateArtifactDoesNotExist(
			IPackageFragment artifactPackage, String artifactName) {

		StatusInfo status = new StatusInfo();

		if (artifactPackage != null) {
			ICompilationUnit cu = artifactPackage
					.getCompilationUnit(artifactName + ".java"); //$NON-NLS-1$
			IResource resource = cu.getResource();

			if (resource.exists()) {
				status
						.setError(org.eclipse.jdt.internal.ui.wizards.NewWizardMessages.NewTypeWizardPage_error_TypeNameExists);
				return status;
			}
			IPath location = resource.getLocation();
			if (location != null && location.toFile().exists()) {
				status
						.setError(org.eclipse.jdt.internal.ui.wizards.NewWizardMessages.NewTypeWizardPage_error_TypeNameExistsDifferentCase);
				return status;
			}
		}

		return status;
	}
}
