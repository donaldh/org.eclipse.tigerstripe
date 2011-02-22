/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author Alena Repina
 * 
 */
public class TigerstripeModuleProject extends TigerstripeProject {

	private final ITigerstripeModelProject container;

	public TigerstripeModuleProject(ITigerstripeModelProject container) {
		super();
		this.container = container;
	}

	public TigerstripeModuleProject() {
		this(null);
	}

	@Override
	public TigerstripeProjectHandle getProjectHandle() {
		try {
			IProjectDetails pd = getIProjectDetails();
			if (pd != null) {
				String modelId = pd.getModelId();
				if (modelId != null) {
					if (container != null) {
						for (IDependency dependency : container
								.getDependencies()) {
							if (dependency.getIModuleHeader().getModuleID()
									.equals(modelId)) {
								ITigerstripeModuleProject moduleProject = dependency
										.makeModuleProject(container);
								if (moduleProject instanceof TigerstripeProjectHandle) {
									return (TigerstripeProjectHandle) moduleProject;
								}
							}
						}
					}

					InstalledModule module = InstalledModuleManager
							.getInstance().getModule(modelId);
					if (module != null) {
						ITigerstripeModuleProject moduleProject = module
								.makeModuleProject();
						if (moduleProject instanceof TigerstripeProjectHandle) {
							return (TigerstripeProjectHandle) moduleProject;
						}
					}
				}
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return null;
	}

	@Override
	public void descriptorChanged(IResource changedDescriptor) {
		// Ignore this - it shouldn't really happen!
	}
}
