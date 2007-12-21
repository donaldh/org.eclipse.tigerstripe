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
package org.eclipse.tigerstripe.core.model.importing.db.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.RowSet;

import org.eclipse.tigerstripe.core.TigerstripeRuntime;

public class DatabaseUtil {

	public static void close(RowSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			// ignore
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			// ignore
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}

	public static void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			// ignore
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}

	public static void close(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
			// ignore
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}

	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			// ignore
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}

	public static java.sql.Date getJavaSqlDate() {

		java.util.Date javaUtilDate = new java.util.Date();
		return new java.sql.Date(javaUtilDate.getTime());
	}

	public static String getTrimmedString(ResultSet resultSet, int index)
			throws SQLException {

		String value = resultSet.getString(index);

		if (value != null) {
			value = value.trim();
		}

		return value;
	}

	public static String getTrimmedString(ResultSet resultSet, String columnName)
			throws SQLException {

		String value = resultSet.getString(columnName);

		if (value != null) {
			value = value.trim();
		}

		return value;
	}
}
