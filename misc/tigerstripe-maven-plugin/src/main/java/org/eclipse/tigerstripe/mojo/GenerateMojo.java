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
package org.eclipse.tigerstripe.mojo;

import java.io.File;
import java.util.ArrayList;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * @goal generate
 * @phase generate-sources
 */
public class GenerateMojo extends AbstractMojo {

	private static final String DELIMITER = "=";
	
	private static final String IMPORT_PROJECT_ARG = "PROJECT_IMPORT";

	private static final String GENERATION_PROJECT_ARG = "GENERATION_PROJECT";
	
	protected static final String WIN_ECLIPSE_EXE = "eclipsec.exe";
	
	protected static final String MAC_ECLIPSE_EXE = "eclipse";
	
	protected static final String LNX_ECLIPSE_EXE = MAC_ECLIPSE_EXE;
	
	/**
	 * @parameter expression="${workspace}"
	 * @required
	 */
	public String workspace;
	
	/**
	 * @parameter expression="${projects}"
	 * @required
	 */
	public ArrayList<String> projects;
	
	/**
	 * @parameter expression="${generationProject}"
	 * @required
	 */
	public String generationProject;
	
	public void execute() throws MojoExecutionException {
		
		getLog().debug("Workspace: " + workspace);
		for (String project : projects) {
			getLog().info("Projects: " + project);
		}
		
		Commandline cl = new Commandline();
		cl.setExecutable(System.getenv("ECLIPSE_HOME") + File.separator + getExecutableForOs(System.getProperty("os.name")));
		cl.createArg(true).setValue("-nosplash");
		cl.createArg().setValue("-data");
		cl.createArg().setValue(workspace);
		cl.createArg().setValue("-application");
		cl.createArg().setValue("org.eclipse.tigerstripe.workbench.headless.tigerstripe");
		
		// add plug-in parameters as key/value pairs
		for (String project : projects) {
			cl.createArg().setValue(IMPORT_PROJECT_ARG + DELIMITER + project);
		}
		cl.createArg().setValue(GENERATION_PROJECT_ARG + DELIMITER + generationProject);
		
		StreamConsumer consumer = new StreamConsumer() {
			public void consumeLine(String line) {
				getLog().info(line);
			}
		};

		try {
			int result = CommandLineUtils.executeCommandLine(cl, consumer, consumer);
			if(result != 0) {
				throw new MojoFailureException("Tigerstripe generation failed. See logs for more information.");
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Command execution failed.", e);
		}
	}

	protected String getExecutableForOs(String osName) {
		
		if(osName.startsWith("Mac OS")) {
			return MAC_ECLIPSE_EXE;
		}
		else if (osName.startsWith("Linux")) {
			return LNX_ECLIPSE_EXE;
		}
		else if (osName.startsWith("Windows")) {
			return WIN_ECLIPSE_EXE;
		}
		throw new UnsupportedOperationException(osName + "is not currently supported.");
	}
}
