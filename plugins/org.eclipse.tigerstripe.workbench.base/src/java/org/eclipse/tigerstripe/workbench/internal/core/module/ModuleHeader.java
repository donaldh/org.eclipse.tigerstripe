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
package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.util.Date;

import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;

/**
 * The Module header contains some admin info about the module
 * 
 * @author Eric Dillon
 * 
 */
public class ModuleHeader implements IModuleHeader {

	private String moduleID;
	private String build;
	private Date date;

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public String getModuleID() {
		return this.moduleID;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
