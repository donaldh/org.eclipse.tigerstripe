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
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ModelRuleHelper {

	public static Collection<ITigerstripeModelProject> getResultSet(
			IPluginConfig pluginConfig, IProgressMonitor monitor)
			throws TigerstripeException {
		// Build the result set
		Collection<ITigerstripeModelProject> resultSet = new HashSet<ITigerstripeModelProject>();
		try {
			IAbstractTigerstripeProject aProject = pluginConfig
					.getProjectHandle();
			if (aProject != null
					&& aProject instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
				//Add the base project itself!
				resultSet.add(project);
				
				
				//include dependencies
				for (IDependency dependecny : project.getDependencies()){
					
					ITigerstripeModuleProject modProj = dependecny.makeModuleProject(project);
					resultSet.add(modProj);
				}
				
				
				for (ITigerstripeModelProject ref : project.getReferencedProjects()) {
					// Direct references - local projects and installed modules.
					resultSet.add(ref);
					resultSet.addAll(getChildModules(ref));
					
				}
				
			}
		} catch (TigerstripeException t) {
			throw new TigerstripeException("Failed to build result Set. ", t);
		}
		return resultSet;
	}


	private static Collection<ITigerstripeModelProject> getChildModules(ITigerstripeModelProject ref) throws TigerstripeException{
		Collection<ITigerstripeModelProject> myResultSet = new HashSet<ITigerstripeModelProject>();
		for (ITigerstripeModelProject  child : ref.getReferencedProjects()) {
			
			myResultSet.add(child);
			myResultSet.addAll(getChildModules(child));
		}
		return myResultSet;
	}

}
