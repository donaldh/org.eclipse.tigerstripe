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

public class ImportedKey {

	private String targetSchemaName;
	private String targetTableName;
	private String targetColumnName;
	private String localColumnName;

	public ImportedKey(String localColumnName, String targetSchemaName,
			String targetTableName, String targetColumnName) {
		this.targetSchemaName = targetSchemaName;
		this.targetTableName = targetTableName;
		this.targetColumnName = targetColumnName;
		this.localColumnName = localColumnName;
	}

	public String getTargetSchemaName() {
		return this.targetSchemaName;
	}

	public String getTargetTableName() {
		return this.targetTableName;
	}

	public String getTargetColumnName() {
		return this.targetColumnName;
	}

	public String getLocalColumnName() {
		return this.localColumnName;
	}

}
