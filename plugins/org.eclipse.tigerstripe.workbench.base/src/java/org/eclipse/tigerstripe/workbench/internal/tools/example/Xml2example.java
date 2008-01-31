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
package org.eclipse.tigerstripe.workbench.internal.tools.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Xml2example extends Example {

	boolean debugging = false;
	private String xmlName;
	private Document xmlDoc;
	private String schemaPath;

	public void generateExample(String schemaName, PluginConfig pluginConfig)
			throws IOException, ParserConfigurationException,
			org.xml.sax.SAXException, TigerstripeException {
		try {

			File sc = new File(schemaName);
			this.schemaPath = sc.getParent();

			String schemaFilename = pluginConfig.getProject().getProjectDetails()
					.getOutputDirectory()
					+ File.separator + schemaName;
			// TigerstripeRuntime.logInfoMessage(schemaFilename);
			File schema = new File(schemaFilename);
			File xmlFile = new File(pluginConfig.getProject().getBaseDir()
					+ File.separator + schema.getPath());

			this.pluginConfig = pluginConfig;
			this.schemaMap = new HashMap();
			this.defaults = new HashMap();
			this.runningList = new ArrayList<String>();
			setDefaultsForTypes();

			if (!xmlFile.exists()) {
				// Oops...
				TigerstripeRuntime.logInfoMessage("Invalid XML file");
				TigerstripeRuntime.logInfoMessage("Source directory : "
						+ xmlFile.exists() + " : " + xmlFile);
				throw new TigerstripeException("Invalid XML file " + xmlFile);

			}

			this.xmlName = xmlFile.getName().substring(0,
					xmlFile.getName().indexOf("."));

			// Change to local Directory in case of local Schemas etc
			this.localDir = xmlFile.getParent();

			// Check target Directory
			if (targetDir.equals("")) {
				// not set, so use the same location as the xml file.
				this.targetDir = this.localDir;
			}

			if (!this.suffix.equals("")) {
				this.targetDir = this.targetDir + suffix;
			}
			if (this.schemaPath == null) {
				this.targetDir = pluginConfig.getProject().getBaseDir()
						+ File.separator
						+ pluginConfig.getProject().getProjectDetails()
								.getOutputDirectory() + File.separator
						+ this.suffix;
			} else {
				this.targetDir = pluginConfig.getProject().getBaseDir()
						+ File.separator
						+ pluginConfig.getProject().getProjectDetails()
								.getOutputDirectory() + File.separator
						+ this.suffix + File.separator + schemaPath;
			}
			// TigerstripeRuntime.logInfoMessage(this.targetDir);

			File destinationDir = new File(this.targetDir);
			// TigerstripeRuntime.logInfoMessage(destinationDir+ "
			// "+destinationDir.exists());
			if (!destinationDir.exists()) {
				destinationDir.mkdirs();
			}

			// TigerstripeRuntime.logInfoMessage(destinationDir.getAbsolutePath());

			// create DOM objects...
			this.factory = DocumentBuilderFactory.newInstance();
			this.factory.setIgnoringComments(true);
			this.factory.setCoalescing(true);
			this.factory.setNamespaceAware(true);

			this.parser = this.factory.newDocumentBuilder();
			this.xmlDoc = this.parser.parse(xmlFile);

			// Root will be the schema definition
			Element xmlRoot = xmlDoc.getDocumentElement();

			// Need to add "this" schema to the schemaMap

			String namespace = getTarget(this.xmlDoc);
			schemaMap.put(namespace, this.xmlDoc);
			if (debugging) {
				TigerstripeRuntime.logInfoMessage("Adding schema " + namespace);
			}

			// loadSchemas(localDir,xmlRoot);
			loadSchemas(pluginConfig.getProject().getBaseDir().getAbsolutePath()
					.toString(), xmlRoot);
			NodeList elements = xmlRoot.getChildNodes();

			for (int a = 0; a < elements.getLength(); a++) {
				if (elements.item(a) instanceof Element) {
					if (((Element) elements.item(a)).getTagName().equals(
							"element")
							|| ((Element) elements.item(a)).getTagName()
									.equals("complexType")) {
						buildXmlExampleFile((Element) elements.item(a));
					}
				}
			}

		} catch (TigerstripeException t) {
			throw t;
		} catch (Exception e) {
			String errorMsg = null;
			errorMsg = "Unexpected Error in example creation " + e.getClass();
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException(errorMsg, e);
		}
	}

	public boolean buildXmlExampleFile(Element element)
			throws TigerstripeException {
		addedElements.clear();
		try {
			if (debugging) {
				TigerstripeRuntime.logInfoMessage("Doing :"
						+ element.getAttribute("name"));
			}
			this.outDom = this.parser.newDocument();
			this.runningList.clear();

			String elementElementName = element.getAttribute("name");
			String nameSpaceAlias = getLocalPrefix(this.xmlDoc);

			Element startNode;

			String fullNameSpace = lookupNameSpace(xmlDoc, nameSpaceAlias);
			if (element.getTagName().equals("element")) {
				// Seed the first element any away we go!
				if (!doSchemaElement(this.outDom, fullNameSpace,
						elementElementName))
					return false;
			} else if (element.getTagName().equals("complexType")) {
				startNode = this.outDom.createElement(nameSpaceAlias + ":"
						+ elementElementName);
				this.runningList.add(this.xmlDoc);
				this.outDom.appendChild(startNode);
				if (!doSchemaComplexType(startNode, this.xmlDoc, element))
					return false;
			}
			if (!this.runningList.contains(this.xmlDoc)) {
				this.runningList.add(this.xmlDoc);
			}

			Iterator docIt = this.runningList.iterator();
			while (docIt.hasNext()) {
				Document doc = (Document) docIt.next();
				this.outDom.getDocumentElement().setAttribute(
						"xmlns:" + getLocalPrefix(doc), getTarget(doc));
			}

			File domOutputFile = new File(this.targetDir + File.separator
					+ this.xmlName + File.separator + elementElementName
					+ ".example.xml");

			// Do we need to create the target dir ?
			if (!domOutputFile.getParentFile().exists()) {
				domOutputFile.getParentFile().mkdirs();
			}

			this.out = new PrintWriter(new FileWriter(domOutputFile));

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(this.outDom);
			StreamResult result = new StreamResult(out);

			transformer.setOutputProperty("indent", "yes");
			transformer.transform(source, result);
			out.close();

			Collection<String> files = this.pluginConfig.getReport()
					.getGeneratedFiles();
			String subTarget = targetDir.replace(this.pluginConfig.getProject()
					.getProjectDetails().getOutputDirectory(), "");
			if (this.schemaPath == null) {
				files.add(this.suffix + File.separator + elementElementName
						+ ".example.xml");
			} else {
				files.add(this.suffix + File.separator + this.schemaPath
						+ File.separator + elementElementName + ".example.xml");
			}
			return true;

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException("Error building file ", e);
		}
	}

}
