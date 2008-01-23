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
package org.eclipse.tigerstripe.core.model.importing.mapper;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IAdvancedProperties;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.messages.MessageList;

/**
 * Maps datatypes as usual found in XMIs into Java datatypes
 * 
 * @author Eric Dillon
 * 
 */
public class UmlDatatypeMapper extends UmlElementMapper {

	public final static String DEFAULTMAPPINGS = "<umlDatatypeMappingTable>"
			+ "<defaultMapping>java.lang.Object</defaultMapping>"
			+ "<umlDatatypeMapping umlDatatype=\"Integer\" modelType=\"int\"/>"
			+ "<umlDatatypeMapping umlDatatype=\"Boolean\" modelType=\"boolean\"/>"
			+ "<umlDatatypeMapping umlDatatype=\"String\" modelType=\"String\"/>"
			+ "<umlDatatypeMapping umlDatatype=\"Float\" modelType=\"float\"/>"
			+ "<umlDatatypeMapping umlDatatype=\"Double\" modelType=\"double\"/>"
			+ "<umlDatatypeMapping umlDatatype=\"Char\" modelType=\"char\"/>"
			+ "</umlDatatypeMappingTable>";

	// The target project used to get/set the map
	private ITigerstripeProject targetProject;

	// The default mapping used when no mapping is found
	private String defaultMapping;

	// The map is actually stored as an AdvancedProperty (XML-ized) in the
	// project descriptor.
	private HashMap<String, UmlDatatypeMapping> datatypeMap = new HashMap<String, UmlDatatypeMapping>();

	public UmlDatatypeMapper(String initialMap) throws TigerstripeException {
		super(null);
		extractMap(initialMap);
	}

	/**
	 * 
	 * @param xmlizedMap
	 *            the XML-ized datatype map as stored as an AdvancedProperty on
	 *            the project
	 */
	public UmlDatatypeMapper(ITigerstripeProject targetProject)
			throws TigerstripeException {
		this(null, targetProject);
	}

	public UmlDatatypeMapper(MessageList list, ITigerstripeProject targetProject)
			throws TigerstripeException {
		super(list);
		this.targetProject = targetProject;
		extractMapFromProject();
	}

	public void extractMapFromProject() throws TigerstripeException {

		String xmlizedMap = targetProject
				.getAdvancedProperty(IAdvancedProperties.PROP_IMPORT_UMLDATATYPE_MAP);
		extractMap(xmlizedMap);
	}

	public void extractMap(String xmlizedMap) throws TigerstripeException {
		if (xmlizedMap == null || "".equals(xmlizedMap)) {
			xmlizedMap = DEFAULTMAPPINGS;
		}
		parseMap(xmlizedMap);
	}

	public void saveMapInProject() throws TigerstripeException {
		if (targetProject != null) {
			String xmlizedMap = xmlizeMap();
			targetProject
					.setAdvancedProperty(
							IAdvancedProperties.PROP_IMPORT_UMLDATATYPE_MAP,
							xmlizedMap);
		}
	}

	public void setMappings(UmlDatatypeMapping[] mappings)
			throws TigerstripeException {
		datatypeMap = new HashMap<String, UmlDatatypeMapping>();
		for (UmlDatatypeMapping mapping : mappings) {
			datatypeMap.put(mapping.getUmlDatatype(), mapping);
		}
		saveMapInProject();
	}

	// public void addUmlDatatypeMapping(UmlDatatypeMapping mapping)
	// throws TigerstripeException {
	// if (datatypeMap.containsKey(mapping.getUmlDatatype())) {
	// throw new TigerstripeException("Duplicate mapping ("
	// + mapping.getUmlDatatype() + ")");
	// } else {
	// datatypeMap.put(mapping.getUmlDatatype(), mapping);
	// saveMapInProject();
	// }
	// }
	//
	// public void removeUmlDatatypeMapping(UmlDatatypeMapping mapping)
	// throws TigerstripeException {
	// if (!datatypeMap.containsKey(mapping.getUmlDatatype())) {
	// throw new TigerstripeException("Unknown mapping ("
	// + mapping.getUmlDatatype() + ").");
	// } else {
	// datatypeMap.remove(mapping.getUmlDatatype());
	// saveMapInProject();
	// }
	// }
	//
	public UmlDatatypeMapping[] getMappings() {
		UmlDatatypeMapping[] arr = new UmlDatatypeMapping[datatypeMap.size()];
		return datatypeMap.values().toArray(arr);
	}

	/**
	 * Maps the given UmlDatatype to the corresponding Java Datatype
	 * 
	 * @param datatype
	 * @return
	 * @throws MappingException
	 */
	// public String mapUmlDatatypeToJavaDatatype(DataType umlDatatype,
	// AnnotableDatatype annotableDatatype) {
	//
	// String datatypeName = umlDatatype.getName();
	//
	// UmlDatatypeMapping mapping = datatypeMap.get(datatypeName);
	// if (mapping != null) {
	// annotableDatatype.setName(mapping.getMappedDatatype());
	// return mapping.getMappedDatatype();
	// } else {
	//
	// // need to default here.
	// Message msg = new Message();
	// String cleanedName = removeIllegalCharacters(datatypeName);
	//
	// msg.setMessage("Type " + datatypeName
	// + " defined as a UML:Datatype. Mapping to " + cleanedName
	// + " in the model.");
	// msg.setSeverity(Message.WARNING);
	// getMessageList().addMessage(msg);
	//
	// annotableDatatype.setName(cleanedName);
	// return cleanedName;
	// }
	// }
	public String mapUmlDatatypeToJavaDatatype(String umlType) {
		UmlDatatypeMapping mapping = datatypeMap.get(umlType);
		if (mapping != null)
			return mapping.getMappedDatatype();
		else
			return umlType;
	}

	protected void parseMap(String xmlizedMap) throws TigerstripeException {
		SAXReader reader = new SAXReader();
		datatypeMap = new HashMap<String, UmlDatatypeMapping>();

		try {
			StringReader strReader = new StringReader(xmlizedMap);
			Document document = reader.read(strReader);
			Element root = document.getRootElement();
			defaultMapping = root.elementText("defaultMapping");
			for (Iterator iter = root.elementIterator("umlDatatypeMapping"); iter
					.hasNext();) {
				Element elm = (Element) iter.next();
				String src = elm.attributeValue("umlDatatype");
				String tgt = elm.attributeValue("modelType");
				UmlDatatypeMapping mapping = new UmlDatatypeMapping(src, tgt);
				datatypeMap.put(src, mapping);
			}

		} catch (DocumentException e) {
			TigerstripeRuntime.logErrorMessage("DocumentException detected", e);
			throw new TigerstripeException("Invalid UmlDatatype mapping table",
					e);
		}
	}

	public String xmlizeMap() throws TigerstripeException {
		Document document = DocumentHelper.createDocument();

		Element root = document.addElement("umlDatatypeMappingTable");
		Element defaultType = root.addElement("defaultMapping");
		defaultType.addText(defaultMapping);

		for (UmlDatatypeMapping mapping : datatypeMap.values()) {
			Element mapElm = root.addElement("umlDatatypeMapping");
			mapElm.addAttribute("umlDatatype", mapping.getUmlDatatype());
			mapElm.addAttribute("modelType", mapping.getMappedDatatype());
		}

		return document.asXML();
	}
}
