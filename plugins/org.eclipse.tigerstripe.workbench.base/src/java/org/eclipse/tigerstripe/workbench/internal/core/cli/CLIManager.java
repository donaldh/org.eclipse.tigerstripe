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
package org.eclipse.tigerstripe.workbench.internal.core.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * @author Eric Dillon
 * 
 * Command Line Manager. This is largely inspired from Maven.
 * 
 */
public class CLIManager {

	/** Tigerstripe Session't command-line option configuration. */
	private static Options options = null;

	/** Configure the option set. */
	static {
		options = new Options();

		options.addOption(OptionBuilder.create('d'));

		options.addOption(OptionBuilder.create('b'));

		options.addOption(OptionBuilder.create('u'));

		options.addOption(OptionBuilder.create('h'));

		options.addOption("p", "package", true, "Package project into Module");
		options.addOption("m", "moduleID", true, "Force value of ModuleID");
	}

	public static CommandLine parse(String[] args) throws ParseException {
		CommandLineParser parser = new PosixParser();
		return parser.parse(options, args);
	}

	/**
	 * Display usage information based upon current command-line option
	 * configuration.
	 */
	public static void displayHelp() {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("tigerstripe [options] ", "\nOptions:", options,
				"\n");

	}
}