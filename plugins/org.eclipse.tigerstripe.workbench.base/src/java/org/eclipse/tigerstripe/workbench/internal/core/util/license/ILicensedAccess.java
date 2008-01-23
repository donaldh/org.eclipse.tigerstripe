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
package org.eclipse.tigerstripe.workbench.internal.core.util.license;

public interface ILicensedAccess {

	// Eclipse related keys
	public final static String TS_UI_ECLIPSE = "ts.ui.eclipse";
	public final static String TS_UI_ECLIPSE_LABEL = "Tigerstripe Workbench for Eclipse";

	// CLI Related keys
	public final static String TS_CLI = "ts.cli";
	public final static String TS_CLI_LABEL = "Tigerstrile CLI";

	// target Project types
	public final static String TS_PROJECT_OSSJ = "ts.project.ossj";
	public final static String TS_PROJECT_OSSJ_LABEL = "Support for OSS/J Projects";

	// target plugins
	public final static String TS_PLUGIN_OSSJ_JVT = "ts.plugin.ossj.jvt";
	public final static String TS_PLUGIN_OSSJ_JVT_LABEL = "J2EE Profile for OSS/J";

}
