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

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.workbench.internal.core.cli.App;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A singleton factory class for all PluginRef
 * 
 * @author Eric Dillon
 */
public class PluginRefFactory {

	private final static String GROUPID_4DH = "4dh";
	public final static String GROUPID_TS = "ts";

	private final static String VERSION_DEV = "dev";
	public final static String VERSION_1_3 = "1.3";

	/** logger for output */
	private static Logger log = Logger.getLogger(App.class);

	private static PluginRefFactory instance;

	public static PluginRefFactory getInstance() {
		if (instance == null) {
			instance = new PluginRefFactory();
		}
		return instance;
	}

	public PluginRef createPluginRef(PluginRef model, TigerstripeProject project)
			throws UnknownPluginException {
		return createPluginRefInternal(model.getPluginId(), model.getGroupId(),
				model.getVersion(), project);
	}

	/**
	 * Creates a pluginRef corresponding to the given element
	 * 
	 * @param element
	 * @return
	 */
	public PluginRef createPluginRef(Element element, TigerstripeProject project)
			throws UnknownPluginException {

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

		PluginRef pluginRef = createPluginRefInternal(pluginId, groupId,
				version, project);

		if (enabledNode == null || enabledNode.getNodeValue().length() == 0) {
			enabled = true; // by default it's enabled if the attribute is
			// missing
		} else {
			enabled = "true".equalsIgnoreCase(enabledNode.getNodeValue());
		}

		if (loggingNode != null) {
			String loggingIndex = loggingNode.getNodeValue();
			int index = Integer.parseInt(loggingIndex);
			pluginRef.setLogLevel(PluginLog.LogLevel.fromInt(index));
		}

		if (disableLoggingNode != null) {
			String disableLabel = disableLoggingNode.getNodeValue();
			boolean disable = Boolean.parseBoolean(disableLabel);
			pluginRef.setDisableLogging(disable);
		}

		pluginRef.setVersion(version);
		pluginRef.setEnabled(enabled);

		// Load plugin Properties
		Properties properties = loadProperties(element);
		pluginRef.setProperties(properties);

		pluginRef.extractSpecificXMLContent(element);

		return pluginRef;
	}

	private PluginRef createPluginRefInternal(String pluginId, String groupId,
			String version, TigerstripeProject project)
			throws UnknownPluginException {

		PluginRef pluginRef = null;
		if (JvtPluginRef.MODEL.getPluginId().equals(pluginId)) {
			pluginRef = new JvtPluginRef(project);
		} else if (XmlPluginRef.MODEL.getPluginId().equals(pluginId)) {
			pluginRef = new XmlPluginRef(project);
		} else if (XmlExamplePluginRef.MODEL.getPluginId().equals(pluginId)) {
			pluginRef = new XmlExamplePluginRef(project);
		} else if (WsdlPluginRef.MODEL.getPluginId().equals(pluginId)) {
			pluginRef = new WsdlPluginRef(project);
		} else if (WsdlExamplePluginRef.MODEL.getPluginId().equals(pluginId)) {
			pluginRef = new WsdlExamplePluginRef(project);
		} else if (PublishPluginRef.MODEL.getPluginId().equals(pluginId)) {
			pluginRef = new PublishPluginRef(project);
		} else if (CSVCreatePluginRef.MODEL.getPluginId().equals(pluginId)) {
			pluginRef = new CSVCreatePluginRef(project);
		} else {
			// Consider it as a PluggablePluginRef then.
			PluggablePluginRef ref = new PluggablePluginRef(project);
			ref.setGroupId(groupId);
			ref.setPluginId(pluginId);
			ref.setVersion(version);
			pluginRef = ref;
		}
		return pluginRef;
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
