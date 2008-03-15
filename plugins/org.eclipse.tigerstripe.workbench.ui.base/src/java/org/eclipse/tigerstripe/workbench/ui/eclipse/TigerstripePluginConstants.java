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
package org.eclipse.tigerstripe.workbench.ui.eclipse;

public interface TigerstripePluginConstants {

	/**
	 * The plug-in identifier of the Tigerstripe Eclipse plugin (value
	 * <code>"org.eclipse.tigerstripe.eclipse.EclipsePlugin"</code>).
	 */
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.workbench.ui.base"; //$NON-NLS-1$

	public static final String PROJECT_NATURE_ID = PLUGIN_ID
			+ ".tigerstripeProject";

	public static final String OLDPROJECT_NATURE_ID = "com.tigerstripesoftware.workbench.ui.base.tigerstripeProject";

	public static final String PLUGINPROJECT_NATURE_ID = PLUGIN_ID
			+ ".tigerstripePluginProject";

	public static final String M0Generator_NATURE_ID = PLUGIN_ID
			+ ".m0GeneratorProject";

	public static final String OLDPLUGINPROJECT_NATURE_ID = "com.tigerstripesoftware.workbench.ui.base.tigerstripePluginProject";

	public static final String MARKER_ID = PLUGIN_ID + ".auditmarker";

	public static final String OLDPROJECT_BUILDER_ID = "com.tigerstripesoftware.workbench.ui.base.tigerstripeProjectAuditor";

	public static final String OLDPLUGINPROJECT_BUILDER_ID = "com.tigerstripesoftware.workbench.ui.base.pluggablePluginProjectAuditor";
}
