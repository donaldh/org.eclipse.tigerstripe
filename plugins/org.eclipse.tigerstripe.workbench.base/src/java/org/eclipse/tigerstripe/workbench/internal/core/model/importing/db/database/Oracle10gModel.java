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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.database.AbstractDatabaseHelper.ColumnDetail;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.DatabaseSchema;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.DatabaseTable;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.ImportedKey;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.TableColumn;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class Oracle10gModel extends AbstractDatabaseModel {

	private final static String LABEL = "Oracle 10.2.x (and compatible)";

	@Override
	public AbstractDatabaseModel make() {
		return new Oracle10gModel();
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public DatabaseSchema[] extractSchemas(MessageList list,
			ITigerstripeProject targetProject,
			IModelImportConfiguration config, ModelImporterListener listener)
			throws TigerstripeException {

		OracleDatabaseHelper helper = new OracleDatabaseHelper();

		List<DatabaseSchema> result = new ArrayList<DatabaseSchema>();
		ResultSet rs = null;

		try {
			listener.importBeginTask("Establishing connection to database", 5);
			connect();
			listener.worked(3);
			listener.importBeginTask("Extracting metadata from database", 20);

			List<String> schemaNames = helper.getSchemas(connection);
			for (String schemaName : schemaNames) {
				DatabaseSchema schema = new DatabaseSchema(schemaName);
				List<String> names = new ArrayList<String>();
				if ("true"
						.equalsIgnoreCase(targetProject
								.getAdvancedProperty(IAdvancedProperties.PROP_IMPORT_DB_TABLES))) {
					names.addAll(helper.getTableNames(connection, schemaName));
				}

				if ("true"
						.equalsIgnoreCase(targetProject
								.getAdvancedProperty(IAdvancedProperties.PROP_IMPORT_DB_VIEWS))) {
					names.addAll(helper.getViewNames(connection, schemaName));
				}
				for (String tableName : names) {
					DatabaseTable table = new DatabaseTable(tableName);
					List<ColumnDetail> columnDetails = helper.getColumnDetails(
							connection, schemaName, tableName);
					for (ColumnDetail columnDetail : columnDetails) {
						TableColumn column = new TableColumn(columnDetail.name,
								columnDetail.type, columnDetail.size,
								columnDetail.nullable);
						column.setParentTable(table);
						table.addTableColumn(column);

						List<ImportedKey> keys = helper.getImportedKeys(
								connection, null, schemaName, tableName);
						for (ImportedKey key : keys) {
							table.addImportedKey(key);
						}
					}
					schema.addTable(table);
				}
				result.add(schema);
			}

			listener.worked(10);
			DatabaseSchema[] arr = new DatabaseSchema[result.size()];
			return result.toArray(arr);
		} catch (TigerstripeException e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// ignore, just trying to clean up
				}
			}
			disconnect();
		}
	}

	@Override
	public void connect() throws TigerstripeException {

		try {
			Class.forName(getJdbcDriver());
		} catch (ClassNotFoundException e) {
			throw new TigerstripeException("Unable to load JDBC Driver ("
					+ getJdbcDriver() + "): " + e.getMessage(), e);
		}

		// try {
		throw new TigerstripeException("This funtionality is un-available");
		// OracleDataSource ods = new OracleDataSource();
		//
		// ods.setServerName(getHostname());
		// ods.setDriverType("thin");
		// ods.setPortNumber(Integer.parseInt(getPort()));
		// ods.setDatabaseName(getDbName());
		// if (!"".equals(getUsername())) {
		// ods.setUser(getUsername());
		// }
		// if (!"".equals(getPassword())) {
		// ods.setPassword(getPassword());
		// }
		//
		// connection = ods.getConnection();
		// } catch (SQLException e) {
		// throw new TigerstripeException(
		// "Error while trying to connect to DB: " + e.getMessage(), e);
		// }
	}

	@Override
	public String getDefaultPort() {
		return "1521";
	}

	@Override
	public String getJdbcDriver() {
		return "oracle.jdbc.pool.OracleDataSource";
	}

	@Override
	public String getJdbcUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
