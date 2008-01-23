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
package org.eclipse.tigerstripe.core.ant;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.api.modules.IModulePackager;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.TigerstripeLogProgressMonitor;

/**
 * @author Eric Dillon
 * 
 * This is the main Ant Task for Tigerstripe
 * 
 */
public class TigerstripeModuleBuildTask extends Task {

	/** logger for output */
	private static Logger log = Logger
			.getLogger(TigerstripeModuleBuildTask.class);

	private File baseDir;

	private String moduleId;

	private String modulePath;

	/** return code for ok processing */
	private static final int RC_OK = 0;

	/** return code from command prompt when initialization fails */
	private static final int RC_INIT_ERROR = 20;

	/** return code from running without a JDK */
	private static final int NO_JDK_ERROR = 30;

	/**
	 * validate and build
	 */
	@Override
	public void execute() throws BuildException {
		executeMain();
	}

	/**
	 * Directory from which to archive files; optional.
	 */
	public void setBasedir(File baseDir) {
		this.baseDir = baseDir;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}

	private void executeMain() throws BuildException {
		int returnCode = RC_OK;

		if (!TigerstripeRuntime.hasJavaCompiler()) {
			TigerstripeRuntime
					.logErrorMessage("A JDK is required to run headless. You are running a JRE, please install a JDK.");
			log
					.error("A JDK is required to run headless. You are running a JRE, please install a JDK.");
			returnCode = NO_JDK_ERROR;
		} else {

			try {
				final ITigerstripeProject tsProject = (ITigerstripeProject) API
						.getDefaultProjectSession().makeTigerstripeProject(
								baseDir.toURI(), null);
				log.info("  Building Module : "
						+ (new File(tsProject.getURI())).getAbsolutePath());
				// final IProject project = (IProject)tsProject ;
				internalPerformFinish(tsProject, moduleId, modulePath);

				// IWorkspaceRunnable op = new IWorkspaceRunnable() {
				// public void run(IProgressMonitor monitor) throws
				// CoreException,
				// OperationCanceledException {
				// try {
				// internalPerformFinish(monitor, tsProject, moduleId,
				// modulePath);
				// } catch (InterruptedException e) {
				// throw new OperationCanceledException(e.getMessage());
				// }
				// }
				// };

				log.info("  Successfully completed.");
			} catch (TigerstripeLicenseException e) {
				log.error(e.getLocalizedMessage());
				returnCode = RC_INIT_ERROR;
			} catch (TigerstripeException e) {
				log.error(e.getLocalizedMessage());
				returnCode = RC_INIT_ERROR;
			} catch (Exception e) {
				TigerstripeRuntime.logErrorMessage("Exception detected", e);
				log.error(e.getLocalizedMessage());
				returnCode = RC_INIT_ERROR;
			}
		}
		if (returnCode != RC_OK) {
			exit(returnCode);
		}

	}

	private void internalPerformFinish(ITigerstripeProject tsProject,
			String moduleID, String jarFile) throws InterruptedException {

		try {
			// monitor.beginTask("Packaging project", 10);
			// project.build( IncrementalProjectBuilder.FULL_BUILD, monitor );
			// monitor.worked(1);

			IModulePackager packager = tsProject.getPackager();
			File file = new File(jarFile);

			IModuleHeader header = packager.makeHeader();
			header.setModuleID(moduleID);

			// monitor.worked(3);
			String classesDirStr = tsProject.getURI().getPath()
					+ File.separator + "/bin";
			File classesDir = new File(classesDirStr);
			packager.packageUp(file.toURI(), classesDir, header,
					new TigerstripeLogProgressMonitor());

			// monitor.done();
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME
		}
	}

	protected void exit(int status) {
		System.exit(status);
	}

}
