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

import static java.text.MessageFormat.format;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
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
	
	private static List<IStatus> reportedStatuses; 

	/**
	 * Sets up the logger prior to running a pluggable plugin
	 * 
	 * @param pluginConfig
	 */
	public static void setUpForRun(PluginConfig pluginConfig, RunConfig config)
			throws TigerstripeException {
		PluginLogger.pluginConfig = pluginConfig;
		reportedStatuses = new ArrayList<IStatus>();
		if (pluginConfig.isLogEnabled()) {
			TigerstripeRuntime.logTraceMessage("Setting up logger for generator: "
					+ pluginConfig.toString());
			initLogger(pluginConfig, config);
		}
	}
	
	/**
	 * Reported statuses are displayed in generation results dialog. 
	 */
	public static void reportStatus(IStatus status) {
		if (reportedStatuses != null) {
			reportedStatuses.add(status);
		}
	}
	
	public static IStatus[] getReportedStatuses() {
		if (reportedStatuses != null) {
			return reportedStatuses
					.toArray(new IStatus[reportedStatuses.size()]);
		}
		return new IStatus[] {};
	}

	private static void initLogger(PluginConfig pluginConfig, RunConfig config)
			throws TigerstripeException {

		try {
			String tigerstripeLoggerID = PluginLogger.class.getCanonicalName();

			String outputPath = getOutputPath(pluginConfig, config);
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
					"Error while trying to set up log for generator: "
							+ pluginConfig.getLabel(), e);
		}
	}
	
	private static String getOutputPath(PluginConfig pluginConfig, RunConfig config) throws TigerstripeException, IOException {
		String outputDir = pluginConfig.getProjectHandle().getProjectDetails().getOutputDirectory();

		final String projectDir;
		if (pluginConfig.getProjectHandle().getLocation() != null) {
			projectDir = pluginConfig.getProjectHandle().getLocation().toOSString();
		} else if (config != null && config.getAbsoluteOutputDir() != null) {
			projectDir = config.getAbsoluteOutputDir();
		} else {
			throw new IOException("Project Directory is NULL");
		}
		return format("{1}{0}{2}{0}{3}", File.separator, projectDir, outputDir, pluginConfig.getLogPath());
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
		reportedStatuses = null;
		if (logInitialized) {
			TigerstripeRuntime
					.logTraceMessage("Tearing down logger for generator: "
							+ pluginConfig.toString());
			pluginLogger.removeAllAppenders();
			logInitialized = false;
		}
	}

	public static String getLogStartTime() {
		return logStartTime;
	}
}
