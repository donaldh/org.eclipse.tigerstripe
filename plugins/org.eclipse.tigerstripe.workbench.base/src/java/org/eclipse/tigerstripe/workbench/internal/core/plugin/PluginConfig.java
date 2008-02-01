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

import java.util.Iterator;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginLogger;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Eric Dillon
 * 
 * A PluginConfig is a reference to a plugin that conditions what is triggered for
 * a run of Tigerstripe. PluginConfigs are captured by the ant TigerstripeTask.
 * 
 * A plugin is identified by 3 attributes - groupId: the organization that built
 * the plugin - pluginId: an identifier for that plugin, it is unique within the
 * groupId - version: the version for that plugin.
 * 
 * Once the pluginConfig has been resolved (i.e. the corresponding PluginHousing
 * was successfully found), it can be executed (i.e. calling the actual plugin
 * code)
 * 
 */
public abstract class PluginConfig implements IPluginConfig {

	public static final String PLUGIN_REFERENCE_TAG = "plugin";

	private TigerstripeProject project;

	private boolean enabled;

	private String version = "1.3";

	private Properties properties;

	private PluginHousing housing;

	private ITigerstripeProject projectHandle;

	private PluginLog.LogLevel logLevel = null;

	private boolean disableLogging = false;

	private boolean markAsFailed = false;

	private String failMessage = "";

	private Throwable failThrowable = null;

	public PluginConfig(TigerstripeProject project) {
		this.properties = new Properties();
		this.project = project;
	}

	public Object getProperty(String property) {
		String rawProperty = this.properties.getProperty(property);
		try {
			IPluginProperty prop = getPropertyDef(property);
			if (prop != null)
				return prop.deSerialize(rawProperty);
		} catch (UnknownPluginException e) {
			TigerstripeRuntime.logErrorMessage(
					"UnknownPluginException detected", e);
		}
		return rawProperty;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public boolean matches(PluginHousing housing) {
		boolean result = getGroupId().equals(housing.getGroupId())
				&& getPluginId().equals(housing.getPluginId())
				&& this.version.equals(housing.getVersion());
		return result;
	}



	public void setProperties(Properties properties) {
		if (properties != null) {
			this.properties = properties;
		}
	}

	public TigerstripeProject getProject() {
		return this.project;
	}

	public abstract String getPluginId();

	public abstract String getGroupId();

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return this.version;
	}

	@Override
	public String toString() {
		return getLabel() + "(" + getGroupId() + ", " + getPluginId() + ", "
				+ getVersion() + ")";
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Resolve the Plugin Reference and attach the corresponding PluginHousing,
	 * so it can be executed.
	 * 
	 * @param manager -
	 *            PluginManager the manager to use to resolve this PluginConfig.
	 * @throws UnknownPluginException
	 *             if this Plugin Reference cannot be resolved.
	 */
	public void resolve() throws UnknownPluginException {
		this.housing = PluginManager.getManager().resolveReference(this);
	}

	/**
	 * Returns true if this plugin reference has been resolved.
	 * 
	 * @return true if this plugin reference has been resolved, false otherwise
	 */
	public boolean isResolved() {
		return this.housing != null;
	}

	/**
	 * 
	 */
	public String getLabel() {
		try {
			if (!isResolved()) {
				resolve();
			}
		} catch (UnknownPluginException e) {
			return "unknown";
		}

		return this.housing.getLabel();
	}

	public void trigger(RunConfig config) throws TigerstripeException {
		if (this.housing != null && isEnabled()) {
			this.housing.trigger(this, config);
		}
	}

	public void trigger() throws TigerstripeException {
		trigger(null);
	}

	public PluginReport getReport() throws TigerstripeException {
		if (this.housing != null && isEnabled())
			return this.housing.getReport();
		else
			return null;
	}

	/**
	 * @Since 2.1
	 * @return
	 * @throws TigerstripeException
	 */
	public ITigerstripeProject getProjectHandle() {
		return projectHandle;
	}

	/**
	 * @since 2.1
	 * 
	 */
	public void setProjectHandle(ITigerstripeProject projectHandle) {
		this.projectHandle = projectHandle;
	}

	/**
	 * An array of all properties defined for this Plugin Ref.
	 * 
	 * @return
	 */
	public String[] getDefinedProperties() {
		try {
			if (!isResolved()) {
				resolve();
			}
		} catch (UnknownPluginException e) {
			return new String[0];
		}

		return this.housing.getDefinedProperties();
	}

	/**
	 * Returns the category of plugin (one of the PUBLISH, GENERATE for now)
	 * 
	 * @deprecated This is now obsolete as all plugins are now pluggable
	 *             plugins.
	 */
	@Deprecated
	public int getCategory() {
		if (!isResolved())
			return UNKNOWN_CATEGORY;
		else
			return this.housing.getCategory();
	}

	// =============================================================
	// XML Marshalling
	// These methods are used to encode/decode the pluginConfig into the
	// tigerstripe.xml
	// descriptor.

	/**
	 * 
	 */
	public Element buildPluginElement(Document document) {
		Element plugin = document.createElement(PLUGIN_REFERENCE_TAG);

		// pluginId
		plugin.setAttribute("pluginId", getPluginId());
		plugin.setAttribute("groupId", getGroupId());
		plugin.setAttribute("version", getVersion());
		plugin.setAttribute("enabled", String.valueOf(isEnabled()));
		if (getCurrentLogLevel() != null)
			plugin.setAttribute("logLevel", String.valueOf(getCurrentLogLevel()
					.toInt()));
		plugin.setAttribute("disableLogging", String.valueOf(disableLogging));

		Properties prop = getProperties();
		for (Iterator iterProp = prop.keySet().iterator(); iterProp.hasNext();) {
			String propertyName = (String) iterProp.next();
			String propertyValue = prop.getProperty(propertyName);

			Element property = document.createElement("property");
			property.setAttribute("name", propertyName);
			property.appendChild(document.createTextNode(propertyValue));
			plugin.appendChild(property);
		}

		// This is a hook for any plugin specific XML content
		appendSpecificXMLContent(plugin, document);

		return plugin;
	}

	protected void appendSpecificXMLContent(Element plugin, Document document) {
		// To be overriden by child classes
	}

	protected void extractSpecificXMLContent(Element plugin) {
		// To be overriden by child classes
	}

	protected IPluginProperty getPropertyDef(String property)
			throws UnknownPluginException {
		if (!isResolved()) {
			resolve();
		}

		if (housing instanceof PluggableHousing) {
			PluggableHousing pHousing = (PluggableHousing) housing;
			IPluginProperty[] propDefs = pHousing.getBody()
					.getPProject().getGlobalProperties();
			for (IPluginProperty propDef : propDefs) {
				if (propDef.getName().equals(property))
					return propDef;
			}
		}
		return null;
	}

	protected boolean isTableProperty(String property)
			throws UnknownPluginException {
		return getPropertyDef(property) instanceof ITablePluginProperty;
	}

	// Logging Stuff

	/**
	 * Get Log Level
	 * 
	 * Note: the default log level is set in the housing, but the user can
	 * change that when running the plugin.
	 */
	public PluginLog.LogLevel getCurrentLogLevel() {
		try {
			if (!isResolved()) {
				resolve();
			}

			if (logLevel == null && housing instanceof PluggableHousing) {
				PluggableHousing pHousing = (PluggableHousing) housing;
				return pHousing.getDefaultLogLevel();
			} else
				return logLevel;
		} catch (UnknownPluginException e) {
			TigerstripeRuntime.logTraceMessage(
					"While trying to assess if log was enabled", e);
			return PluginLog.LogLevel.ERROR;
		}
	}

	public void setLogLevel(PluginLog.LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public boolean isLogEnabled() {
		try {
			if (!isResolved()) {
				resolve();
			}

			// local override
			if (disableLogging)
				return false;

			if (housing instanceof PluggableHousing) {
				PluggableHousing pHousing = (PluggableHousing) housing;
				return pHousing.isLogEnabled();
			}
		} catch (UnknownPluginException e) {
			TigerstripeRuntime.logTraceMessage(
					"While trying to assess if log was enabled", e);
			return false;
		}
		return false;
	}

	public String getLogPath() {
		try {
			if (!isResolved()) {
				resolve();
			}

			if (housing instanceof PluggableHousing) {
				PluggableHousing pHousing = (PluggableHousing) housing;
				return pHousing.getLogPath();
			}
		} catch (UnknownPluginException e) {
			TigerstripeRuntime.logTraceMessage("While trying to get log path",
					e);
			return PluginLogger.DEFAULT_PATH;
		}
		return PluginLogger.DEFAULT_PATH;
	}

	public void setDisableLogging(boolean disable) {
		this.disableLogging = disable;
	}

	public boolean isLoggingDisabled() {
		return this.disableLogging;
	}

	public EPluggablePluginNature getPluginNature() {
		try {
			if (!isResolved()) {
				resolve();
			}

			if (housing instanceof PluggableHousing) {
				PluggableHousing pHousing = (PluggableHousing) housing;
				return pHousing.getPluginNature();
			}
		} catch (UnknownPluginException e) {
			TigerstripeRuntime.logTraceMessage(
					"While trying to get plugin nature", e);
			return EPluggablePluginNature.Generic;
		}
		return EPluggablePluginNature.Generic;
	}

	public void fail(String message, Throwable t) {
		markAsFailed = true;
		failMessage = message;
		failThrowable = t;
	}

	public void fail(String message) {
		fail(message, null);
	}

	public String getValidationFailMessage() {
		return failMessage;
	}

	public Throwable getValidationFailThrowable() {
		return failThrowable;
	}

	public boolean validationFailed() {
		return markAsFailed;
	}

	public void resetFailState() {
		markAsFailed = false;
		failMessage = "";
		failThrowable = null;
	}
}
