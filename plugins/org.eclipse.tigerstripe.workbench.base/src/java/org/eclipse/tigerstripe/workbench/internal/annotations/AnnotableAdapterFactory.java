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
package org.eclipse.tigerstripe.workbench.internal.annotations;

import java.io.StringReader;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.annotations.IAnnotable;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

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

					if (aProject instanceof ITigerstripeModelProject) {
						ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
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
									new NullProgressMonitor());
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
