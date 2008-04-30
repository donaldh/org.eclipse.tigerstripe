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
package org.eclipse.tigerstripe.workbench.internal.api;

import java.io.File;

/**
 * Single spot to store all constants.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface ITigerstripeConstants {

	public final static String PROJECT_DESCRIPTOR = "tigerstripe.xml";

	/**
	 * Tigerstripe Plugin descriptor
	 */
	public final static String PLUGIN_DESCRIPTOR = "ts-plugin.xml";
	
	public final static String M0_GENERATOR_DESCRIPTOR = "m0-generator.xml";

	public final static String PHANTOMSRC_ECLIPSECLASSPATH_VARIABLE = "TIGERSTRIPE_ARTIFACT_BASE";

	public final static String PHANTOMLIB = "PROFILE_ARTIFACTS_LIB";

	public final static String PHANTOMLIB_DEF = File.separator + "lib"
			+ File.separator + "phantom.zip";

	public final static String BASEPRIMITIVE_PACKAGE = "primitive";

	public final static String EXTERNALAPI_LIB = "Tigerstripe External API";

	public final static String INTERNALAPI_LIB = "Tigerstripe Internal API";
	
	public final static String EQUINOX_COMMON = "Equinox Common";

	// TODO remove these 2 defs @see #299
//	public final static String LEGACYCOREOSSJ_LIB = "Legacy Core Artifacts";
//
//	public final static String LEGACYCOREOSSJ_DEF = File.separator
//			+ "src/compatibility/1.1.x/distrib.core.model-1.0.6.jar";
}
