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
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.project.TigerstripeProjectVisitor;

/**
 * @author Eric Dillon
 * 
 * This is the main Ant Task for Tigerstripe
 * 
 */
public class TigerstripeTask extends Task {

	/** logger for output */
	private static Logger log = Logger.getLogger(TigerstripeTask.class);

	private File baseDir;

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
		TigerstripeRuntime.setRuntype(TigerstripeRuntime.ANT_RUN);
		TigerstripeRuntime.initLogger();

		executeMain();
	}

	/**
	 * Directory from which to archive files; optional.
	 */
	public void setBasedir(File baseDir) {
		this.baseDir = baseDir;
	}

	private void executeMain() throws BuildException {
		// Added code to generate through Ant script - bug # 83
		int returnCode = RC_OK;
		if (!TigerstripeRuntime.hasJavaCompiler()) {
			TigerstripeRuntime
					.logErrorMessage("A JDK is required to run headless. You are running a JRE, please install a JDK.");
			log
					.error("A JDK is required to run headless. You are running a JRE, please install a JDK.");
			returnCode = NO_JDK_ERROR;
		} else {

			try {
				ITigerstripeProject project = (ITigerstripeProject) API
						.getDefaultProjectSession().makeTigerstripeProject(
								baseDir.toURI(), null);
				log.info("  Generating project: "
						+ (new File(project.getURI())).getAbsolutePath());
				TigerstripeRuntime.logInfoMessage("Generating Project : "
						+ project.getURI().getPath().toString());
				project.generate(new TigerstripeProjectVisitor());
				log.info("  Successfully completed.");
			} catch (TigerstripeLicenseException e) {
				log.error(e.getLocalizedMessage());
				returnCode = RC_INIT_ERROR;
			} catch (TigerstripeException e) {
				log.error(e.getLocalizedMessage());
				returnCode = RC_INIT_ERROR;
			}
		}
		if (returnCode != RC_OK) {
			exit(returnCode);
		}

		TigerstripeRuntime.logInfoMessage("The base directory is : " + baseDir);

	}

	protected void exit(int status) {
		System.exit(status);
	}

}
