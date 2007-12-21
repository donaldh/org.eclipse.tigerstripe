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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;

public abstract class AbstractArtifactAuditor {

	private IAbstractArtifact artifact;

	private IProject project;

	private IJavaProject jProject;

	private ITigerstripeProject tsProject;

	public AbstractArtifactAuditor() {

	}

	public AbstractArtifactAuditor(IProject project, IAbstractArtifact artifact) {
		setDetails(project, artifact);
	}

	public void setDetails(IProject project, IAbstractArtifact artifact) {
		this.artifact = artifact;
		this.project = project;
		this.jProject = null;
		this.tsProject = null;
	}

	protected IAbstractArtifact getArtifact() {
		return this.artifact;
	}

	protected IProject getIProject() {
		return this.project;
	}

	protected IJavaProject getJProject() {
		if (jProject == null) {
			if (project instanceof IAdaptable) {
				jProject = (IJavaProject) ((IAdaptable) project)
						.getAdapter(IJavaProject.class);
			}
		}
		return jProject;
	}

	protected ITigerstripeProject getTSProject() {
		if (tsProject == null) {
			tsProject = (ITigerstripeProject) EclipsePlugin
					.getITigerstripeProjectFor(getIProject());
		}
		return tsProject;
	}

	/**
	 * Checks in the current active profile whether References are allowed or
	 * not
	 * 
	 * @return
	 */
	/**
	 * Check that all attributes are valid, without duplicates.
	 * 
	 * @param monitor
	 */
	private void checkAttributes(IProgressMonitor monitor) {
		// Empty for now. The Java Compiler does a lot of checking for us
		// already.
	}

	public void run(IProgressMonitor monitor) {

		checkAttributes(monitor);
		checkLabels(monitor);
		checkMethods(monitor);
		checkInterfacePackage();
		checkSuperArtifact();
		checkImplementedArtifacts();
	}

	private void checkImplementedArtifacts() {
		for (IAbstractArtifact art : getArtifact().getImplementedArtifacts()) {
			if (getTSProject() != null) {
				try {
					IAbstractArtifact s = getTSProject()
							.getArtifactManagerSession()
							.getArtifactByFullyQualifiedName(
									art.getFullyQualifiedName());
					if (!(s instanceof ISessionArtifact)) {
						TigerstripeProjectAuditor.reportError(
								"Implemented artifact '"
										+ art.getFullyQualifiedName()
										+ "' is not a valid ISessionArtifact.",
								TigerstripeProjectAuditor
										.getIResourceForArtifact(getIProject(),
												getArtifact()), 222);
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private void checkLabels(IProgressMonitor monitor) {
		// Let the compiler do its job...
		// Nothing to add for now.
	}

	private void checkMethods(IProgressMonitor monitor) {
		// Checking that abstract methods only belong to abstract artifacts
		for (IMethod method : getArtifact().getIMethods()) {
			if (method.isAbstract() && !getArtifact().isAbstract()) {
				TigerstripeProjectAuditor.reportError("Method "
						+ method.getName() + " is marked Abstract although "
						+ getArtifact().getFullyQualifiedName()
						+ " is not marked as Abstract.",
						TigerstripeProjectAuditor.getIResourceForArtifact(
								getIProject(), getArtifact()), 222);
			}
		}
	}

	protected IResource getIResource() {
		return TigerstripeProjectAuditor.getIResourceForArtifact(getIProject(),
				getArtifact());
	}

	protected void checkInterfacePackage() {

		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) API
				.getIWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS)
				&& prop
						.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_JVT_PLUGIN)) {
			// no need to check on the specifics if they don't appear
			if (getArtifact().getIStandardSpecifics() instanceof IOssjArtifactSpecifics) {
				IOssjArtifactSpecifics spec = (IOssjArtifactSpecifics) getArtifact()
						.getIStandardSpecifics();
				String pack = spec.getInterfaceProperties().getProperty(
						"package");
				if (pack == null || pack.length() == 0) {
					TigerstripeProjectAuditor.reportError(
							"Undefined target interface package for '"
									+ getArtifact().getFullyQualifiedName()
									+ "'.", TigerstripeProjectAuditor
									.getIResourceForArtifact(getIProject(),
											getArtifact()), 222);
				} else {
					IStatus status = JavaConventions.validatePackageName(pack);
					if (status.getSeverity() == IStatus.ERROR) {
						TigerstripeProjectAuditor.reportError(
								"Invalid target interface package for '"
										+ getArtifact().getFullyQualifiedName()
										+ "'.", TigerstripeProjectAuditor
										.getIResourceForArtifact(getIProject(),
												getArtifact()), 222);
					}
				}
			}
		}
	}

	/**
	 * Checks that the super artifact has the same artifact type. We don't worry
	 * about the super artifact existence as the Java Builder will take care of
	 * it.
	 * 
	 */
	protected void checkSuperArtifact() {
		IAbstractArtifact superArtifact = getArtifact().getExtendedIArtifact();

		if (superArtifact != null) {
			if (superArtifact.getClass() != getArtifact().getClass()) {
				TigerstripeProjectAuditor.reportError(
						"Invalid Type Hierarchy in '"
								+ getArtifact().getFullyQualifiedName()
								+ "'. Super-artifact should be of same type.",
						TigerstripeProjectAuditor.getIResourceForArtifact(
								getIProject(), getArtifact()), 222);
			}
		}
	}
}