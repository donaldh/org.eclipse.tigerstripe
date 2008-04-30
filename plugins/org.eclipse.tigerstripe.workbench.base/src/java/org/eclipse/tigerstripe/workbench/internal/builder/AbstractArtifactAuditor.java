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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.UnresolvedStereotypeInstance;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class AbstractArtifactAuditor {

	private IAbstractArtifact artifact;

	private IProject project;

	private IJavaProject jProject;

	private ITigerstripeModelProject tsProject;

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

	protected ITigerstripeModelProject getTSProject() {
		if (tsProject == null) {
			tsProject = (ITigerstripeModelProject) getIProject().getAdapter(
					ITigerstripeModelProject.class);
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
		for (IField attribute : getArtifact().getFields()) {
			checkStereotypes(attribute, "attribute '" + attribute.getName()
					+ "' of artifact '" + getArtifact().getName() + "'");
		}
	}

	public void run(IProgressMonitor monitor) {

		checkAttributes(monitor);
		checkLabels(monitor);
		checkMethods(monitor);
		// checkInterfacePackage();
		checkSuperArtifact();
		checkImplementedArtifacts();
		checkStereotypes(getArtifact(), "artifact '" + getArtifact().getName()
				+ "'");
	}

	private void checkStereotypes(IStereotypeCapable capable, String location) {
		for (IStereotypeInstance instance : capable.getStereotypeInstances()) {
			if (instance instanceof UnresolvedStereotypeInstance) {
				TigerstripeProjectAuditor.reportWarning("Stereotype '"
						+ instance.getName() + "' on " + location
						+ " not defined in the current profile",
						TigerstripeProjectAuditor.getIResourceForArtifact(
								getIProject(), getArtifact()), 222);
			}
		}
	}

	/**
	 * Need a separate method to get the return and argument stereos
	 * 
	 * @param capable
	 * @param location
	 */
	private void checkMethodStereotypes(IMethod method) {
		for (IStereotypeInstance instance : method
				.getReturnStereotypeInstances()) {
			if (instance instanceof UnresolvedStereotypeInstance) {
				String location = " return of method '" + method.getName()
						+ "' of artifact '" + getArtifact().getName() + "'";
				TigerstripeProjectAuditor.reportWarning("Stereotype "
						+ instance.getName() + " on " + location
						+ " not defined in the current profile",
						TigerstripeProjectAuditor.getIResourceForArtifact(
								getIProject(), getArtifact()), 222);
			}
		}
		for (IArgument argument : method.getArguments()) {
			for (IStereotypeInstance instance : argument
					.getStereotypeInstances()) {
				if (instance instanceof UnresolvedStereotypeInstance) {
					String location = " argument '" + argument.getName()
							+ "' of method '" + method.getName()
							+ "' of artifact '" + getArtifact().getName() + "'";
					TigerstripeProjectAuditor.reportWarning("Stereotype '"
							+ instance.getName() + "' on '" + location
							+ "' not defined in the current profile",
							TigerstripeProjectAuditor.getIResourceForArtifact(
									getIProject(), getArtifact()), 222);
				}
			}
		}
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
					BasePlugin.log(e);
				}
			}
		}
	}

	private void checkLabels(IProgressMonitor monitor) {
		for (ILiteral literal : getArtifact().getLiterals()) {
			checkStereotypes(literal, "literal '" + literal.getName()
					+ "' of artifact '" + getArtifact().getName() + "'");
		}
	}

	private void checkMethods(IProgressMonitor monitor) {
		// Checking that abstract methods only belong to abstract artifacts
		for (IMethod method : getArtifact().getMethods()) {
			if (method.isAbstract() && !getArtifact().isAbstract()) {
				TigerstripeProjectAuditor.reportError("Method "
						+ method.getName() + " is marked Abstract although "
						+ getArtifact().getFullyQualifiedName()
						+ " is not marked as Abstract.",
						TigerstripeProjectAuditor.getIResourceForArtifact(
								getIProject(), getArtifact()), 222);
			}
			checkStereotypes(method, "method '" + method.getName()
					+ "' of artifact '" + getArtifact().getName() + "'");
			// Need separate method to check the Return and Argument steros.
			// Possible change with new metamodel
			checkMethodStereotypes(method);
		}
	}

	protected IResource getIResource() {
		return TigerstripeProjectAuditor.getIResourceForArtifact(getIProject(),
				getArtifact());
	}

	/*
	 * protected void checkInterfacePackage() {
	 * 
	 * OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty)
	 * TigerstripeCore
	 * .getWorkbenchProfileSession().getActiveProfile().getProperty(
	 * IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
	 * 
	 * if (prop
	 * .getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS) &&
	 * prop .getPropertyValue(IOssjLegacySettigsProperty.ENABLE_JVT_PLUGIN)) { //
	 * no need to check on the specifics if they don't appear if
	 * (getArtifact().getIStandardSpecifics() instanceof IOssjArtifactSpecifics) {
	 * IOssjArtifactSpecifics spec = (IOssjArtifactSpecifics) getArtifact()
	 * .getIStandardSpecifics(); String pack =
	 * spec.getInterfaceProperties().getProperty( "package"); if (pack == null ||
	 * pack.length() == 0) { TigerstripeProjectAuditor.reportError( "Undefined
	 * target interface package for '" + getArtifact().getFullyQualifiedName() +
	 * "'.", TigerstripeProjectAuditor .getIResourceForArtifact(getIProject(),
	 * getArtifact()), 222); } else { IStatus status =
	 * JavaConventions.validatePackageName(pack); if (status.getSeverity() ==
	 * IStatus.ERROR) { TigerstripeProjectAuditor.reportError( "Invalid target
	 * interface package for '" + getArtifact().getFullyQualifiedName() + "'.",
	 * TigerstripeProjectAuditor .getIResourceForArtifact(getIProject(),
	 * getArtifact()), 222); } } } } }
	 */

	/**
	 * Checks that the super artifact has the same artifact type. We don't worry
	 * about the super artifact existence as the Java Builder will take care of
	 * it.
	 * 
	 */
	protected void checkSuperArtifact() {
		IAbstractArtifact superArtifact = getArtifact().getExtendedArtifact();

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