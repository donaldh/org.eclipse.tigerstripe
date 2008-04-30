/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder;

/**
 * This interface simply gathers a few constants related to Tigerstripe Builders
 * 
 * Note: for compatibility reasons, we kept the "old" prefix even though it is
 * not in "ui.base" anymore.
 * 
 * @author erdillon
 * 
 */
public interface BuilderConstants {

	public static final String PREFIX = "org.eclipse.tigerstripe.workbench.base"; //$NON-NLS-1$

	public static final String PROJECT_NATURE_ID = PREFIX
			+ ".tigerstripeProject"; //$NON-NLS-1$

	public static final String PROJECT_BUILDER_ID = PREFIX
			+ ".tigerstripeProjectAuditor"; //$NON-NLS-1$

	public static final String[] OLDPROJECT_NATURE_IDs = {
			"com.tigerstripesoftware.workbench.ui.base.tigerstripeProject", //$NON-NLS-1$
			"org.eclipse.tigerstripe.workbench.ui.base.tigerstripeProject" //$NON-NLS-1$
	};

	public static final String PLUGINPROJECT_NATURE_ID = PREFIX
			+ ".tigerstripePluginProject"; //$NON-NLS-1$

	public static final String PLUGINPROJECT_BUILDER_ID = PREFIX
			+ ".pluggablePluginProjectAuditor"; //$NON-NLS-1$

	public static final String M0Generator_NATURE_ID = PREFIX
			+ ".m0GeneratorProject"; //$NON-NLS-1$

	public static final String[] OLDPLUGINPROJECT_NATURE_IDs = {
			"com.tigerstripesoftware.workbench.ui.base.tigerstripePluginProject", //$NON-NLS-1$
			"org.eclipse.tigerstripe.workbench.ui.base.tigerstripePluginProject", //$NON-NLS-1$
	};

	public static final String MARKER_ID = PREFIX + ".auditmarker"; //$NON-NLS-1$

	public static final String[] OLDPROJECT_BUILDER_IDs = { 
		"com.tigerstripesoftware.workbench.ui.base.tigerstripeProjectAuditor", //$NON-NLS-1$
		"org.eclipse.tigerstripe.workbench.ui.base.tigerstripeProjectAuditor", //$NON-NLS-1$
	};

	public static final String[] OLDPLUGINPROJECT_BUILDER_IDs = { 
		"com.tigerstripesoftware.workbench.ui.base.pluggablePluginProjectAuditor", //$NON-NLS-1$
		"org.eclipse.tigerstripe.workbench.ui.base.pluggablePluginProjectAuditor", //$NON-NLS-1$
	};

}
