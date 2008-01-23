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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic model for Database tables
 * 
 * @author Eric Dillon
 * 
 */
public class DatabaseTable extends DatabaseElement {

	private List<TableColumn> columns = new ArrayList<TableColumn>();
	private List<ImportedKey> importedKeys = new ArrayList<ImportedKey>();

	public DatabaseTable(String name) {
		super(name);
	}

	public void addImportedKey(ImportedKey key) {
		this.importedKeys.add(key);
	}

	public ImportedKey[] getImportedKeys() {
		return importedKeys.toArray(new ImportedKey[importedKeys.size()]);
	}

	public void addTableColumn(TableColumn column) {
		columns.add(column);
	}

	public void removeTableColumn(TableColumn column) {
		columns.remove(column);
	}

	public TableColumn getColumnByName(String name) {
		for (TableColumn column : columns) {
			if (column.getName().equals(name))
				return column;
		}
		return null;
	}

	public TableColumn[] getColumns() {
		TableColumn[] arr = new TableColumn[columns.size()];
		return columns.toArray(arr);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof DatabaseTable) {
			DatabaseTable oTable = (DatabaseTable) other;
			return oTable.getName().equals(this.getName());
		} else
			return false;
	}
}
