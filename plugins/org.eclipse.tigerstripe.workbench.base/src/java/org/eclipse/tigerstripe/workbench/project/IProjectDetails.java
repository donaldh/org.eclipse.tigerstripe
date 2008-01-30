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
	/*
	 * the namespace prefix to be used when referencing the common API in
	 * generated XSDs
	 */
	//public final static String OSSJ_COMMON_NAMESPACE_PREFIX = "ossj.common.namespacePrefix";

	/* The target namespace for the OSSJ common schema */
	//public final static String OSSJ_COMMON_TARGET_NAMESPACE = "ossj.common.targetNamespace";

	/* The OSSJ Common schema location */
	//public final static String OSSJ_COMMON_SCHEMA_LOCATION = "ossj.common.schemaLocation";

	/* The default artifact package for newly created artifacts */
	public final static String DEFAULTARTIFACTPACKAGE_PROP = "defaultArtifactPackage";

	/* The copyright notice to put as the header of all generated files */
	public final static String COPYRIGHT_NOTICE = "copyrightNotice";

	/* whether to ignore the facets if any */
	public final static String IGNORE_FACETS = "ignoreFacets";
	public final static String IGNORE_FACETS_DEFAULT = "false";

	/* whether to generate included modules if any */
	public final static String GENERATE_MODULES = "generateModules";
	public final static String GENERATE_MODULES_DEFAULT = "false";

	/* whether to generate referenced projects if any */
	public final static String GENERATE_REFPROJECTS = "generateRefProjects";
	public final static String GENERATE_REFPROJECTS_DEFAULT = "false";

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
	//		OSSJ_COMMON_NAMESPACE_PREFIX, OSSJ_COMMON_TARGET_NAMESPACE,
	//		OSSJ_COMMON_SCHEMA_LOCATION };

	public void setName(String name);

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
	public String getName();

	public String getOutputDirectory();

	public Properties getProperties();

	public String getProperty(String name, String defaultValue);

	public String getProvider();

	public String getVersion();

}
