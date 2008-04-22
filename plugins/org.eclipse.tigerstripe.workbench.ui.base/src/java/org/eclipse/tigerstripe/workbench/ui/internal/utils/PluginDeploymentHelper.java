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

import java.io.File;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.GeneratorProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header.PluggablePluginProjectPackager;

/**
 * A helper class that groups all Plugin deployment related operations
 * 
 * @author Eric Dillon
 * 
 */
public class PluginDeploymentHelper {

	private ITigerstripeGeneratorProject ppProject;

	public PluginDeploymentHelper(ITigerstripeGeneratorProject ppProject) {
		Assert.isNotNull(ppProject);
		this.ppProject = ppProject;
	}

	/**
	 * 
	 * @param monitor
	 * @return the path where the plugin was successfully deployed
	 * @throws TigerstripeException -
	 *             if the deploy operation failed
	 */
	public String deploy(IProgressMonitor monitor) throws TigerstripeException {
		String deploymentPath = null;
		monitor.beginTask("Deploying " + ppProject.getProjectLabel(), 15);

		monitor.subTask("Closing all editors");
		EclipsePlugin.closeAllEditors(true, true, false, false, false);
		monitor.worked(2);

		monitor.subTask("Cleaning plugin repository");
		deploymentPath = getDefaultPluginFileName(ppProject);
		cleanPluggable(new File(deploymentPath));
		monitor.worked(3);

		PluggablePluginProjectPackager packager = new PluggablePluginProjectPackager(
				((GeneratorProjectHandle) ppProject).getDescriptor());
		monitor.subTask("Packaging plugin...");
		packager.packageUpProject(monitor, deploymentPath);
		monitor.worked(5);
		monitor.subTask("Reloading Workbench");
		PluginManager.getManager().load();
		monitor.done();

		return deploymentPath;
	}

	public String unDeploy(IProgressMonitor monitor)
			throws TigerstripeException {
		String deploymentPath = null;
		monitor.beginTask("Un-Deploying " + ppProject.getProjectLabel(), 15);

		monitor.subTask("Closing all editors");
		EclipsePlugin.closeAllEditors(true, true, false, false, false);
		monitor.worked(2);

		monitor.subTask("Cleaning plugin repository");
		deploymentPath = getDefaultPluginFileName(ppProject);
		// TODO remove the unzipped stuff and reset the classpath?
		cleanPluggable(new File(deploymentPath));
		monitor.worked(3);

		monitor.subTask("Reloading Workbench");
		PluginManager.getManager().load();
		monitor.done();
		return deploymentPath;
	}

	private void cleanPluggable(File zipFile) {
		if (zipFile.exists()) {
			boolean res = zipFile.delete();
			if (!res) {
				IStatus status = new Status(IStatus.WARNING,
						TigerstripePluginConstants.PLUGIN_ID, 222,
						"Couldn't delete " + zipFile, null);
				EclipsePlugin.log(status);
			}
		}

		String runtimeRoot = TigerstripeRuntime.getTigerstripeRuntimeRoot();
		String pluginsRoot = runtimeRoot + File.separator + "plugins";

		// where did the zip file unzip?
		String unZippedFile = pluginsRoot + File.separator + "."
				+ zipFile.getName();

		if (unZippedFile.endsWith(".zip")) {
			unZippedFile = unZippedFile.substring(0, unZippedFile.length() - 4);
		}

		File unZippedDir = new File(unZippedFile);
		if (unZippedDir.exists()) {
			boolean res = Util.deleteDir(unZippedDir);
			if (!res) {
				IStatus status = new Status(IStatus.WARNING,
						TigerstripePluginConstants.PLUGIN_ID, 222,
						"Couldn't delete " + unZippedFile, null);
				EclipsePlugin.log(status);
			}
		}
	}

	private String getDefaultPluginFileName(ITigerstripeGeneratorProject handle)
			throws TigerstripeException {
		String runtimeRoot = TigerstripeRuntime.getTigerstripeRuntimeRoot();
		String path = runtimeRoot + File.separator + "plugins" + File.separator;

		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		path = path + handle.getProjectLabel() + "-"
				+ handle.getProjectDetails().getVersion() + "_temp.zip";
		return path;
	}

	public boolean canDeploy() throws TigerstripeException {
		return true;
	}

	public boolean canUndeploy() throws TigerstripeException {
		String deploymentPath = getDefaultPluginFileName(ppProject);
		File zipFile = new File(deploymentPath);
		return zipFile.exists();
	}

}
