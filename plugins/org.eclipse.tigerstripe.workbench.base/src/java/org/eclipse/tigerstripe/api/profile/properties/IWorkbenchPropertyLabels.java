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
package org.eclipse.tigerstripe.api.profile.properties;

/**
 * This interface contains all the labels of defined properties
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IWorkbenchPropertyLabels {

	/**
	 * @see org.eclipse.tigerstripe.core.profile.properties.CoreArtifactSettingsProperty
	 */
	public final static String CORE_ARTIFACTS_SETTINGS = "artifacts.core.settings";

	/**
	 * @see org.eclipse.tigerstripe.core.profile.properties.OssjLegacySettingsProperty
	 */
	public final static String OSSJ_LEGACY_SETTINGS = "ossj.legacy.settings";

	/**
	 * @see org.eclipse.tigerstripe.core.profile.properties.GlobalSettingsProperty
	 */
	public final static String GLOBAL_SETTINGS = "global.settings";

}
