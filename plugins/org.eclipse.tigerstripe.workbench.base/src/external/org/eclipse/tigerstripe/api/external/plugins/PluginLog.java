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
package org.eclipse.tigerstripe.api.external.plugins;

import org.apache.log4j.Level;
import org.eclipse.tigerstripe.api.plugins.PluginLogger;

/**
 * User facing interface for Plugin Logs.
 * 
 * To log a message into the plugin specific log:
 * 
 * PluginLog.logError( "my message", e );
 * 
 * The log is configured in the plugin project and has mainly the following
 * options: - log name - log rolling max numbers - log enabled/disabled -
 * default log level
 * 
 * At runtime of the plugin user can control the log level.
 * 
 * @author Eric Dillon
 * 
 */
public class PluginLog {

	public static enum LogLevel {
		ERROR(0), WARNING(1), INFO(2), DEBUG(3), TRACE(4);

		private int level = 0;

		private LogLevel(int level) {
			this.level = level;
		}

		public int toInt() {
			return level;
		}

		public static LogLevel fromInt(int i) {
			if (i == ERROR.level)
				return ERROR;
			else if (i == WARNING.level)
				return WARNING;
			else if (i == INFO.level)
				return INFO;
			else if (i == DEBUG.level)
				return DEBUG;
			else if (i == TRACE.level)
				return TRACE;

			throw new IllegalArgumentException("Unknown Level for " + i);
		}

		public Level toLog4j() {
			if (level == ERROR.level)
				return Level.ERROR;
			else if (level == WARNING.level)
				return Level.WARN;
			else if (level == INFO.level)
				return Level.INFO;
			else if (level == DEBUG.level)
				return Level.DEBUG;
			else if (level == TRACE.level)
				return Level.TRACE;
			throw new IllegalArgumentException("Unknown Level");
		}

	};

	public static void log(LogLevel level, String message, Throwable e) {
		PluginLogger.log(level, message, e);
	}

	public static void log(LogLevel level, String message) {
		PluginLog.log(level, message, null);
	}

	public static void logError(String message, Throwable e) {
		PluginLog.log(LogLevel.ERROR, message, e);
	}

	public static void logError(String message) {
		PluginLog.logError(message, null);
	}

	public static void logWarning(String message, Throwable e) {
		PluginLog.log(LogLevel.WARNING, message, e);
	}

	public static void logWarning(String message) {
		PluginLog.logWarning(message, null);
	}

	public static void logInfo(String message, Throwable e) {
		PluginLog.log(LogLevel.INFO, message, e);
	}

	public static void logInfo(String message) {
		PluginLog.logInfo(message, null);
	}

	public static void logDebug(String message, Throwable e) {
		PluginLog.log(LogLevel.DEBUG, message, e);
	}

	public static void logDebug(String message) {
		PluginLog.logDebug(message, null);
	}

	public static void logTrace(String message, Throwable e) {
		PluginLog.log(LogLevel.TRACE, message, e);
	}

	public static void logTrace(String message) {
		PluginLog.logTrace(message, null);
	}

}
