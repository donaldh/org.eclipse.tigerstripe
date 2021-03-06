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
package org.eclipse.tigerstripe.workbench.internal.api.modules;

import java.util.Date;

public interface IModuleHeader {

	/**
	 * The project name as it was when the module was created.
	 * 
	 * @return
	 */
	public String getOriginalName();

	public String getModuleID();

	public void setModuleID(String moduleID);

	public String getBuild();

	public Date getDate();
}
