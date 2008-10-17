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

import org.eclipse.buckminster.core.cspec.builder.CSpecBuilder;
import org.eclipse.buckminster.core.cspec.builder.ComponentRequestBuilder;
import org.eclipse.buckminster.core.cspec.model.ComponentName;
import org.eclipse.buckminster.core.cspec.model.DependencyAlreadyDefinedException;
import org.eclipse.buckminster.core.ctype.AbstractComponentType;
import org.eclipse.buckminster.core.ctype.IResolutionBuilder;
import org.eclipse.buckminster.core.query.model.ComponentQuery;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.optional.buckminster.TigerstripePlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TigerstripeProjectComponentType extends AbstractComponentType {

	public static final String TIGERSTRIPE_XML_FILE = "tigerstripe.xml";

	private static final TigerstripeProjectCSpecBuilder builder = new TigerstripeProjectCSpecBuilder();

	public IResolutionBuilder getResolutionBuilder(IComponentReader reader, IProgressMonitor monitor) throws CoreException {

		MonitorUtils.complete(monitor);
		return builder;
	}

	public static void addDependencies(IComponentReader reader, CSpecBuilder cspec, Document tsXmlDoc) throws CoreException {

		Node referenceNode = null;
		Element project = tsXmlDoc.getDocumentElement();
		for (Node child = project.getFirstChild(); child != null; child = child.getNextSibling()) {

			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			// look for "references" element
			if ("references".equals(child.getNodeName())) {
				if (child.hasChildNodes()) {
					referenceNode = child;
				}
				break;
			}
		}

		if (referenceNode != null) {

			ComponentQuery query = reader.getNodeQuery().getComponentQuery();
			for (Node ref = referenceNode.getFirstChild(); ref != null; ref = ref.getNextSibling()) {

				if (ref.getNodeType() == Node.ELEMENT_NODE && "reference".equals(ref.getNodeName())) {

					// add the project references
					String componentName = getPathAttributeValue(ref);
					ComponentRequestBuilder depBldr = cspec.createDependencyBuilder();
					depBldr.setName(componentName);
					depBldr.setComponentTypeID("tigerstripe");

					try {
						cspec.addDependency(depBldr);
					} catch (DependencyAlreadyDefinedException e) {
						TigerstripePlugin.getLogger().warning(e.getMessage());
					}
				}
			}
		}
	}

	private static String getPathAttributeValue(Node ref) {

		String path = ref.getAttributes().getNamedItem("path").getTextContent().trim();

		if (path != null) {
			return ref.getAttributes().getNamedItem("path").getTextContent().trim();
		}
		throw new IllegalArgumentException("Invalid reference element");
	}

}
