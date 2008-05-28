/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.adapt;

import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Overall Tigerstripe Resource Adapter factory
 * 
 * @author erdillon
 * 
 */
public class TigerstripeResourceAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == ITigerstripeModelProject.class) {
			IAbstractTigerstripeProject aProject = adaptToProject(adaptableObject);
			if (aProject instanceof ITigerstripeModelProject) {
				return aProject;
			}
		} else if (adapterType == ITigerstripeGeneratorProject.class
				|| adapterType == ITigerstripeM0GeneratorProject.class
				|| adapterType == ITigerstripeM1GeneratorProject.class) {
			IAbstractTigerstripeProject aProject = adaptToProject(adaptableObject);
			if (aProject instanceof ITigerstripeGeneratorProject) {
				return aProject;
			}
		} else if (adapterType == IAbstractTigerstripeProject.class) {
			return adaptToProject(adaptableObject);
		} else if (adapterType == IModelComponent.class) {
			return adaptToArtifact(adaptableObject);
		}

		return null;
	}

	protected IAbstractArtifact adaptToArtifact(Object adaptableObject) {
		if (adaptableObject instanceof IFile) {
			IFile res = (IFile) adaptableObject;
			if (res != null) {
				try {
					IAbstractTigerstripeProject aProject = TigerstripeCore
							.findProject(res.getProject().getLocation()
									.toFile().toURI());

					if (aProject instanceof ITigerstripeModelProject) {
						ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
						IArtifactManagerSession mgr = project
								.getArtifactManagerSession();

						IAbstractArtifact artifact = ((ArtifactManagerSessionImpl) mgr)
								.getArtifactManager().getArtifactByFilename(
										res.getLocation().toOSString());

						if (artifact == null) {
							try {
								InputStreamReader reader = new InputStreamReader(
										res.getContents());
								artifact = mgr.extractArtifact(reader,
										new NullProgressMonitor());
							} catch (CoreException e) {
								BasePlugin.log(e);
							}
						}
						return artifact;
					} else
						return null;

				} catch (TigerstripeException e) {
					BasePlugin.log(e);
					return null;
				}
			} else
				return null;
		} else
			return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { ITigerstripeModelProject.class,
				ITigerstripeGeneratorProject.class,
				IAbstractTigerstripeProject.class,
				ITigerstripeM0GeneratorProject.class,
				ITigerstripeM1GeneratorProject.class, IModelComponent.class };
	}

	private IAbstractTigerstripeProject adaptToProject(Object adaptableObject) {
		if (adaptableObject instanceof IProject) {
			IProject project = (IProject) adaptableObject;
			if (project.exists() && project.isOpen())
				try {
					IAbstractTigerstripeProject tsProject = TigerstripeCore
							.findProject(project.getLocation().toFile().toURI());

					return tsProject;

				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
		} else if (adaptableObject instanceof IResource) {
			IResource res = (IResource) adaptableObject;
			if (res != null) {
				return adaptToProject(res.getProject());
			}
		} else if (adaptableObject instanceof IAdaptable) {
			IResource res = (IResource) ((IAdaptable) adaptableObject)
					.getAdapter(IResource.class);
			if (res != null) {
				return adaptToProject(res.getProject());
			}
		}

		return null;
	}

}
