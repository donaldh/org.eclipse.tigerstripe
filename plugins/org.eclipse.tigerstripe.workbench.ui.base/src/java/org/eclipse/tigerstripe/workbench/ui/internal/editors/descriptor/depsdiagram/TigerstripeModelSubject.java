/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.dependencies.utils.IdObject;

public class TigerstripeModelSubject extends IdObject implements
		IDependencySubject {

	private final ITigerstripeModelProject tsProject;
	private final TigerstripeSubjectsFactory factory;

	public TigerstripeModelSubject(ITigerstripeModelProject tsProject,
			TigerstripeSubjectsFactory factory) {
		this.tsProject = tsProject;
		this.factory = factory;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {

		if (adapter.isAssignableFrom(IResource.class)) {

			IFile file = ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.getFile(
							tsProject.getFullPath().append(
									ITigerstripeConstants.PROJECT_DESCRIPTOR));
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	public String getName() {
		return tsProject.getName();
	}

	public Set<IDependencySubject> getDependencies() {
		Set<IDependencySubject> result = new HashSet<IDependencySubject>();
		try {
			for (ITigerstripeModelProject ref : tsProject
					.getReferencedProjects()) {
				try {
					result.add(factory.getSubject(ref));
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
			for (IDependency dep : tsProject.getDependencies()) {
				result.add(factory.getSubject(dep, tsProject.getModelId()));
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return result;
	}

	public SortedMap<String, String> getProperties() {
		TreeMap<String, String> props = new TreeMap<String, String>();
		try {
			props.put("Model ID", tsProject.getProjectDetails().getModelId());
			props.put("version", tsProject.getProjectDetails().getVersion());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return props;
	}

	public String getDescription() {
		try {
			return tsProject.getProjectDetails().getDescription();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return null;
		}
	}

	public IDependencyType getType() {
		return TigerstripeTypes.PROJECT;
	}

	@Override
	protected String internalId() {
		return tsProject.getName();
	}

	public Image getIcon() {
		return null;
	}

	public boolean isReverseDirection() {
		return false;
	}

	public ITigerstripeModelProject getTsProject() {
		return tsProject;
	}
}