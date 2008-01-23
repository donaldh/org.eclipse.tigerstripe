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

import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;

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
public class PluginVelocityLog {

	private String templateName;

	public PluginVelocityLog(String templateName) {
		this.templateName = templateName;
	}

	public void log(LogLevel level, String message, Throwable e) {
		PluginLogger.logVelocity(templateName, level, message);
	}

	public void log(LogLevel level, String message) {
		log(level, message, null);
	}

	public void logError(String message, Throwable e) {
		log(LogLevel.ERROR, message, e);
	}

	public void logError(String message) {
		logError(message, null);
	}

	public void logWarning(String message, Throwable e) {
		log(LogLevel.WARNING, message, e);
	}

	public void logWarning(String message) {
		logWarning(message, null);
	}

	public void logInfo(String message, Throwable e) {
		log(LogLevel.INFO, message, e);
	}

	public void logInfo(String message) {
		logInfo(message, null);
	}

	public void logDebug(String message, Throwable e) {
		log(LogLevel.DEBUG, message, e);
	}

	public void logDebug(String message) {
		logDebug(message, null);
	}

	public void logTrace(String message, Throwable e) {
		log(LogLevel.TRACE, message, e);
	}

	public void logTrace(String message) {
		logTrace(message, null);
	}
}
