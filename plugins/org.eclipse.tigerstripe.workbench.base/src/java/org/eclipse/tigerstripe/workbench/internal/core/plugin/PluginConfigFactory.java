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

import java.util.Properties;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A singleton factory class for all PluginConfig
 * 
 * @author Eric Dillon
 */
public class PluginConfigFactory {

	private final static String GROUPID_4DH = "4dh";
	public final static String GROUPID_TS = "ts";

	private final static String VERSION_DEV = "dev";
	public final static String VERSION_1_3 = "1.3";

	private static PluginConfigFactory instance;

	public static PluginConfigFactory getInstance() {
		if (instance == null) {
			instance = new PluginConfigFactory();
		}
		return instance;
	}

	public PluginConfig createPluginConfig(PluginConfig model,
			TigerstripeProject project) throws UnknownPluginException {
		return createPluginConfigInternal(model.getPluginId(), model
				.getGroupId(), model.getVersion(), project);
	}

	/**
	 * Creates a pluginConfig corresponding to the given element
	 * 
	 * @param element
	 * @return
	 */
	public PluginConfig createPluginConfig(Element element,
			TigerstripeProject project) throws UnknownPluginException {

		String groupId = null;
		String pluginId = null;
		String version = null;
		boolean enabled = false;

		NamedNodeMap namedAttributes = element.getAttributes();
		Node groupIdNode = namedAttributes.getNamedItem("groupId");
		Node pluginIdNode = namedAttributes.getNamedItem("pluginId");
		Node versionNode = namedAttributes.getNamedItem("version");
		Node enabledNode = namedAttributes.getNamedItem("enabled");
		Node loggingNode = namedAttributes.getNamedItem("logLevel");
		Node disableLoggingNode = namedAttributes
				.getNamedItem("disableLogging");

		groupId = groupIdNode.getNodeValue();
		// #118 make 4dh references go away
		if (GROUPID_4DH.equals(groupId)) {
			groupId = GROUPID_TS;
		}

		pluginId = pluginIdNode.getNodeValue();

		version = versionNode.getNodeValue();
		// #118 make dev references go away
		if (VERSION_DEV.equals(version)) {
			version = VERSION_1_3;
		}

		PluginConfig pluginConfig = createPluginConfigInternal(pluginId,
				groupId, version, project);

		if (enabledNode == null || enabledNode.getNodeValue().length() == 0) {
			enabled = true; // by default it's enabled if the attribute is
			// missing
		} else {
			enabled = "true".equalsIgnoreCase(enabledNode.getNodeValue());
		}

		if (loggingNode != null) {
			String loggingIndex = loggingNode.getNodeValue();
			int index = Integer.parseInt(loggingIndex);
			pluginConfig.setLogLevel(PluginLog.LogLevel.fromInt(index));
		}

		if (disableLoggingNode != null) {
			String disableLabel = disableLoggingNode.getNodeValue();
			boolean disable = Boolean.parseBoolean(disableLabel);
			pluginConfig.setDisableLogging(disable);
		}

		pluginConfig.setVersion(version);
		pluginConfig.setEnabled(enabled);

		// Load plugin Properties
		Properties properties = loadProperties(element);
		pluginConfig.setProperties(properties);

		// Extract potential facet reference
		NodeList facetRefs = element.getElementsByTagName("facetReference");
		if (facetRefs != null && facetRefs.getLength() == 1) {
			// only one facet for now
			Element facetElement = (Element) facetRefs.item(0);
			try {
				IFacetReference ref = FacetReference.decode(facetElement,
						project);
				if (ref != null)
					pluginConfig.setFacetReference(ref);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}

		return pluginConfig;
	}

	private PluginConfig createPluginConfigInternal(String pluginId,
			String groupId, String version, TigerstripeProject project)
			throws UnknownPluginException {

		PluginConfig pluginConfig = null;

		PluggablePluginConfig ref = new PluggablePluginConfig(project,
				pluginId, groupId);
		ref.setVersion(version);
		pluginConfig = ref;

		return pluginConfig;
	}

	private Properties loadProperties(Node node) {
		Properties result = new Properties();

		NodeList propertiesList = node.getChildNodes();

		for (int i = 0; i < propertiesList.getLength(); i++) {
			Node prop = propertiesList.item(i);
			if ("property".equals(prop.getNodeName())) {
				NamedNodeMap namedAttributes = prop.getAttributes();
				Node name = namedAttributes.getNamedItem("name");
				Node value = prop.getFirstChild();

				if (name != null && !"".equals(name.getNodeValue())
						&& value != null) {
					result.setProperty(name.getNodeValue(), value
							.getNodeValue());
				}
			}
		}
		return result;
	}

}
