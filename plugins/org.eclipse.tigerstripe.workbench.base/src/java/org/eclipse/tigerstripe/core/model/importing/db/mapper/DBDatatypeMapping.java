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

public class DBDatatypeMapping {

	private String dbDatatype;
	private String mappedDatatype;

	public DBDatatypeMapping(DBDatatypeMapping mapping) {
		this.dbDatatype = mapping.getDbDatatype();
		this.mappedDatatype = mapping.getMappedDatatype();
	}

	public DBDatatypeMapping() {
		this.dbDatatype = "";
		this.mappedDatatype = "";
	}

	public DBDatatypeMapping(String dbDatatype, String mappedDatatype) {
		this.dbDatatype = dbDatatype;
		this.mappedDatatype = mappedDatatype;
	}

	public String getMappedDatatype() {
		return mappedDatatype;
	}

	public void setMappedDatatype(String mappedDatatype) {
		this.mappedDatatype = mappedDatatype;
	}

	public String getDbDatatype() {
		return dbDatatype;
	}

	public void setDbDatatype(String umlDatatype) {
		this.dbDatatype = umlDatatype;
	}

}
