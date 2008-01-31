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

import org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin.IOssjWSDLProfilePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Specializing of a generic PluginConfig
 * 
 * 
 * @author Eric Dillon
 */
public class XmlPluginConfig extends PluginConfig {

	public final static XmlPluginConfig MODEL = new XmlPluginConfig(null);

	private final static String GROUPID = PluginConfigFactory.GROUPID_TS;
	private final static String PLUGINID = "ossj-xml-spec";

	private PackageToSchemaMapper mapper;

	@Override
	public String getActiveVersion() {
		return getProperties().getProperty("activeVersion",
				IOssjWSDLProfilePlugin.defaultVersion);
	}

	@Override
	public String getGroupId() {
		return GROUPID;
	}

	@Override
	public String getPluginId() {
		return PLUGINID;
	}

	/* package */XmlPluginConfig(TigerstripeProject project) {
		super(project);
		mapper = new PackageToSchemaMapper();
	}

	public PackageToSchemaMapper getMapper() {
		return mapper;
	}

	/**
	 * 
	 */
	@Override
	public void appendSpecificXMLContent(Element parent, Document document) {
		mapper.appendToXML(parent, document);
	}

	@Override
	public void extractSpecificXMLContent(Element parent) {
		mapper.updateFromXML(parent);
	}

}
