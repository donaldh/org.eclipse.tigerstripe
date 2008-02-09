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
import java.sql.SQLException;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.schema.DatabaseSchema;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * An abstract database model
 * 
 * @author Eric Dillon
 * 
 */
public abstract class AbstractDatabaseModel {

	protected Connection connection;

	public abstract AbstractDatabaseModel make();

	public abstract DatabaseSchema[] extractSchemas(MessageList list,
			ITigerstripeModelProject targetProject,
			IModelImportConfiguration config, ModelImporterListener listener)
			throws TigerstripeException;

	public abstract String getDefaultPort();

	public abstract String getJdbcUrl();

	public abstract String getJdbcDriver();

	public abstract void connect() throws TigerstripeException;

	public void disconnect() throws TigerstripeException {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new TigerstripeException(
						"Error while trying to close connection", e);
			}
			connection = null;
		}
	}

	private String hostname;

	private String port;

	private String dbName;

	private String username;

	private String password;

	public abstract String getLabel();

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
