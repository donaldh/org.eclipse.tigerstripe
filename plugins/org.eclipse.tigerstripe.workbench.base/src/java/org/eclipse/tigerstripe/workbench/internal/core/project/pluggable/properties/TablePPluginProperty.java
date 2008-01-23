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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.plugins.ITablePPluginProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TablePPluginProperty extends BasePPluginProperty implements
		ITablePPluginProperty {

	public final static String LABEL = "Table Property";

	private List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();

	public TablePPluginProperty() {
		// TODO Auto-generated constructor stub
	}

	public List<ColumnDef> getColumnDefs() {
		return this.columnDefs;
	}

	@Override
	public void buildBodyFromNode(Node node) {
		columnDefs.clear();
		if (node instanceof Element) {
			Element elm = (Element) node;
			NodeList list = elm.getElementsByTagName("columnDef");
			for (int i = 0; i < list.getLength(); i++) {
				ColumnDef def = new ColumnDef();
				Element e = (Element) list.item(i);
				def.columnName = e.getAttribute("name");
				def.defaultColumnValue = e.getAttribute("default");
				columnDefs.add(def);
			}
		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		Element result = document.createElement("columns");

		for (ColumnDef def : getColumnDefs()) {
			Element colDef = document.createElement("columnDef");
			colDef.setAttribute("name", def.columnName);
			colDef.setAttribute("default", def.defaultColumnValue);
			result.appendChild(colDef);
		}
		return result;
	}

	public Object deSerialize(String sValue) {
		List<TablePropertyRow> rows = new ArrayList<TablePropertyRow>();
		try {
			SAXReader saxReader = new SAXReader();
			StringReader reader = new StringReader(sValue);
			org.dom4j.Document document = saxReader.read(reader);

			org.dom4j.Element root = document.getRootElement();
			Iterator<org.dom4j.Element> iter = root.elementIterator("row");
			for (; iter.hasNext();) {
				org.dom4j.Element rowElm = iter.next();
				TablePropertyRow row = new TablePropertyRow(this);
				for (String columnName : row.getColumnNames()) {
					String value = rowElm.attributeValue(columnName);
					if (value == null) {
						value = "";
					}
					row.setValue(columnName, value);
				}
				rows.add(row);
			}

		} catch (DocumentException e) {
			TigerstripeRuntime.logErrorMessage("DocumentException detected", e);
		}

		return rows;
	}

	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return ITablePPluginProperty.class.getCanonicalName();
	}

	public String serialize(Object value) {
		if (value instanceof List) {
			List<TablePropertyRow> entries = (List<TablePropertyRow>) value;
			org.dom4j.Document document = DocumentFactory.getInstance()
					.createDocument();
			org.dom4j.Element rootElem = DocumentFactory.getInstance()
					.createElement("entries");
			document.setRootElement(rootElem);

			for (TablePropertyRow row : entries) {
				org.dom4j.Element rowElm = rootElem.addElement("row");
				for (String columnName : row.getColumnNames()) {
					rowElm.addAttribute(columnName, row.getValue(columnName));
				}
			}

			return document.asXML();
		}
		return null;
	}

	@Override
	public Object getDefaultValue() {
		return new ArrayList<TablePropertyRow>();
	}

	public TablePropertyRow makeRow() {
		return new TablePropertyRow(this);
	}
}
