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
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface PluginBody {

	public final static String ANY_NATURE = "ts.nature.any";
	public final static String OSSJ_NATURE = "ts.nature.ossj";
	public final static String UNKNOWN_NATURE = "ts.nature.unknown";
	public final static String MTOSI_NATURE = "ts.nature.mtosi";

	/**
	 * Triggers a PluginBody based on the specified reference
	 * 
	 * @param pluginRef
	 * @throws TigerstripeException
	 */
	public void trigger(PluginRef pluginRef, RunConfig config)
			throws TigerstripeException;

	public String getLabel();

	public String getDescription();

	public String getPluginId();

	public String getGroupId();

	public String getVersion();

	public PluginReport getReport();

	/**
	 * Returns a list of the Tigerstripe Project natures supported by this
	 * pluggin.
	 * 
	 * @return String[] - an array of supported natures. At least one nature
	 *         needs to be supported.
	 */
	public String[] getSupportedNatures();

	public String[] getDefinedProperties();

	public int getCategory();

	/**
	 * Returns the default log level set for this plugin
	 * 
	 * Note: this is only relevant is isLogEnabled() == true;
	 * 
	 * @return
	 */
	public PluginLog.LogLevel getDefaultLogLevel();

	public void setLogEnabled(boolean isLogEnabled);

	/**
	 * Returns true if the plugin specific logging mechanism is used for this
	 * plugin
	 * 
	 * @return
	 */
	public boolean isLogEnabled();

	/**
	 * Returns the path (generation dir relative) for the plugin specific log
	 * 
	 * Note: returns null if !isLogEnabled()
	 */
	public String getLogPath();

}
