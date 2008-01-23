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
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRef;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Wsdl2example extends Example {

	private String wsdlName;
	private Document wsdlDoc;

	private NodeList wsdlTypes;
	private NodeList wsdlMessages;
	private NodeList wsdlPortTypes;
	private HashMap<String, String> defMap;
	private String wsdlPath;

	public void generateExample(String wsdlName, PluginRef pluginRef,
			RunConfig config) throws IOException, ParserConfigurationException,
			org.xml.sax.SAXException, TigerstripeException {
		try {

			File ws = new File(wsdlName);

			this.wsdlPath = ws.getParent();

			String wsdlFilename = pluginRef.getProject().getProjectDetails()
					.getOutputDirectory()
					+ File.separator + wsdlName;

			File myFile = new File(wsdlFilename);
			File wsdlFile = new File(pluginRef.getProject().getBaseDir()
					+ File.separator + myFile.getPath());

			this.defMap = new HashMap();
			this.schemaMap = new HashMap();
			this.pluginRef = pluginRef;
			this.defaults = new HashMap();
			this.runningList = new ArrayList<String>();
			setDefaultsForTypes();

			if (!wsdlFile.exists()) {
				// Oops...
				TigerstripeRuntime.logInfoMessage("Invalid WSDL file");
				TigerstripeRuntime.logInfoMessage("Source directory : "
						+ wsdlFile.exists() + " : " + wsdlFile);
				throw new TigerstripeException("Invalid WSDL file " + wsdlFile);

			}

			this.wsdlName = wsdlFile.getName().substring(0,
					wsdlFile.getName().indexOf("."));

			// Change to local Directory in case of local Schemas etc
			this.localDir = wsdlFile.getParent();

			// Check target Directory
			if (targetDir.equals("")) {
				// not set, so use the same location as the wsdl file.
				this.targetDir = this.localDir;
			}

			if (!this.suffix.equals("")) {
				this.targetDir = this.targetDir + suffix;
			}

			if (this.wsdlPath == null) {
				this.targetDir = pluginRef.getProject().getBaseDir()
						+ File.separator
						+ pluginRef.getProject().getProjectDetails()
								.getOutputDirectory() + File.separator
						+ this.suffix;
			} else {
				this.targetDir = pluginRef.getProject().getBaseDir()
						+ File.separator
						+ pluginRef.getProject().getProjectDetails()
								.getOutputDirectory() + File.separator
						+ this.suffix + File.separator + this.wsdlPath;
			}
			File destinationDir = new File(this.targetDir);
			if (!destinationDir.exists()) {
				destinationDir.mkdir();
			}

			// create DOM objects...
			this.factory = DocumentBuilderFactory.newInstance();
			this.factory.setIgnoringComments(true);
			this.factory.setCoalescing(true);
			this.factory.setNamespaceAware(true);

			this.parser = this.factory.newDocumentBuilder();
			this.wsdlDoc = this.parser.parse(wsdlFile);

			// Root will be the defintiions
			Element wsdlRoot = wsdlDoc.getDocumentElement();

			this.wsdlPortTypes = wsdlRoot.getElementsByTagName("wsdl:portType");
			this.wsdlMessages = wsdlRoot.getElementsByTagName("wsdl:message");
			this.wsdlTypes = wsdlRoot.getElementsByTagName("wsdl:types");

			loadWSDLDefinitions(wsdlRoot);
			loadWSDLTypes(localDir);

			extractOperations();

		} catch (TigerstripeException t) {
			throw t;
		} catch (Exception e) {
			String errorMsg = null;
			errorMsg = "Unexpected Error in example creation " + e.getClass();
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException(errorMsg, e);
		}
	}

	public void loadWSDLDefinitions(Element wsdlRoot) {
		// Fill a map with the definition attributes and their values (for
		// lookup purposes)
		NamedNodeMap defs = wsdlRoot.getAttributes();
		for (int a = 0; a < defs.getLength(); a++) {
			// TigerstripeRuntime.logInfoMessage(defs.item(a).getNodeName()+" :
			// "+defs.item(a).getNodeValue());
			// NB This includes xmlns prefix
			this.defMap.put(defs.item(a).getNodeName(), defs.item(a)
					.getNodeValue());
		}
	}

	public boolean loadWSDLTypes(String localDir) throws TigerstripeException {
		// TODO - in our case a set of schema imports
		// Could in theory have local defs as well.....
		try {
			boolean success = false;
			for (int n = 0; n < this.wsdlTypes.getLength(); n++) {
				Element typeElement = (Element) this.wsdlTypes.item(n);
				NodeList schemas = typeElement
						.getElementsByTagName("xsd:schema");
				for (int m = 0; m < schemas.getLength(); m++) {
					Element schemaElement = (Element) schemas.item(m);
					success = loadSchemas(pluginRef.getProject().getBaseDir()
							.getAbsolutePath().toString(), schemaElement);
				}
			}

			if (debugging) {
				for (Object key : schemaMap.keySet()) {
					TigerstripeRuntime
							.logInfoMessage("Shema loaded with namespace : "
									+ key.toString());
				}
			}

			return success;
		} catch (TigerstripeException t) {
			String errorMsg = t.getMessage();
			throw new TigerstripeException(errorMsg, t);

		} catch (Exception e) {
			String errorMsg = "Unknown Problem loading schemas ";
			throw new TigerstripeException(errorMsg, e);
		}
	}

	public boolean extractOperations() throws TigerstripeException {
		// Should go service -> binding -> operation
		try {
			for (int n = 0; n < this.wsdlPortTypes.getLength(); n++) {
				Node port = this.wsdlPortTypes.item(n);
				if (port.getNodeType() == 1) {
					Element portElement = (Element) port;
					NodeList operations = portElement
							.getElementsByTagName("wsdl:operation");
					for (int m = 0; m < operations.getLength(); m++) {
						Element operation = (Element) operations.item(m);
						String operationName = operation.getAttribute("name");
						NodeList inputs = operation
								.getElementsByTagName("wsdl:input");
						NodeList outputs = operation
								.getElementsByTagName("wsdl:output");
						NodeList faults = operation
								.getElementsByTagName("wsdl:fault");

						if (inputs.getLength() > 1 || outputs.getLength() > 1
								|| faults.getLength() > 1)
							// This is invalid IMHO
							throw new TigerstripeException(
									"Too many children of operation :"
											+ operationName);
						// TODO handle failures from below!
						if (inputs.getLength() > 0) {
							if (!buildWSDLExampleFile((Element) inputs.item(0)))
								return false;
						}
						if (outputs.getLength() > 0) {
							if (!buildWSDLExampleFile((Element) outputs.item(0)))
								return false;
						}
						if (faults.getLength() > 0) {
							if (!buildWSDLExampleFile((Element) faults.item(0)))
								return false;
						}
					}
				}

			}
			return true;
		} catch (TigerstripeException t) {
			throw t;
		}
	}

	public boolean buildWSDLExampleFile(Element operationAction)
			throws TigerstripeException {
		addedElements.clear();
		try {
			if (debugging) {
				TigerstripeRuntime.logInfoMessage("Doing :"
						+ operationAction.getAttribute("name"));
			}
			this.outDom = this.parser.newDocument();
			this.runningList.clear();

			Element envelopeElement = this.outDom
					.createElement("soap:Envelope");
			this.outDom.appendChild(envelopeElement);
			Element bodyElement = this.outDom.createElement("soap:Body");
			envelopeElement.appendChild(bodyElement);

			// Find the message
			String messageName = operationAction.getAttribute("message");

			Element messageElement = findElementbyAttribute(this.wsdlMessages,
					"name", separateNS(messageName)[1]);
			if (messageElement != null) {
				// find the "elementElement" ie a DOM element, that has a name
				// Element!
				NodeList parts = messageElement
						.getElementsByTagName("wsdl:part");
				if (parts.getLength() > 1) {
					// This is invalid IMHO
					TigerstripeRuntime
							.logInfoMessage("Too many parts of message!");
					return false;
				}

				String elementAttr = ((Element) parts.item(0))
						.getAttribute("element");

				String elementElementName = separateNS(elementAttr)[1];
				String nameSpaceAlias = separateNS(elementAttr)[0];
				// TODO should really ;look up xmlns!
				String fullNameSpace = defMap.get("xmlns:" + nameSpaceAlias);

				// Seed the first element any away we go!
				if (!doSchemaElement(bodyElement, fullNameSpace,
						elementElementName))
					return false;

				// Once completed, add the used namespaces to the envelope
				envelopeElement.setAttribute("xmlns:soap",
						"http://schemas.xmlsoap.org/soap/envelope");
				Iterator docIt = this.runningList.iterator();
				while (docIt.hasNext()) {
					Document doc = (Document) docIt.next();
					envelopeElement.setAttribute(
							"xmlns:" + getLocalPrefix(doc), getTarget(doc));
				}

				File domOutputFile = new File(targetDir + File.separator
						+ this.wsdlName + File.separator
						+ operationAction.getAttribute("name") + ".example.xml");

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
				Collection<String> files = this.pluginRef.getReport()
						.getGeneratedFiles();
				if (this.wsdlPath == null) {
					files.add(this.suffix + File.separator + this.wsdlName
							+ File.separator
							+ operationAction.getAttribute("name")
							+ ".example.xml");
				} else {
					files.add(this.suffix + File.separator + this.wsdlPath
							+ File.separator + this.wsdlName + File.separator
							+ operationAction.getAttribute("name")
							+ ".example.xml");
				}
				return true;
			} else
				return false;
		} catch (Exception e) {
			throw new TigerstripeException("Error building file ", e);
		}
	}

}
