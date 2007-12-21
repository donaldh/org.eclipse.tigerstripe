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
package org.eclipse.tigerstripe.eclipse.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.api.project.IProjectSession;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;

/**
 * This class provides informations about the runtime context (TS Descriptor,
 * plugins, etc...) so it is available to all wizards to tap into.
 * 
 * @author Eric Dillon
 * 
 */
public class TSRuntimeContext {

	private ITigerstripeProject projectHandle;

	public TSRuntimeContext() {
	}

	public boolean isValidProject() {
		return projectHandle != null && projectHandle.exists();
	}

	public ITigerstripeProject getProjectHandle() throws TigerstripeException {
		if (!isValidProject())
			throw new TigerstripeException("Invalid Project");
		return this.projectHandle;
	}

	/**
	 * returns the ArtifactManager for this project
	 * 
	 * @return
	 */
	protected ArtifactManager getArtifactManager() throws TigerstripeException {
		ArtifactManagerSessionImpl impl = (ArtifactManagerSessionImpl) projectHandle
				.getArtifactManagerSession();
		return impl.getArtifactManager();
	}

	/**
	 * Reads the TS Descriptor based on the jElement passed in argument.
	 * 
	 * @param jElement
	 */
	public boolean readTSDescriptor(IJavaElement jElement) {
		boolean result = false;
		if (jElement != null) {
			// let's extract what we can
			IProject projectRoot = null;

			try {
				IResource res = jElement.getCorrespondingResource();
				if (res == null)
					throw new TigerstripeException(
							"Unable to locate initial Resource");
				projectRoot = res.getProject();

				// Validate and Load the project descriptor
				try {
					IProjectSession session = API.getDefaultProjectSession();

					if (session.makeTigerstripeProject(projectRoot
							.getLocation().toFile().toURI(), null) instanceof ITigerstripeProject) {
						projectHandle = (ITigerstripeProject) session
								.makeTigerstripeProject(projectRoot
										.getLocation().toFile().toURI(), null);
					} else
						throw new TigerstripeException(
								"Not a Tigerstripe Project.");
				} catch (TigerstripeLicenseException e) {
					EclipsePlugin.log(e);
				}

				// We're good...
				result = projectHandle.exists();
			} catch (TigerstripeException e) {
				// ignore, we'll return false
			} catch (JavaModelException e) {
				// ignore, we'll return false
			}
		}
		return result;
	}

	public IArtifactManagerSession getArtifactSession()
			throws TigerstripeException {
		return getProjectHandle().getArtifactManagerSession();
	}
}
