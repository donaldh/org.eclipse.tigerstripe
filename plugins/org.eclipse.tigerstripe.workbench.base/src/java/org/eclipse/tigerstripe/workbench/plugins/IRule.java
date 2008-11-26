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

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;


/**
 * Top level rule interface for Generators
 * 
 * @author erdillon
 * 
 */
public interface IRule {

	public String  ARTIFACT = "artifact";
	public Class<?> ARTIFACT_CLASS = IAbstractArtifact.class;
	
	public String  PLUGINCONFIG = "pluginConfig";
	public String  PLUGINDIR = "pluginDir";
	public String  RUNTIME = "runtime";
	public String  TSPROJECT = "tsProject";
	public String  TSPROJECTHANDLE = "tsProjectHandle";
	public String  MANAGERSESSION = "managerSession";
	public String  EXP = "exp";
	public String  MANAGER = "manager";
	public String  DIAGRAMGENERATOR = "diagramGenerator";
	public String  ANNOTATIONCONTEXT = "annotationContext";
	
	public String  ARTIFACTS = "artifacts";
	public String  ENTITIES = "entities";
	public String  DATATYPES = "datatypes";
	public String  EVENTS = "events";
	public String  ENUMERATIONS = "enumerations";
	public String  EXCEPTIONS = "exceptions";
	public String  QUERIES = "queries";
	public String  UPDATEPROCEDURES = "updateProcedures";
	public String  ASSOCIATIONS = "associations";
	public String  ASSOCIATIONCLASSES = "associationClasses";
	public String  DEPENDENCIES = "dependencies";
	public String  SESSIONS = "sessions";
	public String  PACKAGES = "packages";

	public String  ALLPLUGINCONFIG = "allPluginConfig";
	public String  ALLARTIFACTS = "allArtifacts";
	public String  ALLENTITIES = "allEntities";
	public String  ALLDATATYPES = "allDatatypes";
	public String  ALLEVENTS = "allEvents";
	public String  ALLENUMERATIONS = "allEnumerations";
	public String  ALLEXCEPTIONS = "allExceptions";
	public String  ALLQUERIES = "allQueries";
	public String  ALLUPDATEPROCEDURES = "allUpdateProcedures";
	public String  ALLASSOCIATIONS = "allAssociations";
	public String  ALLASSOCIATIONCLASSES = "allAssociationClasses";
	public String  ALLDEPENDENCIES = "allDependencies";
	public String  ALLSESSIONS = "allSessions";
	public String  ALLPACKAGES = "allPackages";
	
	// The following items are not always in the context.
	public String  SUPPRESSFILES = "suppressFiles";
	public String  OVERWRITEFILES = "overwriteFiles";
	public String  INCLUDEDEPENDENCIES = "includeDependencies";
	
	public String REPORT = "report";
	
	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public String getLabel();

	public String getType();

	public boolean isEnabled();

	public void setEnabled(boolean enabled);
}
