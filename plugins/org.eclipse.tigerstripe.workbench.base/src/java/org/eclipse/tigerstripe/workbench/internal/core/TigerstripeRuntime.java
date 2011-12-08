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
package org.eclipse.tigerstripe.workbench.internal.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.tigerstripe.workbench.internal.startup.PostInstallActions;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IDependency;

/**
 * This singleton class provides access to runtime properties.
 * 
 * It has been significantly re-worked for 1.2 to support the new installation
 * scheme.
 * 
 * If particular, all these values are read from properties file found in
 * ${tigerstripe.home}/install.properties which is created upon activation of
 * the tigerstripe EclipsePlugin.
 * 
 * If this file cannot be found, the headless version of Tigerstripe won't
 * start, which means that the tigerstripe Eclipse plugin must be started at
 * least once before the headless version is available.
 * 
 * This class can be used in 2 environments: - within an Eclipse instance, in
 * which case, the ${tigerstripe.home} has been passed to this singleton so it
 * can read the install.properties directly - outside of an Eclipse instance
 * (headless mode), in which case, the ${tigerstripe.home} will be expected from
 * the Java args (-DTIGERSTRIPE_HOME="xxxx") or from the user environment (as a
 * variable).
 * 
 * @author Eric Dillon
 */
public class TigerstripeRuntime {

	/** Signaling we're running as a generic Eclipse Run */
	public final static int ECLIPSE_GUI_RUN = 1;

	/** Signaling we're running thru the Ant command */
	public final static int ANT_RUN = 2;

	/** Signaling we're running thru the CLI */
	public final static int CLI_RUN = 3;

	/** Signaling we're running thru the Eclipse Headless harness */
	public final static int ECLIPSE_HEADLESS_RUN = 4;

	private static IDependency coreOssjArchive;

	// The properties file created upon activation of
	// org.eclipse.tigerstripe.workbench.ui.base
	// bundle
	public final static String PROPERTIES_FILE = "install.properties";

	// Path to org.eclipse.tigerstripe.workbench.base bundle
	public final static String BASEBUNDLE_ROOT = "base.bundle.root";

	// Path to the Tigerstripe Runtime Directory (containing plugins, etc...)
	public final static String TIGERSTRIPE_RUNTIME_ROOT = "tigerstripe.runtime.root";

	// Version of the currently active tigerstripe feature
	public final static String TIGERSTRIPE_FEATURE_VERSION = "tigerstripe.feature.version";

	// Base Eclipse Bundle id - DO NOT EDIT
	public final static String BASEBUNDLE_ID = "org.eclipse.tigerstripe.workbench.base";

	// // External API Eclipse Bundle id - DO NOT EDIT
	// public final static String EXTERNALAPIBUNDLE_ID =
	// "org.eclipse.tigerstripe.workbench.api.external";
	//
	// Base Workbench Feature bundle id - DO NOT EDIT
	public final static String WORKBENCHFEATURE_ID = "org.eclipse.tigerstripe.workbench";

	// Jar file containing the External API for compilation of Pluggable Plugins
	public final static String EXTERNAL_API_ARCHIVE = "external.api.archive";

	// Jar file containing the External API for compilation of Pluggable Plugins
	public final static String PHANTOM_ARCHIVE = "phantom.project.archive";

	// Archive file containing core definitions for OSS/J
	// TODO: this should be migrated into the profile
	public final static String CORE_OSSJ_ARCHIVE = "core.ossj.archive";

	public final static String TIGERSTRIPE_HOME_DIR = "tigerstripe";

	public final static String TIGERSTRIPE_PLUGINS_DIR = "plugins";

	public final static String TIGERSTRIPE_MODULES_DIR = "modules";

	public final static String PRODUCT_NAME = "product.name";

	// The build properties are set up by Maven at build time and are loaded
	// once by this class.
	private static Properties buildProperties;

	private static TigerstripeRuntime instance;

	// private TigerstripeProject activeProject;

	private static String tigerstripeRuntimeRoot = null;

	/*
	 * the current run type. This is set once and for all Valid values are
	 * ECLIPSE_GUI_RUN, ECLIPSE_HEADLESS_RUN, ANT_RUN, CLI_RUN
	 */
	private static int runType = ECLIPSE_GUI_RUN;

	private static Logger tigerstripeLogger;

	private static RollingFileAppender appender;

	private static boolean loggerInitialized = false;

	private static Level defaultLoggingLevel = Level.ALL;

	private static int maxNumBackupLogs = 9;

	private static final String LOG4J_FQCN = TigerstripeRuntime.class.getName();

	private static String logStartTime = "";

	public static int getRuntype() {
		return TigerstripeRuntime.runType;
	}

	/**
	 * Sets the runtype
	 * 
	 * @param runType -
	 *            valid values are ECLIPSE_GUI_RUN, ECLIPSE_HEADLESS_RUN,
	 *            ANT_RUN, CLI_RUN
	 */
	public static void setRuntype(int runType) {
		TigerstripeRuntime.runType = runType;
	}

	private TigerstripeRuntime() {
	}

	/**
	 * Initialize any Tigerstripe Logging related logic
	 * 
	 * WARNING: this can only be run once the Tigerstripe Runtime Root as been
	 * established.
	 * 
	 * @param loggingDir -
	 *            the absolute path to the dir where the tigerstripe log shall
	 *            be created
	 */
	public static void initLogger() {

		String loggingDirStr = getTigerstripeRuntimeRoot();

		if (!loggerInitialized && loggingDirStr != null) {

			// First check that the loggingDir exists: upon first run
			// it would not have been created at this stage.
			File loggingDir = new File(loggingDirStr);
			if (loggingDir != null && !loggingDir.exists()) {
				loggingDir.mkdirs();
			}

			// Add logic here
			String tigerstripeLoggerID = TigerstripeRuntime.class
					.getCanonicalName();
			String outputPath = loggingDir.getPath() + File.separator
					+ "tigerstripe.log";
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
			try {
				boolean outputFileExists = false;
				if (outputFile.exists())
					outputFileExists = true;
				appender = new RollingFileAppender(patternLayout, outputPath);
				appender.setMaxBackupIndex(maxNumBackupLogs);
				appender.setImmediateFlush(true);
				tigerstripeLogger = Logger.getLogger(tigerstripeLoggerID);
				tigerstripeLogger.removeAllAppenders();
				if (outputFileExists) {
					appender.rollOver();
				}
				tigerstripeLogger.addAppender(appender);
				tigerstripeLogger.setAdditivity(false);
				tigerstripeLogger.setLevel(defaultLoggingLevel);
				loggerInitialized = true;
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}

			// Print info about build in the header of the log
			logInfoMessage(" Tigerstripe(tm) CLI v"
					+ TigerstripeRuntime
							.getProperty(TigerstripeRuntime.TIGERSTRIPE_FEATURE_VERSION));
			logInfoMessage("\nRuntime environment:\n");
			Map<String, String> systemEnv = System.getenv();
			for (String key : systemEnv.keySet()) {
				logInfoMessage("  " + key + " = " + systemEnv.get(key));
			}
		}
	}

	public static void setLogLevel(PluginLog.LogLevel plLevel) {
		tigerstripeLogger.setLevel(plLevel.toLog4j());
	}

	public static void logDebugMessage(String message) {
		logDebugMessage(message, null);
	}

	public static void logDebugMessage(String message, Throwable t) {
		logMessage(Level.DEBUG, message, t);
	}

	public static void logErrorMessage(String message) {
		logErrorMessage(message, null);
	}

	public static void logErrorMessage(String message, Throwable t) {
		logMessage(Level.ERROR, message, t);
	}

	public static void logFatalMessage(String message) {
		logFatalMessage(message, null);
	}

	public static void logFatalMessage(String message, Throwable t) {
		logMessage(Level.FATAL, message, t);
	}

	public static void logInfoMessage(String message) {
		logInfoMessage(message, null);
	}

	public static void logInfoMessage(String message, Throwable t) {
		logMessage(Level.INFO, message, t);
	}

	public static void logTraceMessage(String message) {
		logTraceMessage(message, null);
	}

	public static void logTraceMessage(String message, Throwable t) {
		logMessage(Level.TRACE, message, t);
	}

	public static void logWarnMessage(String message) {
		logWarnMessage(message, null);
	}

	public static void logWarnMessage(String message, Throwable t) {
		logMessage(Level.WARN, message, t);
	}

	public static void logMessage(Level level, String message, Throwable t) {
		tigerstripeLogger.log(LOG4J_FQCN, level, message, t);
	}

	public static TigerstripeRuntime getInstance() {
		if (instance == null) {
			instance = new TigerstripeRuntime();
		}
		return instance;
	}

	/**
	 * Returns the Runtime Properties for the build
	 * 
	 */
	public static String getProperty(String property) {
		return getProperties().getProperty(property);
	}

	/**
	 * get the build-time properties.
	 * 
	 * @return
	 */
	public static Properties getProperties() {

		if (buildProperties == null) {

			if (!PostInstallActions.hasRun()) { 
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					String msg = e.getMessage();
					if (msg==null)
						msg = "";
					
					TigerstripeRuntime.logErrorMessage("Exception while sleeping: " + msg);
				}
			}
			
			
			if (tigerstripeRuntimeRoot == null) {
				// If not setup by now, it means we're running Headless. Let's
				// get this
				// from the env
				tigerstripeRuntimeRoot = System.getenv("TIGERSTRIPE_HOME");
				if (tigerstripeRuntimeRoot == null) {
					System.err.println(" Please define TIGERSTRIPE_HOME.");
					System.exit(-1);
				}
			}

			File propertiesFile = new File(tigerstripeRuntimeRoot
					+ File.separator + PROPERTIES_FILE);
			buildProperties = new Properties();

			FileInputStream propInStream = null; 			
			try {
				propInStream = new FileInputStream(propertiesFile);
				buildProperties.load(propInStream);
			} catch (FileNotFoundException e) {
				buildProperties = null;
				return new Properties();
			} catch (IOException e) {
				buildProperties = null;
				return new Properties();
			} finally {
				if(propInStream != null) {
					try {
						propInStream.close();
					} catch(IOException ioe) {
						TigerstripeRuntime.logErrorMessage("can't close file: " + propertiesFile, ioe);
					}
				}
			}
		}

		return buildProperties;
	}

	/**
	 * Returns the base bundle (org.eclipse.tigerstripe.workbench.base) root
	 */
	public static String getBaseBundleRoot() {
		return getProperty(BASEBUNDLE_ROOT);
	}

	public static String getTigerstripeRuntimeRoot() {
		if (tigerstripeRuntimeRoot != null)
			return tigerstripeRuntimeRoot;
		else
			return getProperty(TIGERSTRIPE_RUNTIME_ROOT);
	}

	public static void setTigerstripeRuntimeRoot(String tigerstripeRuntimeDir) {
		tigerstripeRuntimeRoot = tigerstripeRuntimeDir;
	}

	public static String getLogStartTime() {
		return logStartTime;
	}

	public static String getLogPath() {
		return appender.getFile();
	}

	/**
	 * Returns true if the execution environment is a JDK (i.e. if a Java
	 * compiler) is found in the classpath
	 * 
	 * Since Bug 916, it is necessary to have a JDK to run Tigerstripe headless.
	 * This method is convenient for checking at runtime that we have the proper
	 * environment.
	 */
	public static boolean hasJavaCompiler() {
		try {
			Class.forName("com.sun.tools.javac.main.Main");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public static String getGeneratorDeployLocation() {
		String runtimeRoot = TigerstripeRuntime.getTigerstripeRuntimeRoot();
		String path = runtimeRoot + File.separator + "plugins" + File.separator;

		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return path;
	}
}