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
package org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin;

/**
 * Contains the static information about the JVT profile
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IWSDLExampleProfilePlugin extends IVersionAwarePlugin {

	public final static String defaultVersion = VERSION_1_3;

	public final static String[] supportedVersions = { VERSION_1_3 };
}
