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

import java.util.Properties;

/**
 * Project details
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IProjectDetails  {

	// These are the properties stored within the project details

	/* The default artifact package for newly created artifacts */
	public final static String DEFAULTARTIFACTPACKAGE_PROP = "defaultArtifactPackage";

	/* The copyright notice to put as the header of all generated files */
	public final static String COPYRIGHT_NOTICE = "copyrightNotice";

	/* whether to clear the directory when generating */
	public final static String CLEAR_DIRECTORY_BEFORE_GENERATE = "clearDirectoryBeforeGenerate";
	public final static String CLEAR_DIRECTORY_BEFORE_GENERATE_DEFAULT = "false";
	
	/* whether to ignore the facets if any */
	
	public final static String IGNORE_FACETS = "ignoreFacets";
	public final static String IGNORE_FACETS_DEFAULT = "false";

	/* whether to generate included modules if any */
	public final static String GENERATE_MODULES = "generateModules";
	public final static String GENERATE_MODULES_DEFAULT = "false";

	/* whether to generate referenced projects if any */
	public final static String GENERATE_REFPROJECTS = "generateRefProjects";
	public final static String GENERATE_REFPROJECTS_DEFAULT = "false";
	
	/* whether to override plugin settings for referenced projects and included modules */
	public final static String OVERRIDE_SUBPROJECT_SETTINGS = "overrideSubProjectSettings";
	public final static String OVERRIDE_SUBPROJECT_SETTINGS_DEFAULT = "true";

	/* whether to merge all selected facets in one for a run */
	public final static String MERGE_FACETS = "mergeFacets";
	public final static String MERGE_FACETS_DEFAULT = "false";

	/* whether to process use cases during a run */
	public final static String PROCESS_USECASES = "processUseCases";
	public final static String PROCESS_USECASES_DEFAULT = "false";

	/* whether to process use cases during a run */
	public final static String USECASE_USEXSLT = "useCaseUseXslt";
	public final static String USECASE_USEXSLT_DEFAULT = "false";

	/* whether to process use cases during a run */
	public final static String USECASE_XSL = "useCaseXsl";
	public final static String USECASE_XSL_DEFAULT = "";

	/* whether to process use cases during a run */
	public final static String USECASE_PROC_EXT = "processedUsecaseExtension";
	public final static String USECASE_PROC_EXT_DEFAULT = "html";

	// ==============================================================

	// ==============================================================
	/* Mandatory values are checked for existence by the Auditor */
	public final static String[] MANDATORY_PROPERTIES = { };

	public void setVersion(String version);

	public void setDescription(String description);

	/**
	 * Sets the output dir relative to the descriptor location.
	 * 
	 * @param dir
	 */
	public void setProjectOutputDirectory(String dir);

	public String getProjectOutputDirectory();

	public void setProvider(String provider);

	public String getDescription();

	// ==============================================================

	public String getOutputDirectory();

	public Properties getProperties();

	public String getProperty(String name, String defaultValue);

	public String getProvider();

	public String getVersion();

}
