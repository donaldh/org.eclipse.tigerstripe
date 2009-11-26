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
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PluginHousing {

	protected PluginBody body;

	/**
	 * Triggers the housing for the specified reference
	 * 
	 * @param pluginConfig
	 */
	public void trigger(PluginConfig pluginConfig, RunConfig config)
			throws TigerstripeException {
		body.trigger(pluginConfig, config);
	}

	/**
	 * Triggers the housing for the specified reference
	 * 
	 * @param pluginConfig
	 */
	public PluginReport getReport() throws TigerstripeException {
		return body.getReport();
	}

	public String getLabel() {
		return this.body.getLabel();
	}

	public int getCategory() {
		return body.getCategory();
	}

	public EPluggablePluginNature getPluginNature() {
		return this.body.getPluginNature();
	}
	
	public PluginHousing(PluginBody body) {
		this.body = body;
	}

	public PluginHousing(Class pluginClass) throws TigerstripeException {

		if (PluginBody.class.isAssignableFrom(pluginClass)) {
			try {
				this.body = (PluginBody) pluginClass.newInstance();
			} catch (IllegalAccessException e) {
				throw new TigerstripeException(Messages.formatMessage(
						Messages.UNKNOWN_ERROR_WHILE_LOADING_PLUGIN,
						pluginClass.getName()), e);
			} catch (InstantiationException e) {
				throw new TigerstripeException(Messages.formatMessage(
						Messages.UNKNOWN_ERROR_WHILE_LOADING_PLUGIN,
						pluginClass.getName()), e);
			}
		} else
			throw new TigerstripeException(Messages.formatMessage(
					Messages.UNKNOWN_ERROR_WHILE_LOADING_PLUGIN, pluginClass
							.getName()));
	}

	public String getPluginId() {
		return this.body.getPluginId();
	}

	public String getPluginName() {
		return this.body.getPluginName();
	}
	
	public String getGroupId() {
		return this.body.getGroupId();
	}

	public String getVersion() {
		return this.body.getVersion();
	}

	public String[] getDefinedProperties() {
		return this.body.getDefinedProperties();
	}
	
	// TODO - USe OSGI Version when necessary 
	public boolean matchRef(PluginConfig ref) {
		return getPluginId().equals(ref.getPluginId())
				&& getGroupId().equals(ref.getGroupId())
				&& getVersion().equals(ref.getVersion());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof PluginHousing) {
			PluginHousing housing = (PluginHousing) other;
			return getPluginId().equals(housing.getPluginId());
		}
		return false;
	}
	
	public String getPluginPath(){
		return body.getPluginPath();
	}
}