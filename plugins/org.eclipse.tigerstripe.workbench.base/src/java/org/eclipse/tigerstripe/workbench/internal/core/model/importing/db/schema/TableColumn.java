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

/**
 * Basic model for table columns
 * 
 * @author Eric Dillon
 * 
 */
public class TableColumn extends DatabaseElement {

	private String type;
	private DatabaseTable parentTable;

	public TableColumn(String name, String type, int size, boolean nullable) {
		super(name);
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setParentTable(DatabaseTable table) {
		this.parentTable = table;
	}

	public DatabaseTable getParentTable() {
		return this.parentTable;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TableColumn) {
			TableColumn oColumn = (TableColumn) other;
			return oColumn.getName().equals(this.getName());
		} else
			return false;
	}

}
