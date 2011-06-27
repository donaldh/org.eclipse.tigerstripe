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
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.UnresolvedStereotypeInstance;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public class CommonArtifactAuditor extends AbstractArtifactAuditor implements
		IArtifactAuditor {

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
		IAbstractArtifact artifact = getArtifact();
		String artifactName = "";
		if (artifact != null)
			artifactName = artifact.getFullyQualifiedName();

		if (artifactName == null)
			artifactName = "";
		else
			artifactName = artifactName + ".";

		for (IField attribute : getArtifact().getFields()) {
			checkStereotypes(attribute, "attribute '" + attribute.getName()
					+ "' of artifact '" + getArtifact().getName() + "'");
			checkDefaultValue(artifact, attribute);
			checkEnumField(attribute, artifactName);
		}
	}

	private void checkDefaultValue(IAbstractArtifact artifact, IField field) {
		IType type = field.getType();
		if (type.isPrimitive()
				|| type.getArtifact() instanceof IPrimitiveTypeArtifact) {
			String defaultValue = field.getDefaultValue();
			if (defaultValue != null && defaultValue.trim().length() > 0) {
				IWorkbenchProfile profile = TigerstripeCore
						.getWorkbenchProfileSession().getActiveProfile();
				for (IPrimitiveTypeDef primitiveTypeDef : profile
						.getPrimitiveTypeDefs(true)) {
					if (primitiveTypeDef.getName().equals(type.getName())) {
						validateDefaultValue(artifact, field, primitiveTypeDef);
						break;
					}
				}
			}
		}
	}

	private void validateDefaultValue(IAbstractArtifact artifact, IField field,
			IPrimitiveTypeDef primitiveTypeDef) {
		if (primitiveTypeDef.getValidationExpression() != null
				&& !field.getDefaultValue().matches(
						primitiveTypeDef.getValidationExpression())) {
			TigerstripeProjectAuditor
					.reportError(
							"Default value of '"
									+ artifact.getFullyQualifiedName()
									+ "."
									+ field.getName()
									+ "' attribute is incorrect. Default value should mutch following reqular expression: "
									+ primitiveTypeDef
											.getValidationExpression(),
							(IResource) getArtifact().getAdapter(
									IResource.class), 222);
		}
	}

	private void checkEnumField(IField field, String artifactName) {
		if (field.getType() != null) {
			IAbstractArtifact typeArtifact = field.getType().getArtifact();
			if (typeArtifact instanceof IEnumArtifact) {
				IEnumArtifact enumArtifact = (IEnumArtifact) typeArtifact;
				if (!isEnumFieldDefaultValueCorrect(field, enumArtifact)) {
					TigerstripeProjectAuditor
							.reportError(
									"Default value of '"
											+ artifactName
											+ field.getName()
											+ "' attribute is incorrect. Referenced enumeration '"
											+ enumArtifact
													.getFullyQualifiedName()
											+ "' doesn't contain '"
											+ field.getDefaultValue()
											+ "' literal.",
									(IResource) getArtifact().getAdapter(
											IResource.class), 222);
				}
			}
		}
	}

	private boolean isEnumFieldDefaultValueCorrect(IField field,
			IEnumArtifact enumArtifact) {
		if (field.getDefaultValue() == null) {
			return true;
		}
		for (ILiteral literal : enumArtifact.getLiterals()) {
			if (field.getDefaultValue().equals(literal.getName())) {
				return true;
			}
		}
		return false;
	}

	public void run(IProgressMonitor monitor) {

		// System.out.println("Auditing: " + getArtifact());

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
				TigerstripeProjectAuditor.reportError(
						"Method " + method.getName()
								+ " is marked Abstract although "
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

	/**
	 * Checks that the super artifact has the same artifact type. We don't worry
	 * about the super artifact existence as the Java Builder will take care of
	 * it.
	 * 
	 */
	private void checkSuperArtifact() {
		IAbstractArtifact artifact = getArtifact();
		IAbstractArtifact superArtifact = artifact.getExtendedArtifact();

		if (superArtifact != null
				&& !((AbstractArtifact) superArtifact).isProxy()) {

			boolean eligableHierarhy = superArtifact.getClass() == artifact
					.getClass();
			if (artifact instanceof AssociationClassArtifact) {
				eligableHierarhy |= superArtifact instanceof IManagedEntityArtifact;
			}

			if (!eligableHierarhy) {
				TigerstripeProjectAuditor.reportError(
						"Invalid Type Hierarchy in '"
								+ artifact.getFullyQualifiedName()
								+ "'. Super-artifact should be of same type.",
						TigerstripeProjectAuditor.getIResourceForArtifact(
								getIProject(), artifact), 222);
			}
		}
	}
}