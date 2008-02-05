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

import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectVisitor;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeLogProgressMonitor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class App {

	/** logger for output */
	private static Logger log = Logger.getLogger(App.class);

	private static void initLog() {
		Layout layout = new PatternLayout("[%5p] %m%n");
		Appender appender = new ConsoleAppender(layout);
		log.addAppender(appender);
		log.setLevel(Level.INFO);
		log.setAdditivity(false);
	}

	/** CLI Parser */
	private CommandLine commandLine;

	/** Default console width - for formatting output. */
	private static final int CONSOLE_WIDTH = 80;

	/** Convenience constant for new line character. */
	private static final String LS = System.getProperty("line.separator", "\n");

	/** return code for ok processing */
	private static final int RC_OK = 0;

	/** return code from command prompt when a bad argument is passed */
	private static final int RC_BAD_ARG = 10;

	/** return code from command prompt when initialization fails */
	private static final int RC_INIT_ERROR = 20;

	/** return code from running without a JDK */
	private static final int NO_JDK_ERROR = 30;

	/** Console banner option. */
	private static final String CONSOLE_BANNER = "b";

	/** Display help option. */
	private static final String DISPLAY_HELP = "h";

	/** Display version option. */
	private static final String DISPLAY_VERSION = "v";

	private static final String PACKAGE = "p";

	private static final String MODULEID = "m";

	/** Debug option. */
	private static final String DISPLAY_DEBUG = "d";

	/**
	 * Set the cli parser.
	 * 
	 * @param commandLine
	 *            The command line parser.
	 */
	protected void setCli(CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	/**
	 * Get the CLI parser.
	 * 
	 * @return CommandLine The command line parser.
	 */
	protected CommandLine getCli() {
		return this.commandLine;
	}

	public void initialize(String[] args) throws ParseException, IOException {
		setCli(CLIManager.parse(args));

	}

	/**
	 * Display the command line help if the option is present, then exit
	 */
	private void displayHelp() {
		if (getCli().hasOption(DISPLAY_HELP)) {
			CLIManager.displayHelp();
			exit(RC_OK);
		}
	}

	/**
	 * Display the Maven version info if the option is present, then exit
	 */
	private void displayVersion() {
		if (getCli().hasOption(DISPLAY_VERSION)) {
			printConsoleTigerstripeHeader();
			exit(RC_OK);
		}
	}

	private void checkDebugFlag() {
		if (getCli().hasOption(DISPLAY_DEBUG)) {
			Category.getDefaultHierarchy().setThreshold(Level.DEBUG);
		}
	}

	/**
	 * Prints the MavenSession header.
	 */
	protected void printConsoleTigerstripeHeader() {
		log
				.info(" Tigerstripe(tm) CLI v"
						+ TigerstripeRuntime
								.getProperty(TigerstripeRuntime.TIGERSTRIPE_FEATURE_VERSION));
		log.info("");
	}

	public void doMain(String[] args, Date fullStart) {
		initializeMain(args);

		checkDebugFlag();
		displayHelp();
		displayVersion();

		int returnCode = RC_OK;

		if (!getCli().hasOption(CONSOLE_BANNER)) {
			printConsoleTigerstripeHeader();
		}

		// Bug 916 - first check that we have a Java Compiler (i.e. that this is
		// running
		// within a JDK, not a JRE
		if (!TigerstripeRuntime.hasJavaCompiler()) {
			TigerstripeRuntime
					.logErrorMessage("A JDK is required to run headless. You are running a JRE, please install a JDK.");
			log
					.error("A JDK is required to run headless. You are running a JRE, please install a JDK.");
			returnCode = NO_JDK_ERROR;
		} else {

			File baseDir = new File(".");
			try {
				ITigerstripeProject project = (ITigerstripeProject) InternalTigerstripeCore
						.getDefaultProjectSession().makeTigerstripeProject(
								baseDir.toURI(), null);

				if (getCli().hasOption(PACKAGE)) {
					log.info("  Packaging project: "
							+ (new File(project.getURI())).getAbsolutePath());

					String targetFile = getCli().getOptionValue(PACKAGE);
					String moduleID = project.getProjectDetails().getName()
							+ "-" + project.getProjectDetails().getVersion();
					if (getCli().hasOption(MODULEID)) {
						moduleID = getCli().getOptionValue(MODULEID);
					}

					IModulePackager packager = project.getPackager();
					File file = new File(targetFile);

					IModuleHeader header = packager.makeHeader();
					header.setModuleID(moduleID);

					String classesDirStr = project.getURI().getPath()
							+ File.separator + "/bin";
					File classesDir = new File(classesDirStr);
					packager.packageUp(file.toURI(), classesDir, header,
							new TigerstripeLogProgressMonitor());

					log.info("  Successfully packaged.");
				} else {
					log.info("  Generating project: "
							+ (new File(project.getURI())).getAbsolutePath());
					project.generate(new TigerstripeProjectVisitor());
					log.info("  Successfully completed.");
				}
			
			} catch (TigerstripeException e) {
				log.error(e.getLocalizedMessage());
				returnCode = RC_INIT_ERROR;
			}
		}

		if (returnCode != RC_OK) {
			exit(returnCode);
		}

	}

	/**
	 * Intialize main and exit if failures occur
	 * 
	 * @param args
	 *            command line args
	 */
	private void initializeMain(String[] args) {
		int returnCode = RC_OK;

		try {
			initialize(args);
		} catch (ParseException e) {
			log.info(e.getLocalizedMessage());
			CLIManager.displayHelp();
			returnCode = RC_BAD_ARG;
		} catch (IOException e) {
			log.info(e.getLocalizedMessage());
			returnCode = RC_INIT_ERROR;
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			returnCode = RC_INIT_ERROR;
		}

		if (returnCode != RC_OK) {
			log.info("");
			exit(returnCode);
		}

	}

	/**
	 * Produce a formatted/padded string.
	 * 
	 * @param orig
	 *            The string to format.
	 * @param width
	 *            The width of the resulting formatted string.
	 * @param pad
	 *            The trailing pad character.
	 * 
	 * @return The formatted string, or the original string if the length is
	 *         already &gt;= <code>width</code>.
	 */
	protected String format(String orig, int width, char pad) {
		if (orig.length() >= width)
			return orig;

		StringBuffer buf = new StringBuffer().append(orig);

		int diff = width - orig.length();

		for (int i = 0; i < diff; ++i) {
			buf.append(pad);
		}

		return buf.toString();
	}

	/**
	 * Nicely wraps a message for console output, using a word BreakIterator
	 * instance to determine wrapping breaks.
	 * 
	 * @param msg
	 *            the string message for the console
	 * @param wrapIndent
	 *            the number of characters to indent all lines after the first
	 *            one
	 * @param lineWidth
	 *            the console width that determines where to wrap
	 * @return the message wrapped for the console
	 */
	protected String wrapConsoleMessage(String msg, int wrapIndent,
			int lineWidth) {
		if (msg.indexOf('\n') < 0 && msg.indexOf('\r') < 0)
			return wrapConsoleLine(msg, wrapIndent, lineWidth);

		StringBuffer buf = new StringBuffer();
		StringTokenizer tok = new StringTokenizer(msg, "\n\r");
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken().trim();
			if (token.length() > 0) {
				buf.append(wrapConsoleLine(token, wrapIndent, lineWidth));
				buf.append(LS);
			}
		}
		return buf.toString();
	}

	private String wrapConsoleLine(String msg, int wrapIndent, int lineWidth) {
		int offset = lineWidth - wrapIndent;
		if (msg.length() <= offset)
			return msg;
		else {
			BreakIterator bIter = BreakIterator.getWordInstance();
			StringBuffer buf = new StringBuffer();
			String pad = " ";
			int currentPos = 0;
			bIter.setText(msg);

			while (offset < bIter.getText().getEndIndex()) {
				if (Character.isWhitespace(bIter.getText().first())) {
					// remove leading whitespace and continue
					msg = msg.substring(1);
					bIter.setText(msg);
					continue;
				}

				// get the last boundary before the specified offset
				currentPos = bIter.preceding(offset);
				// append from the start to currentPos
				buf.append(msg.substring(0, currentPos));

				// start next line
				buf.append(LS);

				// pad with spaces to create indent
				for (int i = 0; i != wrapIndent && i < lineWidth; i++) {
					buf.append(pad);
				}

				// set the text of the break iterator to be the rest
				// of the string not already appended
				msg = msg.substring(currentPos);

				// reset the text for another go
				bIter.setText(msg);
			}

			// remove leading whitespace and continue
			while (Character.isWhitespace(msg.charAt(0))) {
				msg = msg.substring(1);
			}
			buf.append(msg);
			return buf.toString();
		}
	}

	/**
	 * Format a time string.
	 * 
	 * @param ms
	 *            Duration in ms.
	 * @return String The formatted time string.
	 */
	protected static String formatTime(long ms) {
		long secs = ms / 1000;
		long min = secs / 60;
		secs = secs % 60;

		if (min > 0)
			return min + " minutes " + secs + " seconds";
		else
			return secs + " seconds";
	}

	/**
	 * Main CLI entry point for MavenSession.
	 * 
	 * @param args
	 *            CLI arguments.
	 */
	public static void main(String[] args) {
		initLog();
		TigerstripeRuntime.setRuntype(TigerstripeRuntime.CLI_RUN);
		TigerstripeRuntime.initLogger();

		Date start = new Date();
		App app = new App();
		app.doMain(args, start);
	}

	/**
	 * To allow subclasses stop the app from exiting
	 * 
	 * @param status
	 *            the value to exit with
	 */
	protected void exit(int status) {
		System.exit(status);
	}
}