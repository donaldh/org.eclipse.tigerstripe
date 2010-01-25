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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.tigerstripe.workbench.ui.internal.runtime.messages.NewWizardMessages;

@SuppressWarnings("restriction")
public class ArtifactNameValidator {

	@SuppressWarnings("deprecation")
	public static IStatus validateArtifactName(String artifactName) {

		StatusInfo status = new StatusInfo();

		IStatus validatedNameStatus = validateNameString(artifactName);
		if(!validatedNameStatus.isOK()) {
			return validatedNameStatus;
		}

		if (artifactName.indexOf('.') != -1) {
			status.setError(NewWizardMessages.getString("NewArtifactWizardPage.error.QualifiedName")); //$NON-NLS-1$
			return status;
		}

		IStatus validatedStatus = validateConventionsStatus(JavaConventions.validateJavaTypeName(artifactName));
		if(!validatedStatus.isOK()) {
			return validatedStatus;
		}

		return status;

	}

	@SuppressWarnings("deprecation")
	public static IStatus validatePackageArtifactName(String artifactName) {

		StatusInfo status = new StatusInfo();

		IStatus validatedNameStatus = validateNameString(artifactName);
		if(!validatedNameStatus.isOK()) {
			return validatedNameStatus;
		}

		IStatus validatedConventionStatus = validateConventionsStatus(JavaConventions.validatePackageName(artifactName));
		if(!validatedConventionStatus.isOK()) {
			return validatedConventionStatus;
		}

		return status;
	}

	public static IStatus validateArtifactDoesNotExist(IPackageFragment artifactPackage, String artifactName) {

		StatusInfo status = new StatusInfo();

		if (artifactPackage != null) {
			ICompilationUnit cu = artifactPackage.getCompilationUnit(artifactName + ".java"); //$NON-NLS-1$
			
			IResource resource = cu.getResource();
			if (resource.exists()) {
				status.setError(org.eclipse.jdt.internal.ui.wizards.NewWizardMessages.NewTypeWizardPage_error_TypeNameExists);
				return status;
			}
			IPath location = resource.getLocation();
			if (location != null && location.toFile().exists()) {
				status.setError(org.eclipse.jdt.internal.ui.wizards.NewWizardMessages.NewTypeWizardPage_error_TypeNameExistsDifferentCase);
				return status;
			}
		} else {
			
			status.setError(NewWizardMessages.getString("NewArtifactWizardPage.error.EnterArtifactName")); //$NON-NLS-1$
			return status;
		}

		return status;
	}

	public static IStatus validatePackageArtifactDoesNotExist(IJavaProject project, String artifactPackageName) {

		StatusInfo status = new StatusInfo();

		if (artifactPackageName != null && project != null) {
			
			IPath path = project.getPath().append("src" + '/' + artifactPackageName.replaceAll("\\.", "/"));
			try {
				
				if(project.findPackageFragment(path) != null) {
					status.setError(org.eclipse.jdt.internal.ui.wizards.NewWizardMessages.NewTypeWizardPage_error_TypeNameExists);
					return status;
				}
			} catch (JavaModelException e) {
				status.setError(e.getMessage());
				return status;
			}
			
		} else {
			status.setError(NewWizardMessages.getString("NewArtifactWizardPage.error.EnterArtifactName")); //$NON-NLS-1$
			return status;
		}
		return status;
	}
	
	
	private static IStatus validateNameString(String artifactName) {
		
		StatusInfo statusInfo = new StatusInfo();
		
		if (artifactName == null || artifactName.length() == 0) {
			statusInfo.setError(NewWizardMessages.getString("NewArtifactWizardPage.error.EnterArtifactName")); //$NON-NLS-1$
			return statusInfo;
		}
		
		return statusInfo;
	}
	
	private static IStatus validateConventionsStatus(IStatus status) {
		
		StatusInfo statusInfo = new StatusInfo();
		
		if (status.getSeverity() == IStatus.ERROR) {
			statusInfo.setError(NewWizardMessages.getFormattedString("NewArtifactWizardPage.error.InvalidArtifactName", status.getMessage())); //$NON-NLS-1$
		} else if (status.getSeverity() == IStatus.WARNING) {
			statusInfo.setWarning(NewWizardMessages.getFormattedString("NewArtifactWizardPage.warning.TypeNameDiscouraged", status.getMessage())); //$NON-NLS-1$
		}
		
		return statusInfo;
	}


}
