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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.EditorHelper;

/**
 * A helper class that groups all Plugin deployment related operations
 * 
 * @author Eric Dillon
 * 
 */
public class GeneratorDeploymentUIHelper extends GeneratorDeploymentHelper {

	/**
	 * 
	 * @param monitor
	 * @return the path where the plugin was successfully deployed
	 * @throws TigerstripeException -
	 *             if the deploy operation failed
	 */
	@Override
	public IPath deploy(ITigerstripeGeneratorProject project,
			IProgressMonitor monitor) throws TigerstripeException {

		monitor.subTask("Closing all editors");
		EditorHelper.closeAllEditors(true, true, false, false, false);
		monitor.worked(2);

		return super.deploy(project, monitor);
	}

	@Override
	public IPath unDeploy(ITigerstripeGeneratorProject project,
			IProgressMonitor monitor) throws TigerstripeException {
		monitor.subTask("Closing all editors");
		EditorHelper.closeAllEditors(true, true, false, false, false);
		monitor.worked(2);

		return super.unDeploy(project, monitor);
	}

}
