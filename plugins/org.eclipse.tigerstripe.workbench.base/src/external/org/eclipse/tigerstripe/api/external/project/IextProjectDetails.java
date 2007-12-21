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
package org.eclipse.tigerstripe.api.external.project;

import java.util.Properties;

/**
 * Project details
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IextProjectDetails {

	// ==============================================================
	public String getName();

	public String getVersion();

	public String getDescription();

	public Properties getProperties();

	public String getProperty(String name, String defaultValue);

	public String getOutputDirectory();

	public String getProvider();

}
