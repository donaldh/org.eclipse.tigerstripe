/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.headless;

import java.io.File;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IRunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.osgi.framework.Constants;

public class Tigerstripe implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {

		System.out.println(TigerstripeCore.getRuntimeDetails()
				.getBaseBundleValue(Constants.BUNDLE_NAME)
				+ " (v"
				+ TigerstripeCore.getRuntimeDetails().getBaseBundleValue(
						Constants.BUNDLE_VERSION) + ")");

		File currentDir = new File(".");

		try {
			ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
					.findProject(currentDir.toURI());

			IRunConfig config = project.makeDefaultRunConfig();
			PluginRunStatus[] status = project.generate(config, null);
			if (status.length == 0)
				return new Integer(0);
			else
				return new Integer(1);
		} catch (TigerstripeException e) {
			e.printStackTrace();
			return new Integer(1);
		}
	}

	@Override
	public void stop() {
		System.out.println("Stopping");
	}

}
