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
package org.eclipse.tigerstripe.workbench.plugins;

/**
 * Pluggable plugin nature
 * 
 * Pluggable plugins have a nature that will condition when/how they are being
 * triggered during a generation cycle.
 * 
 * For compatibility reasons with older versions a plugin that has no nature has
 * the "Unknown" nature, and will be treated as a "generic" plugin.
 * 
 * @author erdillon
 * @since 2.2.4
 */
public enum EPluggablePluginNature {

	Generic, // Generic plugin nature (this is the default from 2.2.4 on)
	Validation,
	// A plugin expected to pass/fail that needs to be run before any other
	// plugin
	M0
}
