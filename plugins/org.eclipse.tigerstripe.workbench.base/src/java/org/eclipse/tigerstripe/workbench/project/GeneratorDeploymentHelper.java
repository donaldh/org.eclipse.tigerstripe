/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.project;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.GeneratorProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProjectPackager;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;

/**
 * This helper contains the deployment logic for Generator projects.
 * 
 * To "deploy" a generator project: ... ITigerstripeM1GeneratorProject proj =
 * ... GeneratorDeploymentHelper helper = new GeneratorDeployementHelper();
 * IPath location = helper.deploy(proj, monitor); ...
 * 
 * @author erdillon
 * 
 */
public class GeneratorDeploymentHelper {

	public GeneratorDeploymentHelper() {
	}

	public IPath deploy(ITigerstripeGeneratorProject project, IProgressMonitor monitor) throws TigerstripeException {

		if (monitor == null)
			monitor = new NullProgressMonitor();

		IPath deploymentPath = null;
		monitor.beginTask("Deploying " + project.getName(), 15);

		monitor.subTask("Cleaning plugin repository");
		deploymentPath = getDefaultPluginPathAndFileName(project);
		cleanPluggable(deploymentPath);
		monitor.worked(3);

		PluggablePluginProjectPackager packager = new PluggablePluginProjectPackager(((GeneratorProjectHandle) project).getDescriptor());
		monitor.subTask("Packaging plugin...");
		packager.packageUpProject(monitor, deploymentPath);
		monitor.worked(5);
		monitor.subTask("Reloading Workbench");
		PluginManager.getManager().load();
		monitor.done();

		return deploymentPath;

	}

	public IPath unDeploy(ITigerstripeGeneratorProject project, IProgressMonitor monitor) throws TigerstripeException {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		IPath deploymentPath = null;
		monitor.beginTask("Un-Deploying " + project.getName(), 15);

		monitor.subTask("Cleaning plugin repository");
		deploymentPath = getDefaultPluginPathAndFileName(project);

		return deploymentPath;
	}

	public void unDeploy(IPath zippedPath, IProgressMonitor monitor) throws TigerstripeException {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		// TODO remove the unzipped stuff and reset the classpath?
		cleanPluggable(zippedPath);
		monitor.worked(3);

		monitor.subTask("Reloading Workbench");
		PluginManager.getManager().load();
		monitor.done();
	}

	private void cleanPluggable(IPath zippedPath) {
		File zipFile = zippedPath.toFile();

		if (zipFile.exists()) {
			boolean res = zipFile.delete();
			if (!res) {
				IStatus status = new Status(IStatus.WARNING, BasePlugin.getPluginId(), 222, "Couldn't delete " + zipFile, null);
				BasePlugin.log(status);
			}
		}

		String runtimeRoot = TigerstripeRuntime.getTigerstripeRuntimeRoot();
		String pluginsRoot = runtimeRoot + File.separator + "plugins";

		// where did the zip file unzip?
		String unZippedFile = pluginsRoot + File.separator + "." + zipFile.getName();

		if (unZippedFile.endsWith(".zip")) {
			unZippedFile = unZippedFile.substring(0, unZippedFile.length() - 4);
		}

		File unZippedDir = new File(unZippedFile);
		if (unZippedDir.exists()) {
			boolean res = Util.deleteDir(unZippedDir);
			if (!res) {
				IStatus status = new Status(IStatus.WARNING, BasePlugin.getPluginId(), 222, "Couldn't delete " + unZippedFile, null);
				BasePlugin.log(status);
			}
		}
	}

	/**
	 * For every Generator to be deployed a unique deployment path is computed
	 * based on its label and the deploy location.
	 * 
	 * @param handle
	 * @return
	 * @throws TigerstripeException
	 */
	private IPath getDefaultPluginPathAndFileName(ITigerstripeGeneratorProject handle) throws TigerstripeException {

		String path = TigerstripeRuntime.getGeneratorDeployLocation();
		path += File.separator + getDefaultPluginFileName(handle);
		return new Path(path);
	}

	public String getDefaultPluginFileName(ITigerstripeGeneratorProject handle) throws TigerstripeException {
		return handle.getName() + "-" + handle.getProjectDetails().getVersion() + "_temp.zip";
	}

	/**
	 * Returns true if the given project can be deployed as is.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public boolean canDeploy(ITigerstripeGeneratorProject project) throws TigerstripeException {
		return true;
	}

	/**
	 * Returns true if the given project has already been deployed and can be
	 * un-deployed.
	 * 
	 * @param project
	 * @return
	 * @throws TigerstripeException
	 */
	public boolean canUndeploy(ITigerstripeGeneratorProject project) throws TigerstripeException {
		IPath deploymentPath = getDefaultPluginPathAndFileName(project);
		File zipFile = deploymentPath.toFile();
		return zipFile.exists();
	}

}
