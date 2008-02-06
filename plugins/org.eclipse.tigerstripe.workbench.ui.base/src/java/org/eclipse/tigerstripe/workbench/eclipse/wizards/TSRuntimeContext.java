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
package org.eclipse.tigerstripe.workbench.eclipse.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

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

				if (TigerstripeCore.findProject(projectRoot.getLocation()
						.toFile().toURI()) instanceof ITigerstripeProject) {
					projectHandle = (ITigerstripeProject) TigerstripeCore
							.findProject(projectRoot.getLocation().toFile()
									.toURI());
				} else
					throw new TigerstripeException("Not a Tigerstripe Project.");

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
