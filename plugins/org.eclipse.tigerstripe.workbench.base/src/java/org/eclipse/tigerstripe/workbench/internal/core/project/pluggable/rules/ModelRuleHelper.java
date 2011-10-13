/*******************************************************************************
 * Copyright (c) 2011 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ModelRuleHelper {

	public static Collection<ModelProject> getResultSet(
			IPluginConfig pluginConfig, IProgressMonitor monitor)
			throws TigerstripeException {
		// Build the result set
		Collection<ModelProject> resultSet = new HashSet<ModelProject>();
		
		IAbstractTigerstripeProject aProject = pluginConfig
				.getProjectHandle();
		if (aProject != null
				&& aProject instanceof ITigerstripeModelProject) {
			ITigerstripeModelProject contextProject = (ITigerstripeModelProject) aProject;
			// Add the base project itself!
			resultSet.add(new ModelProject(contextProject));

			int level = 1;
			// include dependencies
			for (IDependency dependecny : contextProject.getDependencies()) {

				ITigerstripeModuleProject modProj = dependecny
						.makeModuleProject(contextProject);
				resultSet.add(new ModelProject(modProj, contextProject,
						level));
			}

			ITigerstripeModelProject[] references = contextProject
					.getReferencedProjects();
			// Direct references - local projects and installed modules
			for (ITigerstripeModelProject ref : references) {
				resultSet.add(new ModelProject(ref, contextProject, level));
			}
			// others child references
			for (ITigerstripeModelProject ref : references) {
				resultSet
						.addAll(getChildModules(ref, contextProject, level));
			}
		}
		
		return resultSet;
	}

	private static Collection<ModelProject> getChildModules(
			ITigerstripeModelProject ref,
			ITigerstripeModelProject contextProject, int level)
			throws TigerstripeException {
		level++;
		Collection<ModelProject> myResultSet = new HashSet<ModelProject>();
		for (ITigerstripeModelProject child : ref.getReferencedProjects()) {

			myResultSet.add(new ModelProject(child, contextProject, level));
			myResultSet.addAll(getChildModules(child, contextProject, level));
		}
		return myResultSet;
	}

}
