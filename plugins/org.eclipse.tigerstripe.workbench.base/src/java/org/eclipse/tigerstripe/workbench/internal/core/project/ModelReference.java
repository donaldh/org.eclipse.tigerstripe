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

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
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
		return this.projectContext == null;
	}

	public boolean isProjectContextReference() {
		return this.projectContext != null;
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

	public ITigerstripeModelProject getResolvedModel() {
		if (isProjectContextReference()) {
			// look in the project context first
			try {
				IDependency[] dependencies = projectContext.getDependencies();
				for (IDependency dependency : dependencies) {
					String modelId = "".equals(dependency.getIProjectDetails()
							.getModelId()) ? dependency.getIModuleHeader()
							.getOriginalName() : dependency
							.getIProjectDetails().getModelId();
					if (this.toModelId.equals(modelId)) {
						return dependency.makeModuleProject(projectContext);
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
				// note that for compatibility reasons, if no modelId is set, we
				// compare against mere name.
				String modelId = "".equals(project.getModelId()) ? project
						.getName() : project.getModelId();
				if (this.toModelId.equals(modelId)) {
					return project;
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}

		// finally look for "Installed models"
		// TODO: handle installed models here

		return null;
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
