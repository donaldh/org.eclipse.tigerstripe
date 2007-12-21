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
package org.eclipse.tigerstripe.core.model.importing.db.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a DB schema
 * 
 * @author Eric Dillon
 * 
 */
public class DatabaseSchema extends DatabaseElement {

	private List<DatabaseTable> tables = new ArrayList<DatabaseTable>();

	public DatabaseSchema(String name) {
		super(name);
	}

	public void addTable(DatabaseTable table) {
		tables.add(table);
	}

	public void removeTable(DatabaseTable table) {
		tables.remove(table);
	}

	public DatabaseTable[] getTables() {
		DatabaseTable[] arr = new DatabaseTable[tables.size()];
		return tables.toArray(arr);
	}
}
