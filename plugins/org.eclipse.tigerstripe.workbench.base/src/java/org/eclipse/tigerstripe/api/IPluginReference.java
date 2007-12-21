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
package org.eclipse.tigerstripe.api;

import org.eclipse.tigerstripe.api.external.IextPluginReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.plugins.PluginLog;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.generation.RunConfig;

/**
 * A reference to a plugin as it may appear in a Tigerstripe Project.
 * 
 * Within a Tigerstripe project, plugin-specific configuration can be stored
 * through these IPluginReference.
 * 
 * @author Eric Dillon
 * 
 */
public interface IPluginReference extends IextPluginReference {

	/**
	 * Trigger the generation association with this Plugin reference
	 * 
	 * @throws TigerstripeException
	 */
	public void trigger() throws TigerstripeException;

	/**
	 * Trigger the generation association with this Plugin reference
	 * 
	 * @param config -
	 *            optional additional info for plugin run
	 * @throws TigerstripeException
	 */
	public void trigger(RunConfig config) throws TigerstripeException;

	/**
	 * Enables/disables this plugin.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

	public void setProjectHandle(ITigerstripeProject projectHandle);

	public boolean isLogEnabled();

	public PluginLog.LogLevel getCurrentLogLevel();

	public String getLogPath();

	public void setLogLevel(PluginLog.LogLevel logLevel);

	/**
	 * Override the logging details to completely disable the creation of a log
	 * file
	 * 
	 * @param disable
	 */
	public void setDisableLogging(boolean disable);

	public boolean isLoggingDisabled();

	/**
	 * If this plugin is a validation plugin, the fail method can be used to
	 * interrupt the generation cycle.
	 * 
	 * @param message
	 * @since 2.2.4
	 */
	public void fail(String message);

	/**
	 * If this plugin is a validation plugin, the fail method can be used to
	 * interrupt the generation cycle.
	 * 
	 * @param message
	 * @param t
	 * @since 2.2.4
	 */
	public void fail(String message, Throwable t);

	/**
	 * Returns true if the "fail(...)" method was called
	 * 
	 * @return
	 */
	public boolean validationFailed();

	public String getValidationFailMessage();

	public Throwable getValidationFailThrowable();
}
