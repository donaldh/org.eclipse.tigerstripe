/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.StringReader;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.annotations.IAnnotable;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class AnnotableAdapterFactory implements IAdapterFactory {

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IAnnotable.class) {
			return getAnnotable(adaptableObject);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { IAnnotable.class };
	}

	private Object getAnnotable(Object adaptableObject) {
		if (adaptableObject instanceof IModelComponent) {
			IModelComponent component = (IModelComponent) adaptableObject;
			return component.getAdapter(IAnnotable.class);
		} else if (adaptableObject instanceof ICompilationUnit) {
			ICompilationUnit jElem = (ICompilationUnit) adaptableObject;
			IResource res = jElem.getResource();
			if (res != null) {
				try {
					IAbstractTigerstripeProject aProject = TigerstripeCore
							.findProject(res.getProject().getLocation()
									.toFile().toURI());

					if (aProject instanceof ITigerstripeProject) {
						ITigerstripeProject project = (ITigerstripeProject) aProject;
						IArtifactManagerSession mgr = project
								.getArtifactManagerSession();

						IAbstractArtifact artifact = ((ArtifactManagerSessionImpl) mgr)
								.getArtifactManager().getArtifactByFilename(
										jElem.getCorrespondingResource()
												.getLocation().toOSString());

						if (artifact == null) {
							StringReader reader = new StringReader(jElem
									.getSource());
							artifact = mgr.extractArtifact(reader,
									new TigerstripeNullProgressMonitor());
						}
						return artifact.getAdapter(IAnnotable.class);
					} else
						return null;
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
					return null;
				} catch (JavaModelException e) {
					BasePlugin.log(e);
					return null;
				}
			} else
				return null;

		}
		return null;
	}
}
