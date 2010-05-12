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
package org.eclipse.tigerstripe.workbench.internal.core.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.InstalledModuleProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ModuleProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A model reference is used between 2 models.
 * 
 * A reference is done by modelId for now, resolving to the first found matching
 * modelId
 * 
 * Note that an optional project context can be provided so that the resolution
 * is done from that project instead of from the workspace. This allows to find
 * locally included modules that may be in this context project.
 * 
 * @author erdillon
 * 
 */
public class ModelReference {

	private String toModelId = null;
	private ITigerstripeModelProject projectContext = null;

	public final static int INFINITE_LEVEL = -1;

	public ModelReference(ITigerstripeModelProject projectContext,
			String toModelId) {
		assert (toModelId != null && toModelId.length() != 0);
		this.toModelId = toModelId;
		this.projectContext = projectContext;
	}

	public ModelReference(String toModelId) {
		this(null, toModelId);
	}

	public String getToModelId() {
		return this.toModelId;
	}

	public ITigerstripeModelProject getProjectContext() {
		return this.projectContext;
	}

	public boolean isWorkspaceReference() {
		return !isProjectContextReference() && !isInstalledModuleReference();
	}

	public boolean isProjectContextReference() {
		return getResolvedModel() instanceof ModuleProjectHandle;
	}

	public boolean isInstalledModuleReference() {
		return getResolvedModel() instanceof InstalledModuleProjectHandle;
	}

	public boolean isResolved() {
		return getResolvedModel() != null;
	}

	/**
	 * Creates a ModelReference to the given Tigerstripe Model Project
	 * 
	 * @param project
	 * @return
	 */
	public final static ModelReference referenceFromProject(
			ITigerstripeModelProject project) throws TigerstripeException {
		ModelReference mRef = null;
		if (project instanceof IPhantomTigerstripeProject) {
			// TODO
		} else if (project instanceof ITigerstripeModuleProject) {
			// TODO
		} else {
			// This is simply a reference to a project in the workspace
			String modelId = "".equals(project.getModelId()) ? project
					.getName() : project.getModelId();
			mRef = new ModelReference(modelId);
		}
		return mRef;
	}

	public boolean isReferenceTo(ITigerstripeModelProject project) {
		return isResolved() && getResolvedModel().equals(project);
	}

	private ITigerstripeModelProject resolvedModel = null;
	
	public ITigerstripeModelProject getResolvedModel() {
		if (resolvedModel == null){
			resolveModel();
		}
		return resolvedModel;
	}
	
	public void resolveModel() {
		if (projectContext != null) {
			// look in the project context first
			try {
				IDependency[] dependencies = projectContext.getDependencies();
				for (IDependency dependency : dependencies) {
					String modelId = "".equals(dependency.getIProjectDetails()
							.getModelId()) ? dependency.getIModuleHeader()
							.getOriginalName() : dependency
							.getIProjectDetails().getModelId();
					if (this.toModelId.equals(modelId)) {
						resolvedModel = dependency.makeModuleProject(projectContext);
						return;
					}
				}
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}

		// Then look for projects in the workspace
		try {
			ITigerstripeModelProject[] allProjects = TigerstripeCore
					.allModelProjects();
			for (ITigerstripeModelProject project : allProjects) {
				// note that for compatibility reasons, if no modelId is
				// set, we
				// compare against mere name.
				IPath path = project.getFullPath();
				if (path != null && path.segmentCount() == 1) {
					IProject iProject = ResourcesPlugin.getWorkspace()
					.getRoot().getProject(path.segment(0));
					if (iProject.exists() && iProject.isOpen()) {
						String modelId = "".equals(project.getModelId()) ? project
								.getName() : project.getModelId();
								if (this.toModelId.equals(modelId)) {
									resolvedModel = project;
									return;
								}
					}
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		// finally look for "Installed models"
		InstalledModule module = getInstalledModule();
		if (module != null) {
			try {
				resolvedModel = module.makeModuleProject();
				return;
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}
		resolvedModel = null;
		return;
	}

	public InstalledModule getInstalledModule() {
		return InstalledModuleManager.getInstance().getModule(toModelId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModelReference) {
			ModelReference other = (ModelReference) obj;
			if (other.getProjectContext() != null)
				return other.getProjectContext().equals(getProjectContext())
						&& other.getToModelId().equals(getToModelId());
			else
				return other.getToModelId().equals(getToModelId());
		}
		return false;
	}

	@Override
	public String toString() {
		if (getProjectContext() == null)
			return "-> " + getToModelId();
		else
			try {
				return "-(" + getProjectContext().getModelId() + ")-> "
						+ getToModelId();
			} catch (Exception ee) {
				return "-(???)-> " + getToModelId();
			}
	}

}
