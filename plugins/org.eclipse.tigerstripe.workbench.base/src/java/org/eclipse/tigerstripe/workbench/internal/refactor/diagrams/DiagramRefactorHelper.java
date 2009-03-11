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
package org.eclipse.tigerstripe.workbench.internal.refactor.diagrams;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.HeadlessDiagramHandle;

/**
 * This helper contains basic logic to figure out where how to deal (basic
 * stuff) with Diagrams without having to load GMF or have any dependency on UI.
 * 
 * @author erdillon
 * 
 */
public class DiagramRefactorHelper {

	private static final String[] diagramFileExtensions = { "wvd", "wod" }; // FIXME:
	// use
	// extension

	private static final String[] modelFileExtensions = { "vwm", "owm" }; // FIXME:

	// use

	/**
	 * Returns a DiagramHandle for the given resource. The resource can either
	 * be the model file or the diagram file.
	 * 
	 * @param resource
	 * @return
	 */
	public static HeadlessDiagramHandle getDiagramHandle(IResource resource) {
		if (resource instanceof IFile) {
			IResource diagFile = null;
			IResource modelFile = null;
			for (int i = 0; i < diagramFileExtensions.length; i++) {
				String ext = diagramFileExtensions[i];
				if (ext.equals(resource.getFileExtension())) {
					diagFile = resource;
					IResource m = resource.getParent().findMember(
							resource.getName().replace(ext,
									modelFileExtensions[i]));
					if (m != null) {
						modelFile = m;
						break;
					}
				}
			}

			if (diagFile == null || modelFile == null) {
				for (int i = 0; i < modelFileExtensions.length; i++) {
					String ext = modelFileExtensions[i];
					if (ext.equals(resource.getFileExtension())) {
						modelFile = resource;
						IResource d = resource.getParent().findMember(
								resource.getName().replace(ext,
										diagramFileExtensions[i]));
						if (d != null) {
							diagFile = d;
							break;
						}
					}
				}
			}

			ITigerstripeModelProject tsProject = (ITigerstripeModelProject) resource
					.getProject().getAdapter(ITigerstripeModelProject.class);

			if (diagFile != null && modelFile != null && tsProject != null) {
				HeadlessDiagramHandle handle = new HeadlessDiagramHandle(
						diagFile, modelFile, tsProject);
				return handle;
			}
		}
		return null;
	}
}
