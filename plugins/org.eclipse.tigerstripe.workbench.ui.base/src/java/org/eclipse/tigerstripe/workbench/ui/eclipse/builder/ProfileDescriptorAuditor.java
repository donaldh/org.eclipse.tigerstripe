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

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.api.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;

public class ProfileDescriptorAuditor {

	private IProgressMonitor monitor = new NullProgressMonitor();

	private IProject project;

	public ProfileDescriptorAuditor(IProject project) {
		this.project = project;
	}

	public void run(IResource[] resources, IProgressMonitor monitor) {

		if (resources == null || resources.length == 0)
			return;

		this.monitor = monitor;
		ITigerstripeProject tsProject = (ITigerstripeProject) TSExplorerUtils
				.getProjectHandleFor(project);
		if (tsProject != null) {
			monitor.beginTask("Checking profile description", resources.length);

			for (IResource res : resources) {
				try {
					IWorkbenchProfile workbenchProfile = API
							.getIWorkbenchProfileSession()
							.getWorkbenchProfileFor(
									res.getLocation().toOSString());
					checkPrimitiveTypes(workbenchProfile, res);
					checkStereotypes(workbenchProfile, res);
				} catch (Exception e) {
					EclipsePlugin.log(e);
				}
				monitor.worked(1);
			}
			monitor.done();
		} else {
			TigerstripeProjectAuditor.reportError("Project '"
					+ project.getName() + "' is invalid", project, 222);
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
		IPrimitiveTypeDef[] primitiveTypes = workbenchProfile
				.getPrimitiveTypeDefs(false);
		for (int i = 0; i < primitiveTypes.length; i++) {
			if (!primitiveTypes[i].isValidName()) {
				TigerstripeProjectAuditor.reportError(
						"Invalid primitive type name '"
								+ primitiveTypes[i].getName() + "' detected",
						iresource, 222);
			} else if (!primitiveTypes[i].isRecommendedName()) {
				TigerstripeProjectAuditor
						.reportWarning(
								"Type name '"
										+ primitiveTypes[i].getName()
										+ "' is not recommended as the name of a primitive type",
								iresource, 222);
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
							"Duplicate Annotation Name '"
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
			TigerstripeProjectAuditor.reportError("Invalid Annotation Name '"
					+ stereotype.getName() + "' in "
					+ iResource.getFullPath().toOSString(), iResource, 222);
		}

		for (IStereotypeAttribute attr : stereotype.getAttributes()) {
			if (!isValidName(attr.getName())) {
				TigerstripeProjectAuditor.reportError(
						"Invalid attribute name '" + attr.getName()
								+ "' in Annotation '" + stereotype.getName()
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
