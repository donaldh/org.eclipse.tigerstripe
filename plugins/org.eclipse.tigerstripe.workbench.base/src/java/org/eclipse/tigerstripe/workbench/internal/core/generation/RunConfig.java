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
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import org.apache.velocity.VelocityContext;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A RunConfig contains all the details necessary to configure a generation run
 * 
 * @author erdillon
 * 
 */
public abstract class RunConfig {

	public final static int M0 = 0;
	public final static int M1 = 1;

	// The target project
	private ITigerstripeModelProject targetProject;

	// Project relative output path
	private IPath outputPath;

	// A config for all plugins to run
	private IPluginConfig[] pluginConfigs;

	protected RunConfig(ITigerstripeModelProject project) {
		targetProject = project;
	}

	/**
	 * Factory method for all generation Configs
	 * 
	 * @param project
	 * @param runType
	 * @exception TigerstripeException
	 * @return
	 */
	public static RunConfig newGenerationConfig(
			ITigerstripeModelProject project, int runType)
			throws TigerstripeException {
		RunConfig result = null;
		switch (runType) {
		case M0:
			result = new M0RunConfig(project);
			break;
		case M1:
			result = new M1RunConfig(project);
			break;
		}
		if (result == null)
			throw new IllegalArgumentException(
					"Unknown generation config type (" + runType + ").");

		result.initializeFromProject();
		return result;
	}

	protected void initializeFromProject() throws TigerstripeException {
		setOutputPath(new Path(getTargetProject().getProjectDetails()
				.getOutputDirectory()));
	}

	public IPath getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(IPath outputPath) {
		this.outputPath = outputPath;
	}

	public IPluginConfig[] getPluginConfigs() {
		return pluginConfigs;
	}

	public void setPluginConfigs(IPluginConfig[] pluginConfigs) {
		this.pluginConfigs = pluginConfigs;
	}

	public ITigerstripeModelProject getTargetProject() {
		return targetProject;
	}

}
