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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ProfileDescriptorAuditor implements IFileExtensionBasedAuditor {

	/*
	 * private IProject project;
	 * 
	 * public ProfileDescriptorAuditor(IProject project) { this.project =
	 * project; }
	 */

	String extension = IWorkbenchProfile.FILE_EXTENSION;

	public String getFileExtension() {
		return extension;
	}

	public void run(IProject project, List<IResource> resources,
			IProgressMonitor monitor) {

		if (resources == null || resources.size() == 0)
			return;

		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project
				.getAdapter(ITigerstripeModelProject.class);
		if (tsProject != null) {
			monitor.beginTask("Checking profile description", resources.size());

			for (IResource res : resources) {
				try {
					IWorkbenchProfile workbenchProfile = TigerstripeCore
							.getWorkbenchProfileSession()
							.getWorkbenchProfileFor(
									res.getLocation().toOSString());
					checkPrimitiveTypes(workbenchProfile, res);
					checkStereotypes(workbenchProfile, res);
				} catch (Exception e) {
					BasePlugin.log(e);
				}
				monitor.worked(1);
			}
			monitor.done();
		} else {
			TigerstripeProjectAuditor.reportError(
					"Project '" + project.getName()
							+ "' contains invalid profile", project, 222);
		}
	}

	/*
	 * A method that checks the primitive types in a workbench profile, issuing
	 * errors/warnings via the Eclipse "Problems View" when problems are found.
	 * For now, it just checks the validity of the primitive type names, but
	 * this could grow to other tests on primitive types in the future
	 */
	private void checkPrimitiveTypes(IWorkbenchProfile workbenchProfile,
			IResource iresource) {
		Collection<IPrimitiveTypeDef> primitiveTypes = workbenchProfile
				.getPrimitiveTypeDefs(false);
		for (IPrimitiveTypeDef prim : primitiveTypes) {
			if (!prim.isValidName()) {
				TigerstripeProjectAuditor
						.reportError(
								"Invalid "
										+ ArtifactMetadataFactory.INSTANCE
												.getMetadata(
														IPrimitiveTypeImpl.class
																.getName())
												.getLabel(prim) + " name '"
										+ prim.getName() + "' detected",
								iresource, 222);
			} else if (!prim.isRecommendedName()) {
				TigerstripeProjectAuditor.reportWarning(
						"Type name '"
								+ prim.getName()
								+ "' is not recommended as the name of a "
								+ ArtifactMetadataFactory.INSTANCE.getMetadata(
										IPrimitiveTypeImpl.class.getName())
										.getLabel(prim), iresource, 222);
			} else {
				String expression = prim.getValidationExpression();
				if (expression != null && expression.trim().length() > 0) {
					try {
						Pattern.compile(expression);
					} catch (PatternSyntaxException e) {
						TigerstripeProjectAuditor
								.reportError(
										"Primitive type '"
												+ prim.getName()
												+ "' has invalid validation regular expression: "
												+ e.getMessage(), iresource,
										222);
					}
				}
			}
		}
	}

	private void checkStereotypes(IWorkbenchProfile workbenchProfile,
			IResource iResource) {

		ArrayList<String> duplicateStereotypeNameList = new ArrayList<String>();

		for (IStereotype stereotype : workbenchProfile.getStereotypes()) {

			if (checkValidStereotype(stereotype, workbenchProfile, iResource)) {

				if (duplicateStereotypeNameList.contains(stereotype.getName())) {
					TigerstripeProjectAuditor.reportError(
							"Duplicate Stereotype Name '"
									+ stereotype.getName() + "' in "
									+ iResource.getFullPath().toOSString(),
							iResource, 222);
				} else {
					duplicateStereotypeNameList.add(stereotype.getName());
				}
			}
		}
	}

	private static final char[] invalidStereotypeNameChars = { ' ', ';', '\'',
			':', ',', '<', '>', '"', '/', '?', '!', '@', '#', '$', '%', '^',
			'&', '*', '(', ')', '-', '+', '=', '|', '\\' };

	private boolean checkValidStereotype(IStereotype stereotype,
			IWorkbenchProfile profile, IResource iResource) {
		String name = stereotype.getName();
		if (!isValidName(name)) {
			TigerstripeProjectAuditor.reportError("Invalid Stereotype Name '"
					+ stereotype.getName() + "' in "
					+ iResource.getFullPath().toOSString(), iResource, 222);
		}

		for (IStereotypeAttribute attr : stereotype.getAttributes()) {
			if (!isValidName(attr.getName())) {
				TigerstripeProjectAuditor.reportError(
						"Invalid attribute name '" + attr.getName()
								+ "' in Stereotype '" + stereotype.getName()
								+ "' in "
								+ iResource.getFullPath().toOSString(),
						iResource, 222);
			}
		}

		return true;
	}

	private boolean isValidName(String name) {
		for (int i = 0; i < name.length(); i++) {
			for (char c : invalidStereotypeNameChars) {
				if (name.charAt(i) == c)
					return false;
			}
		}
		return true;
	}
}
