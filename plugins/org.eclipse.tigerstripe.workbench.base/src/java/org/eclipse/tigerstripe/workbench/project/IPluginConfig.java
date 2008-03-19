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
package org.eclipse.tigerstripe.workbench.project;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

/**
 * A reference to a plugin as it may appear in a Tigerstripe Project.
 * 
 * Within a Tigerstripe project, plugin-specific configuration can be stored
 * through these IPluginConfig.
 * 
 * @author Eric Dillon
 * 
 */
public interface IPluginConfig {

	public static final int GENERATE_CATEGORY = 0;
	public static final int PUBLISH_CATEGORY = 1;
	public static final int UNKNOWN_CATEGORY = -1;

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
	public void trigger(M1RunConfig config) throws TigerstripeException;

	/**
	 * Enables/disables this plugin.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

	public void setProjectHandle(ITigerstripeModelProject projectHandle);

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

	public int getCategory();

	/**
	 * An array of all properties defined for this Plugin Ref.
	 * 
	 * @return
	 */
	public String[] getDefinedProperties();

	public String getGroupId();

	public String getPluginId();

	public ITigerstripeModelProject getProjectHandle();

	/**
	 * Returns the value of the property as defined in the plugin reference.
	 * 
	 * @param propertyName
	 * @return
	 */
	public Object getProperty(String propertyName);

	public String getVersion();

	public boolean isEnabled();

	/**
	 * Returns the facet reference defined for this plugin config or null if no
	 * facet was defined for this plugin config.
	 * 
	 * @return
	 */
	public IFacetReference getFacetReference();

	/**
	 * Sets the facet reference to use for this plugin config.
	 * 
	 * @param facetReference
	 */
	public void setFacetReference(IFacetReference facetReference);
	
	public EPluggablePluginNature getPluginNature();
	
	public IPluginConfig clone();
}
