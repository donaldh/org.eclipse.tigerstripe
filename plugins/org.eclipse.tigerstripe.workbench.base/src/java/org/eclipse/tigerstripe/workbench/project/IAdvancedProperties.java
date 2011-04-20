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
package org.eclipse.tigerstripe.workbench.project;

/**
 * This interface is a placeholder for all properties that are accessible thru
 * the AdvancedProperties facility in Tigerstripe.
 * 
 * Please note that the default (cross-project) preferences can be accessed thru
 * the Eclipse PreferenceStore
 * 
 * @author Eric Dillon
 * 
 */
public interface IAdvancedProperties {

	// should all rules be overriddedn to execute in "Local mode only"
	public final static String PROP_GENERATION_allRulesLocal = "generation.allRulesLocal";
	
	// should a report be generated upon project generation
	public final static String PROP_GENERATION_GenerateReport = "generation.generateReport";

	// should the messages generated during code generation be logged
	public final static String PROP_GENERATION_LogMessages = "generation.logMessages";

	// Ignore artifact elements that don't have a tigerstripe tag
	public final static String PROP_MISC_IgnoreArtifactElementsWithoutTag = "misc.ignoreArtifactElementsWithoutTag";

	public static final String PROP_IMPORT_USETARGETPROJECTASGUIDE = "modelImport.useTargetProjectAsGuide";

	public final static String PROP_IMPORT_UMLDATATYPE_MAP = "modelImport.uml.datatypeMap";
	public final static String PROP_IMPORT_DBDATATYPE_MAP = "modelImport.db.datatypeMap";

	// =============================================
	// DB Import related config
	public final static String PROP_IMPORT_DB_TABLES = "modelImport.db.importTables";
	public final static String PROP_IMPORT_DB_VIEWS = "modelImport.db.importViews";
}
