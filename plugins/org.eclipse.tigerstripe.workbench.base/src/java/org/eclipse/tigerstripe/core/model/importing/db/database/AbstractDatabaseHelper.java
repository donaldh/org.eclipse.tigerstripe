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
package org.eclipse.tigerstripe.core.model.importing.db.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.model.importing.db.schema.ImportedKey;
import org.eclipse.tigerstripe.core.model.importing.db.utils.DatabaseUtil;

/**
 * This class contains a set of helpers to abstract out some of the variability
 * about JDBC connections.
 * 
 * @author Eric Dillon
 * 
 */
public abstract class AbstractDatabaseHelper {

	protected static final String[] DB_TABLE_TYPES = { "TABLE" };

	protected static final String[] DB_VIEW_TYPES = { "VIEW" };

	protected static final String[] DB_MIXED_TYPES = { "TABLE", "VIEW" };

	protected static final String COLUMN_NAME_TABLE_NAME = "TABLE_NAME";

	protected static final String COLUMN_NAME_COLUMN_NAME = "COLUMN_NAME";

	protected static final String COLUMN_NAME_TYPE = "TYPE";

	protected static final String COLUMN_NAME_DATA_TYPE = "DATA_TYPE";

	protected static final String COLUMN_NAME_VIEW_NAME = "VIEW_NAME";

	protected static final String COLUMN_NAME_TYPE_NAME = "TYPE_NAME";

	protected static final String COLUMN_NAME_COLUMN_SIZE = "COLUMN_SIZE";

	protected static final String COLUMN_NAME_NULLABLE = "NULLABLE";

	protected static final String COLUMN_NAME_ORDINAL_POSITION = "ORDINAL_POSITION";

	protected static final String COLUMN_NAME_TABLE_CATALOG = "TABLE_CAT";

	protected static final String COLUMN_NAME_TABLE_SCHEMA = "TABLE_SCHEM";

	protected static final String COLUMN_NAME_PRIVILEGE = "PRIVILEGE";

	protected static final String COLUMN_NAME_GRANTOR = "GRANTOR";

	protected static final String COLUMN_NAME_IS_GRANTABLE = "IS_GRANTABLE";

	protected static final String COLUMN_NAME_GRANTEE = "GRANTEE";

	protected static final String COLUMN_NAME_ASC_OR_DESC = "ASC_OR_DESC";

	protected static final String COLUMN_NAME_CARDINALITY = "CARDINALITY";

	protected static final String COLUMN_NAME_PAGES = "PAGES";

	protected static final String COLUMN_NAME_FILTER_CONDITION = "FILTER_CONDITION";

	protected static final String COLUMN_NAME_NON_UNIQUE = "NON_UNIQUE";

	protected static final String COLUMN_NAME_INDEX_QUALIFIER = "INDEX_QUALIFIER";

	protected static final String COLUMN_NAME_INDEX_NAME = "INDEX_NAME";

	protected static final String COLUMN_NAME_SCOPE = "SCOPE";

	protected static final String COLUMN_NAME_DECIMAL_DIGITS = "DECIMAL_DIGITS";

	protected static final String COLUMN_NAME_PSEUDO_COLUMN = "PSEUDO_COLUMN";

	protected static final String STORED_PROCEDURE_RETURNS_RESULT = "procedureReturnsResult";

	protected static final String STORED_PROCEDURE_NO_RESULT = "procedureNoResult";

	protected static final String STORED_PROCEDURE_RESULT_UNKNOWN = "procedureResultUnknown";

	static final Map JDBC_TYPE_NAME_MAP = new HashMap();
	static {
		// Get all fields in java.sql.Types
		Field[] fields = java.sql.Types.class.getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				// Get field name
				String name = fields[i].getName();

				// Get field value
				Integer value = (Integer) fields[i].get(null);

				// Add to map
				JDBC_TYPE_NAME_MAP.put(value, name);
			} catch (IllegalAccessException e) {
				// ignore
			}
		}
	}

	public class ColumnDetail {
		public String name;

		public String type;

		public int size;

		public boolean nullable;
	}

	public abstract List<String> getTableNames(Connection connection,
			String schemaName) throws TigerstripeException;

	public abstract List<String> getViewNames(Connection connection,
			String schemaName) throws TigerstripeException;

	/**
	 * 
	 * @param connection
	 * @param schemaName
	 * @param tablename
	 * @return
	 * @throws TigerstripeException
	 */
	public List<ColumnDetail> getColumnDetails(Connection connection,
			String schemaName, String tablename) throws TigerstripeException {
		ResultSet rsColumns = null;
		List<ColumnDetail> result = new ArrayList<ColumnDetail>();

		try {
			if ((tablename == null) || (tablename.length() == 0))
				return result;

			DatabaseMetaData meta = connection.getMetaData();
			if (meta == null)
				throw new TigerstripeException("No metadata while extracting "
						+ tablename);

			rsColumns = meta.getColumns(null, schemaName, tablename
					.toUpperCase(), null);
			while (rsColumns.next()) {
				String columnName = rsColumns
						.getString(COLUMN_NAME_COLUMN_NAME);
				String columnType = rsColumns.getString(COLUMN_NAME_TYPE_NAME);
				int size = rsColumns.getInt(COLUMN_NAME_COLUMN_SIZE);
				boolean isNullable = false;
				int nullableInt = rsColumns.getInt(COLUMN_NAME_NULLABLE);
				isNullable = (nullableInt == DatabaseMetaData.columnNullable);

				ColumnDetail detail = new ColumnDetail();
				detail.name = columnName;
				detail.type = columnType;
				detail.size = size;
				detail.nullable = isNullable;
				result.add(detail);
			}
		} catch (Exception e) {
			throw new TigerstripeException(
					"Couldn't extract column details for " + tablename, e);
		} finally {
			DatabaseUtil.close(rsColumns);
		}
		return result;
	}

	/**
	 * Returns a list of schemas for the given connection. Please note that
	 * Schemas for Oracle are Catalogs for MySQL. So, this function maybe be
	 * implemented differently if needed.
	 * 
	 * @param connection
	 * @return
	 * @throws TigerstripeException
	 */
	public List<String> getSchemas(Connection connection)
			throws TigerstripeException {
		ResultSet schemas = null;
		List<String> result = new ArrayList<String>();

		try {
			DatabaseMetaData meta = connection.getMetaData();
			if (meta == null)
				return result;

			schemas = meta.getSchemas();
			while (schemas.next()) {
				String tableSchema = schemas.getString(1); // "TABLE_SCHEM"
				result.add(tableSchema);
			}
		} catch (Exception e) {
			throw new TigerstripeException("Couldn't get schemas:"
					+ e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 
	 * @param connection
	 * @param catalog
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws TigerstripeException
	 */
	public List<ImportedKey> getImportedKeys(Connection connection,
			String catalog, String schema, String tableName)
			throws TigerstripeException {
		ResultSet rs = null;
		List<ImportedKey> result = new ArrayList<ImportedKey>();

		try {
			if ((tableName == null) || (tableName.length() == 0))
				return result;

			DatabaseMetaData meta = connection.getMetaData();
			if (meta == null)
				return result;

			rs = meta.getImportedKeys(catalog, schema, tableName.toUpperCase());
			if (rs == null)
				return result;

			while (rs.next()) {
				String localColumnName = rs.getString(8);
				String targetSchemaName = rs.getString(2);
				String targetTableName = rs.getString(3);
				String targetColumnName = rs.getString(4);
				ImportedKey iKey = new ImportedKey(localColumnName,
						targetSchemaName, targetTableName, targetColumnName);
				result.add(iKey);
			}
		} catch (SQLException e) {
			throw new TigerstripeException(
					"Error while getting imported keys for " + tableName + ": "
							+ e.getMessage(), e);
		} finally {
			DatabaseUtil.close(rs);
		}

		return result;
	}
}
