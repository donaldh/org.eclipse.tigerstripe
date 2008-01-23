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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.mapper;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.annotables.DBAnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.TableColumn;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class DBTypeMapper extends DBElementMapper {

	private String defaultMapping = "java.lang.Object";

	public final static String DEFAULTMAPPINGS = "<dbDatatypeMappingTable>"
			+ "<defaultMapping>java.lang.Object</defaultMapping>"
			+ "<dbDatatypeMapping dbDatatype=\"NUMBER\" modelType=\"int\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"LONG RAW\" modelType=\"long\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"RAW\" modelType=\"long\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"LONG\" modelType=\"long\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"CHAR\" modelType=\"char\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"FLOAT\" modelType=\"float\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"REAL\" modelType=\"double\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"VARCHAR2\" modelType=\"String\"/>"
			+ "<dbDatatypeMapping dbDatatype=\"DATE\" modelType=\"String\"/>"
			// "<dbDatatypeMapping dbDatatype=\"CLOB\"
			// modelType=\"java.lang.Object\"/>" +
			// "<dbDatatypeMapping dbDatatype=\"DATE\"
			// modelType=\"java.util.Date\"/>" +
			// "<dbDatatypeMapping dbDatatype=\"TIME\"
			// modelType=\"java.util.Date\"/>" +
			// "<dbDatatypeMapping dbDatatype=\"BLOB\"
			// modelType=\"java.lang.Object\"/>" +
			+ "</dbDatatypeMappingTable>";

	// The map is actually stored as an AdvancedProperty (XML-ized) in the
	// project descriptor.
	private HashMap<String, DBDatatypeMapping> datatypeMap = new HashMap<String, DBDatatypeMapping>();

	public DBTypeMapper(MessageList messageList, String initialMap)
			throws TigerstripeException {
		super(messageList, null, null);
		extractMap(initialMap);
	}

	public DBTypeMapper(MessageList messageList,
			ITigerstripeProject targetProject, IModelImportConfiguration config)
			throws TigerstripeException {
		super(messageList, targetProject, config);
		extractMapFromProject();
	}

	public DBAnnotableDatatype getDefaultType() {
		DBAnnotableDatatype result = new DBAnnotableDatatype();
		result.setName(defaultMapping);
		return result;
	}

	public void extractMapFromProject() throws TigerstripeException {

		String xmlizedMap = getTargetProject().getAdvancedProperty(
				IAdvancedProperties.PROP_IMPORT_DBDATATYPE_MAP);
		extractMap(xmlizedMap);
	}

	public void extractMap(String xmlizedMap) throws TigerstripeException {
		if (xmlizedMap == null || "".equals(xmlizedMap)) {
			xmlizedMap = DEFAULTMAPPINGS;
		}
		parseMap(xmlizedMap);
	}

	public void saveMapInProject() throws TigerstripeException {
		if (getTargetProject() != null) {
			String xmlizedMap = xmlizeMap();
			getTargetProject().setAdvancedProperty(
					IAdvancedProperties.PROP_IMPORT_DBDATATYPE_MAP, xmlizedMap);
		}
	}

	public void setMappings(DBDatatypeMapping[] mappings)
			throws TigerstripeException {
		datatypeMap = new HashMap<String, DBDatatypeMapping>();
		for (DBDatatypeMapping mapping : mappings) {
			datatypeMap.put(mapping.getDbDatatype(), mapping);
		}
		saveMapInProject();
	}

	public DBDatatypeMapping[] getMappings() {
		DBDatatypeMapping[] arr = new DBDatatypeMapping[datatypeMap.size()];
		return datatypeMap.values().toArray(arr);
	}

	/**
	 * 
	 * @param datatypeName
	 * @return
	 */
	public DBAnnotableDatatype mapToType(TableColumn column) {

		DBAnnotableDatatype annotableDatatype = new DBAnnotableDatatype();
		String datatypeName = column.getType();

		DBDatatypeMapping mapping = datatypeMap.get(datatypeName);
		if (mapping != null) {
			annotableDatatype.setName(mapping.getMappedDatatype());
			return annotableDatatype;
		} else {
			Message msg = new Message();
			msg.setSeverity(Message.WARNING);
			msg.setMessage("No mapping defined for '" + datatypeName
					+ "'. Used default mapping instead ("
					+ getDefaultType().getFullyQualifiedName()
					+ ") for column '" + column.getName() + "' in table '"
					+ column.getParentTable().getName() + "'.");
			getMessageList().addMessage(msg);
			return getDefaultType();
		}
	}

	protected void parseMap(String xmlizedMap) throws TigerstripeException {
		SAXReader reader = new SAXReader();
		datatypeMap = new HashMap<String, DBDatatypeMapping>();

		try {
			StringReader strReader = new StringReader(xmlizedMap);
			Document document = reader.read(strReader);
			Element root = document.getRootElement();
			defaultMapping = root.elementText("defaultMapping");
			for (Iterator iter = root.elementIterator("dbDatatypeMapping"); iter
					.hasNext();) {
				Element elm = (Element) iter.next();
				String src = elm.attributeValue("dbDatatype");
				String tgt = elm.attributeValue("modelType");
				DBDatatypeMapping mapping = new DBDatatypeMapping(src, tgt);
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

		Element root = document.addElement("dbDatatypeMappingTable");
		Element defaultType = root.addElement("defaultMapping");
		defaultType.addText(defaultMapping);

		for (DBDatatypeMapping mapping : datatypeMap.values()) {
			Element mapElm = root.addElement("dbDatatypeMapping");
			mapElm.addAttribute("dbDatatype", mapping.getDbDatatype());
			mapElm.addAttribute("modelType", mapping.getMappedDatatype());
		}

		return document.asXML();
	}
}
