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
package org.eclipse.tigerstripe.api.external;

import java.util.Properties;

import org.eclipse.tigerstripe.api.external.project.IextTigerstripeProject;

/**
 * A reference to a plugin as it may appear in a Tigerstripe Project
 * 
 * All plugins are referenced in Tigerstripe project to enable additional
 * project-based configuration to be passed to the plugin (plugin properties).
 * 
 * Each plugin
 * 
 * Plugins in Tigerstripe may belong to one of the categories below:
 * <ul>
 * <li><b>UNKNOWN_CATEGORY</b>: for provision only and development purpose
 * should never be used.</li>
 * <li><b>GENERATE_CATEGORY</b>: to mark a plugin as a plugin that is capable
 * of generating files and should be presented to the user through the
 * generation tab in the project descriptors.</li>
 * <li><b>PUBLISH_CATEGORY</b>: Plugins that appear differently in the GUI
 * (publish mechanisms e.g.)
 * </ul>
 * 
 * @author Eric Dillon
 * 
 */
public interface IextPluginReference {

	public static final int UNKNOWN_CATEGORY = -1;
	public static final int GENERATE_CATEGORY = 0;
	public static final int PUBLISH_CATEGORY = 1;

	public String getPluginId();

	public String getGroupId();

	public String getVersion();

	public String getActiveVersion();

	/**
	 * Returns the value of the property as defined in the plugin reference.
	 * 
	 * @param propertyName
	 * @return
	 */
	public Object getProperty(String propertyName);

	/**
	 * 
	 * @return
	 * @deprecated use {@link #getProperty(String)}. Note that table properties
	 *             cannot be read through this method.
	 */
	@Deprecated
	public Properties getProperties();

	public boolean isEnabled();

	/**
	 * An array of all properties defined for this Plugin Ref.
	 * 
	 * @return
	 */
	public String[] getDefinedProperties();

	public int getCategory();

	// Bug 927 changed the return type to be the external version

	public IextTigerstripeProject getProjectHandle();

	/**
	 * Mark this plugin as "failed". This is only applicable for validation
	 * plugins.
	 * 
	 * @param message
	 */
	public void fail(String message);

	public void fail(String message, Throwable t);
}
