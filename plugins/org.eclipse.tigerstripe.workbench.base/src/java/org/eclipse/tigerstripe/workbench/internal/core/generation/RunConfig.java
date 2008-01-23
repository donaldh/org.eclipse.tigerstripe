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
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * Convienence class to capture values of all selection on the Generation Wizard
 * 
 * @author Eric Dillon
 * 
 */
public class RunConfig {

	private ITigerstripeProgressMonitor monitor;

	private boolean ignoreFacets = "true"
			.equalsIgnoreCase(IProjectDetails.IGNORE_FACETS_DEFAULT);

	private boolean generateModules = "true"
			.equalsIgnoreCase(IProjectDetails.GENERATE_MODULES_DEFAULT);

	private boolean useCurrentFacet = false;

	private boolean mergeFacets = false;

	private boolean processUseCases = "true"
			.equalsIgnoreCase(IProjectDetails.PROCESS_USECASES_DEFAULT);

	private boolean generateReferencedProjects = "true"
			.equalsIgnoreCase(IProjectDetails.GENERATE_REFPROJECTS_DEFAULT);

	private String absoluteOutputDir = null;

	private String processUseCaseExtension = "html";

	private String useCaseXsl = null;

	private boolean useUseCaseXSL = false;

	public RunConfig() {
	}

	public boolean useUseCaseXSL() {
		return this.useUseCaseXSL;
	}

	public void setUseUseCaseXSL(boolean useUseCaseXSL) {
		this.useUseCaseXSL = useUseCaseXSL;
	}

	public String getUseCaseXSL() {
		return this.useCaseXsl;
	}

	public void setUseCaseXSL(String useCaseXsl) {
		this.useCaseXsl = useCaseXsl;
	}

	public String getProcessedUseCaseExtension() {
		return this.processUseCaseExtension;
	}

	public void setProcessedUseCaseExtension(String processUseCaseExtension) {
		this.processUseCaseExtension = processUseCaseExtension;
	}

	public void setMonitor(ITigerstripeProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public ITigerstripeProgressMonitor getMonitor() {
		if (monitor == null)
			monitor = new TigerstripeNullProgressMonitor();
		return monitor;
	}

	public RunConfig(ITigerstripeProject tsProject) {
		try {
			IProjectDetails details = tsProject.getProjectDetails();
			ignoreFacets = "true".equals(details.getProperties().getProperty(
					IProjectDetails.IGNORE_FACETS,
					IProjectDetails.IGNORE_FACETS_DEFAULT));
			generateModules = "true".equals(details.getProperties()
					.getProperty(IProjectDetails.GENERATE_MODULES,
							IProjectDetails.GENERATE_MODULES_DEFAULT));
			mergeFacets = "true".equals(details.getProperties().getProperty(
					IProjectDetails.MERGE_FACETS,
					IProjectDetails.MERGE_FACETS_DEFAULT));
			generateReferencedProjects = "true".equals(details.getProperties()
					.getProperty(IProjectDetails.GENERATE_REFPROJECTS,
							IProjectDetails.GENERATE_REFPROJECTS_DEFAULT));
			processUseCases = "true".equals(details.getProperties()
					.getProperty(IProjectDetails.PROCESS_USECASES,
							IProjectDetails.PROCESS_USECASES_DEFAULT));
			useUseCaseXSL = "true".equals(details.getProperties().getProperty(
					IProjectDetails.USECASE_USEXSLT,
					IProjectDetails.USECASE_USEXSLT_DEFAULT));
			useCaseXsl = details.getProperties().getProperty(
					IProjectDetails.USECASE_XSL,
					IProjectDetails.USECASE_XSL_DEFAULT);
			processUseCaseExtension = details.getProperties().getProperty(
					IProjectDetails.USECASE_PROC_EXT,
					IProjectDetails.USECASE_PROC_EXT_DEFAULT);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	public void setAbsoluteOutputDir(String absoluteOutputDir) {
		this.absoluteOutputDir = absoluteOutputDir;
	}

	public String getAbsoluteOutputDir() {
		return this.absoluteOutputDir;
	}

	public void setMergeFacets(boolean mergeFacets) {
		this.mergeFacets = mergeFacets;
	}

	public boolean isMergeFacets() {
		return this.mergeFacets;
	}

	public void setIgnoreFacets(boolean isIgnoreFacet) {
		this.ignoreFacets = isIgnoreFacet;
	}

	public boolean isProcessUseCases() {
		return this.processUseCases;
	}

	public void setProcessUseCases(boolean processUseCases) {
		this.processUseCases = processUseCases;
	}

	public void setGenerateModules(boolean generateModules) {
		this.generateModules = generateModules;
	}

	public boolean isUseCurrentFacet() {
		return useCurrentFacet;
	}

	public void setUseCurrentFacet(boolean useCurrentFacet) {
		this.useCurrentFacet = useCurrentFacet;
	}

	public boolean isGenerateModules() {
		return generateModules;
	}

	public boolean isIgnoreFacets() {
		return ignoreFacets;
	}

	public boolean isGenerateRefProjects() {
		return generateReferencedProjects;
	}

	public void setGenerateRefProjects(boolean generateRefProjects) {
		this.generateReferencedProjects = generateRefProjects;
	}
}
