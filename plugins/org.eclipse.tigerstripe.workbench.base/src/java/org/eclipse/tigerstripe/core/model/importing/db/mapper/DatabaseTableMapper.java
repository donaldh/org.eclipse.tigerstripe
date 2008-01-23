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
package org.eclipse.tigerstripe.core.model.importing.db.mapper;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableElementAttribute;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableElementPackage;
import org.eclipse.tigerstripe.core.model.importing.db.schema.DatabaseTable;
import org.eclipse.tigerstripe.core.model.importing.db.schema.ImportedKey;
import org.eclipse.tigerstripe.core.model.importing.db.schema.TableColumn;
import org.eclipse.tigerstripe.core.util.messages.MessageList;

public class DatabaseTableMapper extends DBElementMapper {

	private ColumnMapper columnMapper;

	public DatabaseTableMapper(MessageList messageList,
			ITigerstripeProject targetProject, IModelImportConfiguration config) {
		super(messageList, targetProject, config);
		this.columnMapper = new ColumnMapper(messageList, targetProject, config);
	}

	public DBAnnotableElement mapTableToElement(DatabaseTable table)
			throws TigerstripeException {

		DBAnnotableElement result = new DBAnnotableElement(table.getName());

		// First map all columns
		for (TableColumn column : table.getColumns()) {
			DBAnnotableElementAttribute att = columnMapper
					.mapToAttribute(column);
			result.addAnnotableElementAttribute(att);
		}

		// if any of the mapped columns were in fact an imported key,
		// we'll overwrite it here
		for (ImportedKey key : table.getImportedKeys()) {
			DBAnnotableElementAttribute att = result
					.getAnnotableElementAttributeByName(key
							.getLocalColumnName());
			DBAnnotableDatatype type = new DBAnnotableDatatype();
			type.setAnnotableElementPackage(new DBAnnotableElementPackage(key
					.getTargetSchemaName()));
			type.setName(key.getTargetTableName());
			att.setType(type);
		}
		return result;
	}
}
