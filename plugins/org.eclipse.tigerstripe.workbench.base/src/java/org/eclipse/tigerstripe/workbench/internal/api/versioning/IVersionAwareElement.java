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
package org.eclipse.tigerstripe.workbench.internal.api.versioning;

/**
 * Version aware element
 * 
 * @author Eric Dillon
 * 
 */
public interface IVersionAwareElement extends IBaseElement {

	public final static String DEFAULT_VERSION = "NO_VERSION";

	public String getVersion();

	public void setVersion(String version);
}
