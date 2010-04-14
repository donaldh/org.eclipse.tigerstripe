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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * @goal generate
 */
public class ModuleGenerationMojo extends AbstractMojo {

	private static final String DELIMITER = "=";

	private static final String MODULE_PROJECT_ARG = "MODULE_PROJECT";

	private static final String MODULE_OUTPUT_ARG = "MODULE_OUTPUT";

	private static final String INCLUDE_DIAGRAMS_ARG = "INCLUDE_DIAGRAMS";

	private static final String INCLUDE_ANNONATIONS_ARG = "INCLUDE_ANNONATIONS";

	protected static final String WIN_ECLIPSE_EXE = "eclipsec.exe";

	protected static final String MAC_ECLIPSE_EXE = "eclipse";

	protected static final String LNX_ECLIPSE_EXE = MAC_ECLIPSE_EXE;

	/**
	 * @parameter expression="${eclipseHome}"
	 * @required
	 */
	public String eclipseHome;

	/**
	 * @parameter expression="${workspace}"
	 * @required
	 */
	public String workspace;

	/**
	 * @parameter expression="${moduleProject}"
	 * @required
	 */
	public String moduleProject;

	/**
	 * @parameter expression="${moduleOutput}"
	 * @required
	 */
	public String moduleOutput;

	/**
	 * @parameter expression="${includeDiagrams}"
	 * @required
	 */
	public String includeDiagrams;

	/**
	 * @parameter expression="${includeAnnotations}"
	 * @required
	 */
	public String includeAnnotations;

	public void execute() throws MojoExecutionException {

		getLog().debug("Workspace: " + workspace);
		getLog().info("Exported project: " + moduleProject);
		getLog().debug("Eclipse home: " + eclipseHome);

		Commandline cl = new Commandline();
		cl.setExecutable(eclipseHome + File.separator
				+ getExecutableForOs(System.getProperty("os.name")));
		cl.createArg(true).setValue("-nosplash");
		cl.createArg().setValue("-data");
		cl.createArg().setValue(workspace);
		cl.createArg().setValue("-application");
		cl.createArg().setValue(
				"org.eclipse.tigerstripe.workbench.headless.moduleGeneration");

		cl.createArg().setValue(MODULE_PROJECT_ARG + DELIMITER + moduleProject);
		cl.createArg().setValue(MODULE_OUTPUT_ARG + DELIMITER + moduleOutput);
		cl.createArg().setValue(
				INCLUDE_DIAGRAMS_ARG + DELIMITER + includeDiagrams);
		cl.createArg().setValue(
				INCLUDE_ANNONATIONS_ARG + DELIMITER + includeAnnotations);

		StreamConsumer consumer = new StreamConsumer() {
			public void consumeLine(String line) {
				getLog().info(line);
			}
		};

		try {
			int result = CommandLineUtils.executeCommandLine(cl, consumer,
					consumer);
			if (result != 0) {
				throw new MojoFailureException(
						"Tigerstripe generation failed. See logs for more information.");
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Command execution failed.", e);
		}
	}

	protected String getExecutableForOs(String osName) {

		if (osName.startsWith("Mac OS")) {
			return MAC_ECLIPSE_EXE;
		} else if (osName.startsWith("Linux")) {
			return LNX_ECLIPSE_EXE;
		} else if (osName.startsWith("Windows")) {
			return WIN_ECLIPSE_EXE;
		}
		throw new UnsupportedOperationException(osName
				+ "is not currently supported.");
	}
}
