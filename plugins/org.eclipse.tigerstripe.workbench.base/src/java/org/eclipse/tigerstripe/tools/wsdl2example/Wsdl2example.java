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
package org.eclipse.tigerstripe.tools.wsdl2example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Wsdl2example {

	boolean debugging = false;

	private HashMap defaults;

	private String wsdlName;

	private NodeList wsdlTypes;
	private NodeList wsdlMessages;
	private NodeList wsdlPortTypes;
	private NodeList wsdlBindings;
	private NodeList wsdlServices;

	DocumentBuilderFactory factory;
	private DocumentBuilder parser;
	private Document wsdlDoc;
	private Document outDom;

	private HashMap<String, String> defMap;

	private HashMap<String, Document> schemaMap;
	// Map of all documents that ever get parsed,
	// the key is the namespace - need to check local alias
	// before looking in here

	private PrintWriter out;
	private String localDir;

	private String targetDir = "";
	private String suffix = "";

	private ArrayList runningList;

	private boolean allowNetworkSchemas = false;

	private String schemaDefaultPath;

	public void setAllowNetworkSchemas(boolean allow) {
		this.allowNetworkSchemas = allow;
	}

	public void setSchemaDefaultPath(String path) {
		this.schemaDefaultPath = path;
	}

	public String getTargetDir() {
		return this.targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public void setTargetDirSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void generateExample(File wsdlFile) throws IOException,
			ParserConfigurationException, org.xml.sax.SAXException,
			TigerstripeException {
		try {
			this.defMap = new HashMap();
			this.schemaMap = new HashMap();
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

			this.wsdlServices = wsdlRoot.getElementsByTagName("wsdl:service");
			this.wsdlBindings = wsdlRoot.getElementsByTagName("wsdl:binding");
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
					success = loadSchemas(localDir, schemaElement);
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

	public boolean loadSchemas(String localDir, Element schemaElement)
			throws TigerstripeException {

		String actualLocation = null;

		NodeList imports = schemaElement.getElementsByTagName("xsd:import");
		if (imports.getLength() == 0) {
			imports = schemaElement.getElementsByTagName("import");
		}
		for (int o = 0; o < imports.getLength(); o++) {
			NamedNodeMap defs = imports.item(o).getAttributes();
			// Must have namespace and schemaLocation
			String namespace = defs.getNamedItem("namespace").getNodeValue();
			String schemaLocation = defs.getNamedItem("schemaLocation")
					.getNodeValue();
			Document newDoc;
			if (!schemaMap.containsKey(namespace)) {
				try {

					// See if we can find a file locally first
					// Logic is look in:
					// Local Dir
					// children of LocalDir (strip off the http://xxx.yyy/ from
					// the file name
					// Defaults Dir (from plugin)
					// Network (if allowed)
					String assumedName;

					if (schemaLocation.contains("/")) {
						assumedName = schemaLocation.substring(schemaLocation
								.lastIndexOf("/") + 1);
					} else {
						assumedName = schemaLocation;
					}

					String locationExtension;
					if (schemaLocation.startsWith("http")) {
						locationExtension = schemaLocation.substring("http://"
								.length() + 1);
						locationExtension = locationExtension
								.substring(locationExtension.indexOf("/"));
					} else {
						locationExtension = schemaLocation;
					}

					// Look locally - easy
					File schemaFile = new File(localDir + "\\" + assumedName);
					File schemaExtensionFile = new File(localDir + "\\"
							+ locationExtension);
					File defaultFile = new File(this.schemaDefaultPath + "\\"
							+ assumedName);

					// TigerstripeRuntime.logInfoMessage("Default locn
					// "+defaultFile.getPath());

					File defaultExtensionFile = new File(this.schemaDefaultPath
							+ "\\" + locationExtension);

					if (schemaFile.exists()) {
						actualLocation = localDir + "\\" + assumedName;
					} else if (schemaExtensionFile.exists()) {
						actualLocation = localDir + "\\" + locationExtension;
					} else if (defaultFile.exists()) {
						actualLocation = this.schemaDefaultPath + "\\"
								+ assumedName;
					} else if (defaultExtensionFile.exists()) {
						actualLocation = this.schemaDefaultPath + "\\"
								+ locationExtension;
					} else if (schemaLocation.startsWith("http")
							&& allowNetworkSchemas) {
						actualLocation = schemaLocation;
					} else
						// Can't find it, or not allowed to look!
						throw new TigerstripeException(
								"Failed to load Schema : " + namespace
										+ " : failed to find file");

					if (debugging) {
						TigerstripeRuntime.logInfoMessage("using "
								+ actualLocation);
					}
					newDoc = parser.parse(actualLocation);

					// This is what it was.....
					// if (schemaLocation.startsWith("http")){
					// newDoc = parser.parse(schemaLocation);
					// }else {
					// // look for a local file
					// newDoc = parser.parse(localDir+"\\"+schemaLocation);
					// }

					schemaMap.put(namespace, newDoc);
					// TigerstripeRuntime.logInfoMessage("Adding schema " +
					// namespace);
					Element childSchemaElement = newDoc.getDocumentElement();
					loadSchemas(localDir, childSchemaElement);

				} catch (TigerstripeException t) {
					throw t;
				} catch (Exception e) {
					throw new TigerstripeException("Failed to load Schema : "
							+ namespace + " from location " + actualLocation
							+ "Unknown reason", e);
				}
			}
		}

		return true;
	}

	public boolean extractOperations() throws TigerstripeException {
		String[] ops = new String[0];

		// Bit of a shortcut for now as we know the binding...

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
							if (!buildFile((Element) inputs.item(0)))
								return false;
						}
						if (outputs.getLength() > 0) {
							if (!buildFile((Element) outputs.item(0)))
								return false;
						}
						if (faults.getLength() > 0) {
							if (!buildFile((Element) faults.item(0)))
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

	public boolean buildFile(Element operationAction)
			throws TigerstripeException {

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
				return true;
			} else
				return false;
		} catch (Exception e) {
			throw new TigerstripeException("Error building file ", e);
		}
	}

	public boolean doSchemaElement(Node startNode, String elementNS,
			String elementName) {

		Document elementDoc = schemaMap.get(elementNS); // Doc containting the
		// element Element!
		Element schemaRoot = elementDoc.getDocumentElement();

		NodeList elements = schemaRoot.getElementsByTagName("element");

		// Look for substitution Groups...
		// This might be "sub-optimal"
		// Need to use the prefix here not the full NS
		String elementPrefix = lookupPrefix(elementDoc, elementNS);
		Element substitute = findElementbyAttribute(elements,
				"substitutionGroup", elementPrefix + ":" + elementName);
		if (substitute != null) {
			if (debugging) {
				TigerstripeRuntime
						.logInfoMessage("Substitute for " + elementName + " : "
								+ substitute.getAttribute("name"));
			}
			elementName = substitute.getAttribute("name");
		}
		Element element = findElementbyAttribute(elements, "name", elementName);
		return doSchemaElement(startNode, elementDoc, element);
	}

	/**
	 * 
	 * @param startNode
	 *            is the current node at which to add any new nodes. It is in
	 *            the output.
	 * @param elementDoc
	 * @param element
	 */
	public boolean doSchemaElement(Node startNode, Document elementDoc,
			Element element) {

		// Need to check for Array types (ie do more than one of the same thing)
		int loopCount = 1;
		if (element.hasAttribute("maxOccurs")) {
			if (element.getAttribute("maxOccurs").equals("unbounded")) {
				// Pick a number!
				loopCount = 2;
			} else {
				// Repeat the loop by the number of times defined
				loopCount = Integer.parseInt(element.getAttribute("maxOccurs"));
			}
		}
		if (debugging) {
			TigerstripeRuntime.logInfoMessage("loop " + loopCount);
		}
		for (int loop = 0; loop < loopCount; loop++) {
			Element elementNameElement;
			if (element.hasAttribute("name")) {
				// NB ref elements have no name...
				if (debugging) {
					TigerstripeRuntime.logInfoMessage("Element name = "
							+ element.getAttribute("name"));
				}

				// Prepend the prefix of the defining Document
				elementNameElement = this.outDom
						.createElement(getLocalPrefix(elementDoc) + ":"
								+ element.getAttribute("name"));
				startNode.appendChild(elementNameElement);

				if (!this.runningList.contains(elementDoc)) {
					this.runningList.add(elementDoc);
				}

			} else {
				elementNameElement = (Element) startNode;
			}

			// ElementNodes can have "complexType", "simpleType" child nodes, OR
			// a "type" attribute, OR a "ref" attribute

			boolean worked = false;
			NodeList complexTypes = element.getElementsByTagName("complexType");
			NodeList simpleTypes = element.getElementsByTagName("simpleType");
			if (complexTypes.getLength() > 0) {
				Element complexTypeDef = (Element) complexTypes.item(0);
				// These are local
				worked = doSchemaComplexType(elementNameElement, elementDoc,
						complexTypeDef);
			} else if (simpleTypes.getLength() > 0) {
				Element simpleTypeDef = (Element) simpleTypes.item(0);
				// These are local as well!
				worked = doSchemaSimpleType(elementNameElement, elementDoc,
						simpleTypeDef);
			} else if (element.hasAttribute("type")) {
				String typeName = element.getAttribute("type");
				// This could be elsewhere
				String[] typeNS = getNS(typeName, elementDoc);
				worked = doSchemaGlobalType(elementNameElement, typeNS[0],
						typeNS[1]);
			} else if (element.hasAttribute("ref")) {
				String refName = element.getAttribute("ref");
				// This could be elsewhere
				// but the Ref is just a pointer to an Element, so do that
				// element
				String[] refNS = getNS(refName, elementDoc);
				worked = doSchemaElement(elementNameElement, refNS[0], refNS[1]);
			} else {
				worked = false;
			}
			if (!worked)
				return worked;
		}

		return true;
	}

	public boolean doSchemaGlobalType(Node startNode, String typeNS,
			String typeName) {
		if (debugging) {
			Comment testComment = startNode.getOwnerDocument().createComment(
					"globalTypeString");
			startNode.appendChild(testComment);
		}
		Document typeDoc = schemaMap.get(typeNS); // Doc containing the type
		// Element!

		Element schemaRoot = typeDoc.getDocumentElement();
		// These elements may not be of Element type - could be simple or
		// complexType!
		Element typeElement = null;
		NodeList complexTypeElements = schemaRoot
				.getElementsByTagName("complexType");
		if (complexTypeElements.getLength() > 0) {
			typeElement = findElementbyAttribute(complexTypeElements, "name",
					typeName);
			if (typeElement != null)
				return doSchemaComplexType(startNode, typeDoc, typeElement);
		}
		NodeList simpleTypeElements = schemaRoot
				.getElementsByTagName("simpleType");
		if (simpleTypeElements.getLength() > 0) {
			typeElement = findElementbyAttribute(simpleTypeElements, "name",
					typeName);
			if (typeElement != null)
				return doSchemaSimpleType(startNode, typeDoc, typeElement);
		}
		// Didn't find it
		// Maybe built in type? If it is then give it a value.
		if (debugging) {
			Comment testComment = startNode.getOwnerDocument().createComment(
					"unknown" + typeName);
			startNode.appendChild(testComment);
		}
		if (this.defaults.containsKey(typeName)) {
			Text entry = startNode.getOwnerDocument().createTextNode(
					this.defaults.get(typeName).toString());
			startNode.appendChild(entry);
		}

		return true;

	}

	public boolean doSchemaComplexType(Node startNode, Document definingDoc,
			Element contentDef) {

		// ComplexType can have complexContent (for extensions) sequence, choice
		if (debugging) {
			Comment testComment = startNode.getOwnerDocument().createComment(
					"complexType");
			startNode.appendChild(testComment);

		}

		NodeList complexContent = contentDef
				.getElementsByTagName("complexContent");
		if (complexContent.getLength() > 0) {
			// This indicates an extension.
			Element complexContentDef = (Element) complexContent.item(0);
			NodeList extensions = complexContentDef
					.getElementsByTagName("extension");
			if (extensions.getLength() > 0) {
				Element extension = (Element) extensions.item(0);
				if (extension.hasAttribute("base")) {
					String baseName = extension.getAttribute("base");
					String[] baseNS = getNS(baseName, definingDoc);
					if (!doSchemaGlobalType(startNode, baseNS[0], baseNS[1]))
						return false;
				} else
					// Something wrong with the definition
					return false;

			} else
				// Something wrong with the definition
				return false;
		}

		if (contentDef.getElementsByTagName("choice").getLength() > 0) {
			// Choice
			Element choiceElement = (Element) contentDef.getElementsByTagName(
					"choice").item(0);
			// Pick the first one to include in the example
			NodeList options = choiceElement.getElementsByTagName("element");
			if (options.getLength() > 0) {
				Element choice = (Element) options.item(0);
				// TODO definingDoc might be wrong !
				return doSchemaElement(startNode, definingDoc, choice);
			}

		} else if (contentDef.getElementsByTagName("sequence").getLength() > 0) {
			Element sequence = (Element) contentDef.getElementsByTagName(
					"sequence").item(0);
			NodeList series = sequence.getElementsByTagName("element");
			for (int s = 0; s < series.getLength(); s++) {
				// Do each element of the sequence
				Element sequenceElement = (Element) series.item(s);
				doSchemaElement(startNode, definingDoc, sequenceElement);
			}
		}
		return true;
	}

	public boolean doSchemaSimpleType(Node startNode, Document definingDoc,
			Element contentDef) {
		if (debugging) {
			Comment testComment = startNode.getOwnerDocument().createComment(
					"simpleType");
			startNode.appendChild(testComment);
		}

		// We only use these for Enums, so we can cheat :-)
		NodeList enumerations = contentDef.getElementsByTagName("enumeration");
		if (enumerations.getLength() > 0) {
			// Use the first value
			Element enumeration = (Element) enumerations.item(0);
			String enumVal = enumeration.getAttribute("value");
			Text entry = startNode.getOwnerDocument().createTextNode(enumVal);
			startNode.appendChild(entry);

		}

		return true;
	}

	public String getLocalNameSpace(Element schemaRoot) {
		String alias = "";
		NamedNodeMap defs = schemaRoot.getAttributes();
		// Must have targetNamespace
		String targetNamespace = defs.getNamedItem("targetNamespace")
				.getNodeValue();
		// iterate over the others and find a local alias that atches
		for (int z = 0; z < defs.getLength(); z++) {
			Node def = defs.item(z);
			if (def.getNodeValue().equals(targetNamespace)
					&& !def.getNodeName().equals("targetNamespace")) {
				alias = separateNS(def.getNodeName())[1];
				return alias;
			}
		}
		return targetNamespace;
	}

	/*
	 * Look for the fullNameSpace for a local Alias
	 */
	public String lookupNameSpace(Document schema, String nameSpaceToFind) {
		String alias = "";
		Element schemaRoot = schema.getDocumentElement();
		NamedNodeMap defs = schemaRoot.getAttributes();

		for (int z = 0; z < defs.getLength(); z++) {
			Node def = defs.item(z);
			// TigerstripeRuntime.logInfoMessage(def.getNodeName() + " "+
			// nameSpaceToFind);
			if (def.getNodeName().equals("xmlns:" + nameSpaceToFind)) {
				alias = def.getNodeValue();
				// TigerstripeRuntime.logInfoMessage(alias);
				return alias;
			}
		}
		return nameSpaceToFind;
	}

	public String lookupNameSpace(Element schemaRoot, String nameSpaceToFind) {
		String alias = "";
		NamedNodeMap defs = schemaRoot.getAttributes();

		for (int z = 0; z < defs.getLength(); z++) {
			Node def = defs.item(z);
			if (def.getNodeName().equals(nameSpaceToFind)) {
				alias = def.getNodeValue();
				return alias;
			}
		}
		return nameSpaceToFind;
	}

	/*
	 * Look for the fullNameSpace for a local Alias
	 */
	public String lookupPrefix(Document schema, String prefixToFind) {
		String namespace = "";
		Element schemaRoot = schema.getDocumentElement();
		NamedNodeMap defs = schemaRoot.getAttributes();

		for (int z = 0; z < defs.getLength(); z++) {
			Node def = defs.item(z);
			// TigerstripeRuntime.logInfoMessage(def.getNodeName() + " "+
			// nameSpaceToFind);
			if (!def.getNodeName().equals("targetNamespace")
					&& def.getNodeValue().equals(prefixToFind)) {
				namespace = def.getNodeName();
				namespace = namespace.substring(namespace.indexOf(":") + 1);
				// TigerstripeRuntime.logInfoMessage(namespace);
				return namespace;
			}
		}
		return prefixToFind;
	}

	public String getTarget(Document schema) {
		Element schemaRoot = schema.getDocumentElement();
		NamedNodeMap defs = schemaRoot.getAttributes();
		return schemaRoot.getAttribute("targetNamespace");
	}

	public String getLocalPrefix(Document schema) {
		String prefix = "";
		Element schemaRoot = schema.getDocumentElement();
		NamedNodeMap defs = schemaRoot.getAttributes();
		String target = schemaRoot.getAttribute("targetNamespace");

		for (int z = 0; z < defs.getLength(); z++) {
			Node def = defs.item(z);
			// TigerstripeRuntime.logInfoMessage(def.getNodeName() + " "+
			// nameSpaceToFind);
			if (!def.getNodeName().equals("targetNamespace")
					&& def.getNodeValue().equals(target)) {
				prefix = def.getNodeName();
				prefix = prefix.substring(prefix.indexOf(":") + 1);
				// TigerstripeRuntime.logInfoMessage(namespace);
				return prefix;
			}
		}
		return target;
	}

	public Element findElementbyAttribute(NodeList inList, String attrName,
			String attrValue) {
		// NB only finds first match
		for (int n = 0; n < inList.getLength(); n++) {
			Element element = (Element) inList.item(n);
			if (element.getAttribute(attrName).equals(attrValue))
				return element;
		}
		return null;
	}

	public String[] separateNS(String name) {
		return name.split(":");
	}

	private String[] getNS(String name, Document currentDoc) {
		String[] splitted;
		if (name.contains(":")) {
			splitted = separateNS(name);
			String fullNameSpace = lookupNameSpace(currentDoc, splitted[0]);
			// Now look for type in the corresponding doc
			splitted[0] = fullNameSpace;

		} else {
			// No NS, so put the local one in
			splitted = new String[2];
			splitted[0] = currentDoc.getDocumentElement().getAttribute(
					"targetNamespace");
			splitted[1] = name;

		}
		if (debugging) {
			TigerstripeRuntime.logInfoMessage("Splitting :" + name
					+ " : Returning :" + splitted[0] + " : " + splitted[1]);
		}
		return splitted;
	}

	private void setDefaultsForTypes() {
		java.util.Date date = new Date();
		java.text.Format dateformatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.text.Format timeformatter = new java.text.SimpleDateFormat(
				"HH.mm.ss");
		String s = dateformatter.format(date) + "T"
				+ timeformatter.format(date);

		this.defaults.put("string", "aString");
		this.defaults.put("int", 1);
		this.defaults.put("boolean", true);
		this.defaults.put("dateTime", s);
		this.defaults.put("long", 3);
		this.defaults.put("short", 2);
		this.defaults.put("decimal", 5.0);
		this.defaults.put("float", 5.0);

	}

}
