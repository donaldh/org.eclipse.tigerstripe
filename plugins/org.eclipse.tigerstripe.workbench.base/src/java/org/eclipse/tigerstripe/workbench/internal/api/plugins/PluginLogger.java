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
package org.eclipse.tigerstripe.workbench.internal.api.plugins;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

/**
 * Logger used for Plugin-specific logging.
 * 
 * Upon each plugin run, the logger is reset with the proper details.
 * 
 * @author Eric Dillon
 * 
 */
public class PluginLogger {

	public final static String DEFAULT_PATH = "pluginLog.log";

	private static final String LOG4J_FQCN = PluginLog.class.getName();
	private static String logStartTime = "";

	private static Logger pluginLogger;

	private static PluginConfig pluginConfig;

	private static final int maxNumBackupLogs = 9;

	private static boolean logInitialized = false;

	/**
	 * Sets up the logger prior to running a pluggable plugin
	 * 
	 * @param pluginConfig
	 */
	public static void setUpForRun(PluginConfig pluginConfig, M1RunConfig config)
			throws TigerstripeException {
		PluginLogger.pluginConfig = pluginConfig;

		if (pluginConfig.isLogEnabled()) {
			TigerstripeRuntime.logTraceMessage("Setting up logger for plugin: "
					+ pluginConfig.toString());
			initLogger(pluginConfig, config);
		}
	}

	private static void initLogger(PluginConfig pluginConfig, M1RunConfig config)
			throws TigerstripeException {

		try {
			String tigerstripeLoggerID = PluginLogger.class.getCanonicalName();

			String outputDir = pluginConfig.getProjectHandle().getProjectDetails()
					.getOutputDirectory();
			String projectDir = pluginConfig.getProjectHandle().getLocation().toOSString();

			String outputPath = projectDir + File.separator + outputDir
					+ File.separator + pluginConfig.getLogPath();
			if (config != null && config.getAbsoluteOutputDir() != null) {
				outputPath = config.getAbsoluteOutputDir() + File.separator
						+ outputDir + File.separator + pluginConfig.getLogPath();
			}

			File outputFile = new File(outputPath);
			// the current time (to the nearest millisecond) is used as a
			// unique ID for this process's logfile entries (in case there are
			// multiple processes using the same logfile...
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSZ");
			TimeZone tz = TimeZone.getTimeZone("GMT+0");
			GregorianCalendar calendar = new GregorianCalendar(tz);
			logStartTime = sdf.format(calendar.getTime());
			String conversionPattern = "%-5p %C [%d{dd-MMM-yyyy HH:mm:ss.SSS}] - %m ["
					+ logStartTime + "]%n";
			PatternLayout patternLayout = new PatternLayout(conversionPattern);
			boolean outputFileExists = false;
			if (outputFile.exists())
				outputFileExists = true;
			RollingFileAppender appender = new RollingFileAppender(
					patternLayout, outputPath);
			appender.setMaxBackupIndex(maxNumBackupLogs);
			pluginLogger = Logger.getLogger(tigerstripeLoggerID);
			pluginLogger.removeAllAppenders();
			if (outputFileExists)
				appender.rollOver();
			pluginLogger.addAppender(appender);
			pluginLogger.setAdditivity(false);
			pluginLogger.setLevel(toLevel(pluginConfig.getCurrentLogLevel()));

			logInitialized = true;
		} catch (IOException e) {
			TigerstripeRuntime.logErrorMessage(
					"Error while trying to set up log for plugin: "
							+ pluginConfig.getLabel(), e);
		}

	}

	public static void logVelocity(String templateName,
			PluginLog.LogLevel level, String message) {
		if (logInitialized) {
			pluginLogger.log(LOG4J_FQCN, toLevel(level), templateName + ": "
					+ message, null);
		}
	}

	/**
	 * Note: this is only to be called from PluginLog, or else the reporting
	 * class printed in the log is going to be wrong.
	 * 
	 * For Tigerstripe logging use TigerstripeRuntime instead.
	 * 
	 * @param level
	 * @param message
	 * @param e
	 */
	public static void log(PluginLog.LogLevel level, String message, Throwable e) {
		if (logInitialized) {
			pluginLogger.log(LOG4J_FQCN, toLevel(level), message, e);
		}
	}

	private static Level toLevel(PluginLog.LogLevel level) {
		if (level == PluginLog.LogLevel.TRACE)
			return Level.TRACE;
		else if (level == PluginLog.LogLevel.DEBUG)
			return Level.DEBUG;
		else if (level == PluginLog.LogLevel.INFO)
			return Level.INFO;
		else if (level == PluginLog.LogLevel.WARNING)
			return Level.WARN;
		else if (level == PluginLog.LogLevel.ERROR)
			return Level.ERROR;

		throw new IllegalArgumentException("Level :" + level.toString()
				+ " unknown for logging.");
	}

	/**
	 * 
	 */
	public static void tearDown() {
		if (logInitialized) {
			TigerstripeRuntime
					.logTraceMessage("Tearing down logger for plugin: "
							+ pluginConfig.toString());
			pluginLogger.removeAllAppenders();
			logInitialized = false;
		}
	}

	public static String getLogStartTime() {
		return logStartTime;
	}
}
