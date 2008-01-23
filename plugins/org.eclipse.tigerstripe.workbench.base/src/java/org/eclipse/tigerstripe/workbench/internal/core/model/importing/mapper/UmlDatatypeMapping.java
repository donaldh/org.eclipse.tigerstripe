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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.mapper;

public class UmlDatatypeMapping {

	private String umlDatatype;
	private String mappedDatatype;

	public UmlDatatypeMapping(UmlDatatypeMapping mapping) {
		this.umlDatatype = mapping.getUmlDatatype();
		this.mappedDatatype = mapping.getMappedDatatype();
	}

	public UmlDatatypeMapping() {
		this.umlDatatype = "";
		this.mappedDatatype = "";
	}

	public UmlDatatypeMapping(String umlDatatype, String mappedDatatype) {
		this.umlDatatype = umlDatatype;
		this.mappedDatatype = mappedDatatype;
	}

	public String getMappedDatatype() {
		return mappedDatatype;
	}

	public void setMappedDatatype(String mappedDatatype) {
		this.mappedDatatype = mappedDatatype;
	}

	public String getUmlDatatype() {
		return umlDatatype;
	}

	public void setUmlDatatype(String umlDatatype) {
		this.umlDatatype = umlDatatype;
	}

}
