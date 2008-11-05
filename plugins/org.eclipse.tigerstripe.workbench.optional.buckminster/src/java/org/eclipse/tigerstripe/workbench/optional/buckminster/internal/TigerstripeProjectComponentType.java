/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     J. Strawn (Cisco Systems, Inc.) - Initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.optional.buckminster.internal;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.buckminster.core.cspec.builder.CSpecBuilder;
import org.eclipse.buckminster.core.cspec.builder.ComponentRequestBuilder;
import org.eclipse.buckminster.core.cspec.model.DependencyAlreadyDefinedException;
import org.eclipse.buckminster.core.ctype.AbstractComponentType;
import org.eclipse.buckminster.core.ctype.IResolutionBuilder;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.core.version.IVersionType;
import org.eclipse.buckminster.runtime.BuckminsterException;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.optional.buckminster.TigerstripePlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TigerstripeProjectComponentType extends AbstractComponentType {

	public static final String TIGERSTRIPE_XML_FILE = "tigerstripe.xml";

	private static final TigerstripeProjectCSpecBuilder builder = new TigerstripeProjectCSpecBuilder();

	public IResolutionBuilder getResolutionBuilder(IComponentReader reader, IProgressMonitor monitor) throws CoreException {

		MonitorUtils.complete(monitor);
		return builder;
	}

	public static void addDependencies(IComponentReader reader, CSpecBuilder cspec, Document tsxml) throws CoreException {

		Node referenceNode = null;
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			referenceNode = (Node) xpath.evaluate("/tigerstripe/references", tsxml, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw BuckminsterException.wrap(e);
		}

		if (referenceNode != null) {

			for (Node ref = referenceNode.getFirstChild(); ref != null; ref = ref.getNextSibling()) {

				if (ref.getNodeType() == Node.ELEMENT_NODE && "reference".equals(ref.getNodeName())) {

					// add the project references
					String componentName = getPathAttributeValue(ref);
					ComponentRequestBuilder depBldr = cspec.createDependencyBuilder();
					depBldr.setName(componentName);
					depBldr.setComponentTypeID("tigerstripe");
					// CURRENTLY NO VERSION SUPPORT IN THE REFERENCES SECTION OF
					// TS.XML!
					try {
						cspec.addDependency(depBldr);
					} catch (DependencyAlreadyDefinedException e) {
						TigerstripePlugin.getLogger().warning(e.getMessage());
					}
				}
			}
		}

		Node pluginsNode = null;
		xpath = XPathFactory.newInstance().newXPath();
		try {
			pluginsNode = (Node) xpath.evaluate("/tigerstripe/plugins", tsxml, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw BuckminsterException.wrap(e);
		}

		if (pluginsNode != null) {

			for (Node ref = pluginsNode.getFirstChild(); ref != null; ref = ref.getNextSibling()) {

				if (ref.getNodeType() == Node.ELEMENT_NODE && "plugin".equals(ref.getNodeName())) {

					if (getPluginEnabled(ref)) {

						// add the project references
						String componentName = getPluginAttributeValue(ref);
						componentName = componentName.substring(0, componentName.indexOf('('));
						ComponentRequestBuilder depBldr = cspec.createDependencyBuilder();
						depBldr.setName(componentName);
						depBldr.setComponentTypeID("tigerstripe.generator");
						depBldr.setVersionDesignator(getVersionAttributeValue(ref), IVersionType.OSGI);
						try {
							cspec.addDependency(depBldr);
						} catch (DependencyAlreadyDefinedException e) {
							TigerstripePlugin.getLogger().warning(e.getMessage());
						}
					}
				}
			}
		}

	}

	private static String getVersionAttributeValue(Node ref) {

		String version = ref.getAttributes().getNamedItem("version").getTextContent().trim();
		if (version != null) {
			return version;
		}
		throw new IllegalArgumentException("Invalid reference element");
	}

	private static boolean getPluginEnabled(Node ref) {

		String enabled = ref.getAttributes().getNamedItem("enabled").getTextContent().trim();
		if (enabled != null) {
			return Boolean.valueOf(enabled).booleanValue();
		}
		throw new IllegalArgumentException("Invalid value defined for \"enabled\" attribute in Tigerstripe.xml file.");
	}

	private static String getPluginAttributeValue(Node ref) {

		String path = ref.getAttributes().getNamedItem("pluginId").getTextContent().trim();
		if (path != null) {
			return path;
		}
		throw new IllegalArgumentException("Invalid reference element");
	}

	private static String getPathAttributeValue(Node ref) {

		String path = ref.getAttributes().getNamedItem("path").getTextContent().trim();
		if (path != null) {
			return path;
		}
		throw new IllegalArgumentException("Invalid reference element");
	}

}
