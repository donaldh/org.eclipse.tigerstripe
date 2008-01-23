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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.utils.DatabaseUtil;

/**
 * Oracle specifics when the generic JDBC stuff doesn't work properly
 * 
 * @author Eric Dillon
 * @since 1.1
 */
public class OracleDatabaseHelper extends AbstractDatabaseHelper {

	protected static final String ORACLE_VIEWS = "select object_name from user_objects where object_type = 'VIEW'";

	protected static final String ORACLE_TABLES = "select object_name from user_objects where object_type = 'TABLE'";

	protected static final String ORACLE_TABLES_AND_VIEWS = "select object_name from user_objects where object_type = 'TABLE' or object_type = 'VIEW'";

	/**
	 * Gets a list of the tablenames for the given connection
	 * 
	 */
	@Override
	public List<String> getTableNames(Connection connection, String schemaName)
			throws TigerstripeException {
		return internalGetTableOrViewsNames(connection, schemaName,
				ORACLE_TABLES);
	}

	@Override
	public List<String> getViewNames(Connection connection, String schemaName)
			throws TigerstripeException {
		return internalGetTableOrViewsNames(connection, schemaName,
				ORACLE_VIEWS);
	}

	protected List<String> internalGetTableOrViewsNames(Connection connection,
			String schemaName, String sql) throws TigerstripeException {
		Statement stmt = null;
		ResultSet rs = null;

		List<String> result = new ArrayList<String>();

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs == null)
				return result;

			while (rs.next()) {
				String tableName = DatabaseUtil.getTrimmedString(rs, 1);
				result.add(tableName);
			}
		} catch (Exception e) {
			throw new TigerstripeException(
					"Unable to get Tables/views for connection: "
							+ e.getMessage(), e);
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
		}
		return result;
	}
}
