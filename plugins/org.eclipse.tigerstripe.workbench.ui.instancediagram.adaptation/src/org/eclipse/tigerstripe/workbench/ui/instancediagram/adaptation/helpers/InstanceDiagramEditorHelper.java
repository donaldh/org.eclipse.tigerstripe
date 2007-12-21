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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.IEditorPart;

public class InstanceDiagramEditorHelper {

	private IEditorPart editor;

	public InstanceDiagramEditorHelper(IEditorPart editor) {
		this.editor = editor;
	}

	private IResource getIResource() {
		IResource res = (IResource) editor.getEditorInput().getAdapter(
				IResource.class);
		return res;
	}

	public ITigerstripeProject getCorrespondingTigerstripeProject() {

		IResource res = getIResource();
		IAbstractTigerstripeProject project = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());
		if (project instanceof ITigerstripeProject)
			return (ITigerstripeProject) project;
		return null;
	}

	public String getInitialBasePackage() {

		ITigerstripeProject project = getCorrespondingTigerstripeProject();
		IResource res = getIResource();
		File projectDir = res.getProject().getLocation().toFile();

		try {
			String baseRepository = project.getBaseRepository();
			IResource repo = res.getProject().getFolder(baseRepository);
			if (repo != null && res.getParent() instanceof IFolder) {
				IPath resRelPath = res.getParent().getProjectRelativePath();
				IPath repoPath = repo.getProjectRelativePath();

				int matchingSegments = resRelPath
						.matchingFirstSegments(repoPath);
				if (matchingSegments != 0) {
					IPath packagePath = resRelPath
							.removeFirstSegments(matchingSegments);
					String packageStr = packagePath.toOSString();
					if (packageStr != null && packageStr.length() != 0) {
						packageStr = packageStr
								.replace(File.separatorChar, '.');
					}

					if (packageStr != null && packageStr.length() != 0)
						return packageStr;
				}
			}

			String basePackage = project.getProjectDetails().getProperty(
					IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
					"com.mycompany");

			return basePackage;
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return "com.mycompany";
	}
}
