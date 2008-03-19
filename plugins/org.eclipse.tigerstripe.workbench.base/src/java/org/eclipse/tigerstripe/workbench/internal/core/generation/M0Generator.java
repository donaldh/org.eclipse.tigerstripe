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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class M0Generator {

	private M0RunConfig config = null;

	public M0Generator(M0RunConfig config) {
		this.config = config;
	}

	protected ITigerstripeModelProject getProject() {
		return config.getTargetProject();
	}

	public PluginRunStatus[] run(IProgressMonitor monitor)
			throws TigerstripeException, GenerationException {
		List<PluginRunStatus> result = new ArrayList<PluginRunStatus>();
		monitor.beginTask("Generating.", config.getPluginConfigs().length);
		for (IPluginConfig pConfig : config.getPluginConfigs()) {
			monitor.subTask(pConfig.getPluginId());
			if (pConfig.isEnabled()) {
			}
			monitor.worked(1);
		}
		monitor.done();
		return result.toArray(new PluginRunStatus[result.size()]);
	}

}
