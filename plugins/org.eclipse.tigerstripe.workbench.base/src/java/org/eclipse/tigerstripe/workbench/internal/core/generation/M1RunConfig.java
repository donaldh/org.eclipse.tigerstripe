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
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Convienence class to capture values of all selection on the Generation Wizard
 * 
 * @author Eric Dillon
 * 
 */
public class M1RunConfig extends RunConfig implements IM1RunConfig {

	private boolean clearDirectoryBeforeGenerate = "true"
			.equalsIgnoreCase(IProjectDetails.CLEAR_DIRECTORY_BEFORE_GENERATE_DEFAULT);

	private boolean ignoreFacets = "true".equalsIgnoreCase(IProjectDetails.IGNORE_FACETS_DEFAULT);

	private boolean generateModules = "true".equalsIgnoreCase(IProjectDetails.GENERATE_MODULES_DEFAULT);

	private boolean overrideSubprojectSettings = "true"
			.equalsIgnoreCase(IProjectDetails.OVERRIDE_SUBPROJECT_SETTINGS_DEFAULT);

	private boolean useCurrentFacet = false;

	private boolean useProjectFacets = false;

	private boolean usePluginConfigFacets = false;

	private boolean mergeFacets = false;

	private boolean processUseCases = "true".equalsIgnoreCase(IProjectDetails.PROCESS_USECASES_DEFAULT);

	private boolean generateReferencedProjects = "true".equalsIgnoreCase(IProjectDetails.GENERATE_REFPROJECTS_DEFAULT);

	private String processUseCaseExtension = "html";

	private String useCaseXsl = null;

	private boolean useUseCaseXSL = false;

	private boolean allRulesAsLocal = false;

	protected M1RunConfig() throws TigerstripeException {
		super(null);
	}

	protected M1RunConfig(ITigerstripeModelProject tsProject) throws TigerstripeException {
		super(tsProject);
		if (tsProject != null) {
			initializeFromProject();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#useUseCaseXSL()
	 */
	public boolean useUseCaseXSL() {
		return this.useUseCaseXSL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#setUseUseCaseXSL(
	 * boolean)
	 */
	public void setUseUseCaseXSL(boolean useUseCaseXSL) {
		this.useUseCaseXSL = useUseCaseXSL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#getUseCaseXSL()
	 */
	public String getUseCaseXSL() {
		return this.useCaseXsl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#setUseCaseXSL(
	 * java.lang.String)
	 */
	public void setUseCaseXSL(String useCaseXsl) {
		this.useCaseXsl = useCaseXsl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * getProcessedUseCaseExtension()
	 */
	public String getProcessedUseCaseExtension() {
		return this.processUseCaseExtension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * setProcessedUseCaseExtension(java.lang.String)
	 */
	public void setProcessedUseCaseExtension(String processUseCaseExtension) {
		this.processUseCaseExtension = processUseCaseExtension;
	}

	public boolean isUsePluginConfigFacets() {
		return usePluginConfigFacets;
	}

	public void setUsePluginConfigFacets(boolean usePluginConfigFacets) {
		this.usePluginConfigFacets = usePluginConfigFacets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#setMergeFacets(
	 * boolean)
	 */
	public void setMergeFacets(boolean mergeFacets) {
		this.mergeFacets = mergeFacets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#isMergeFacets()
	 */
	public boolean isMergeFacets() {
		return this.mergeFacets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#setIgnoreFacets(
	 * boolean)
	 */
	public void setIgnoreFacets(boolean isIgnoreFacet) {
		this.ignoreFacets = isIgnoreFacet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * setClearDirectoryBeforeGenerate(boolean)
	 */
	public void setClearDirectoryBeforeGenerate(boolean clearDirectoryBeforeGenerate) {
		this.clearDirectoryBeforeGenerate = clearDirectoryBeforeGenerate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#isProcessUseCases
	 * ()
	 */
	public boolean isProcessUseCases() {
		return this.processUseCases;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * setProcessUseCases(boolean)
	 */
	public void setProcessUseCases(boolean processUseCases) {
		this.processUseCases = processUseCases;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * setGenerateModules(boolean)
	 */
	public void setGenerateModules(boolean generateModules) {
		this.generateModules = generateModules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#isUseCurrentFacet
	 * ()
	 */
	public boolean isUseCurrentFacet() {
		return useCurrentFacet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * setUseCurrentFacet(boolean)
	 */
	public void setUseCurrentFacet(boolean useCurrentFacet) {
		this.useCurrentFacet = useCurrentFacet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#isGenerateModules
	 * ()
	 */
	public boolean isGenerateModules() {
		return generateModules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.generation.IRunConfig#isIgnoreFacets()
	 */
	public boolean isIgnoreFacets() {
		return ignoreFacets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * isClearDirectoryBeforeGenerate()
	 */
	public boolean isClearDirectoryBeforeGenerate() {
		return clearDirectoryBeforeGenerate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * isGenerateRefProjects()
	 */
	public boolean isGenerateRefProjects() {
		return generateReferencedProjects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.workbench.generation.IRunConfig#
	 * setGenerateRefProjects(boolean)
	 */
	public void setGenerateRefProjects(boolean generateRefProjects) {
		this.generateReferencedProjects = generateRefProjects;
	}

	public boolean isUseProjectFacets() {
		return useProjectFacets;
	}

	public void setUseProjectFacets(boolean useProjectFacets) {
		this.useProjectFacets = useProjectFacets;
	}

	public void setAllRulesAsLocal(boolean allRulesAsLocal) {
		this.allRulesAsLocal = allRulesAsLocal;

	}

	public boolean isAllRulesAsLocal() {
		return allRulesAsLocal;
	}

	@Override
	protected void initializeFromProject() throws TigerstripeException {
		super.initializeFromProject();

		// Clone all the M1-Level Plugin Configs
		setPluginConfigs(M1GenerationUtils.m1PluginConfigs(getTargetProject(), false, true));

		IProjectDetails details = getTargetProject().getProjectDetails();
		clearDirectoryBeforeGenerate = "true"
				.equals(details.getProperties().getProperty(IProjectDetails.CLEAR_DIRECTORY_BEFORE_GENERATE,
						IProjectDetails.CLEAR_DIRECTORY_BEFORE_GENERATE_DEFAULT));
		ignoreFacets = "true".equals(details.getProperties().getProperty(IProjectDetails.IGNORE_FACETS,
				IProjectDetails.IGNORE_FACETS_DEFAULT));
		generateModules = "true".equals(details.getProperties().getProperty(IProjectDetails.GENERATE_MODULES,
				IProjectDetails.GENERATE_MODULES_DEFAULT));
		mergeFacets = "true".equals(details.getProperties().getProperty(IProjectDetails.MERGE_FACETS,
				IProjectDetails.MERGE_FACETS_DEFAULT));
		generateReferencedProjects = "true".equals(details.getProperties()
				.getProperty(IProjectDetails.GENERATE_REFPROJECTS, IProjectDetails.GENERATE_REFPROJECTS_DEFAULT));
		processUseCases = "true".equals(details.getProperties().getProperty(IProjectDetails.PROCESS_USECASES,
				IProjectDetails.PROCESS_USECASES_DEFAULT));
		useUseCaseXSL = "true".equals(details.getProperties().getProperty(IProjectDetails.USECASE_USEXSLT,
				IProjectDetails.USECASE_USEXSLT_DEFAULT));
		useCaseXsl = details.getProperties().getProperty(IProjectDetails.USECASE_XSL,
				IProjectDetails.USECASE_XSL_DEFAULT);
		processUseCaseExtension = details.getProperties().getProperty(IProjectDetails.USECASE_PROC_EXT,
				IProjectDetails.USECASE_PROC_EXT_DEFAULT);
		allRulesAsLocal = "true"
				.equals(getTargetProject().getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_allRulesLocal));

		if (!ignoreFacets) {
			// By default use project level facets although pluginConfig
			// facets have precedence.
			if (getTargetProject().getFacetReferences() != null
					&& getTargetProject().getFacetReferences().length != 0) {
				useProjectFacets = true;
			}

			// but if there are any plugin facet defined, they would
			// have precedence
			IPluginConfig[] configs = getTargetProject().getPluginConfigs();
			for (IPluginConfig config : configs) {
				if (config.getFacetReference() != null) {
					usePluginConfigFacets = true;
					useProjectFacets = false;
					break;
				}
			}
		}
	}

	public boolean isOverrideSubprojectSettings() {
		return this.overrideSubprojectSettings;
	}

	public void setOverrideSubprojectSettings(boolean overrideSubprojectSettings) {
		this.overrideSubprojectSettings = overrideSubprojectSettings;

	}
}
