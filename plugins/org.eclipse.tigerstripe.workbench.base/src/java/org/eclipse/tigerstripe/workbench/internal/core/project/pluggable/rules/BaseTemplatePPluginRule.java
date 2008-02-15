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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.TigerstripePluginProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactNoFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.internal.core.util.VelocityContextUtil;
import org.eclipse.tigerstripe.workbench.plugins.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateRunRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class BaseTemplatePPluginRule extends BasePPluginRule implements
		ITemplateRunRule {

	public BaseTemplatePPluginRule() {
		super();
		contextDefinitions = new ArrayList<VelocityContextDefinition>();
		macroLibraries = new ArrayList<String>();
	}

	private VelocityContext defaultVContext;

	private List<String> macroLibraries;

	private List<VelocityContextDefinition> contextDefinitions;

	private String template = "";

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
		markProjectDirty();
	}

	private String outputFile = "";

	public String getOutputFile() {
		return this.outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
		markProjectDirty();
	}

	/**
	 * Initializes the Velocity framework and sets it up with a classpath
	 * loader.
	 * 
	 * NOTE: Velocity.init only works the first time - subsequen inits are
	 * ignored- Therefore can't out any specific behaviour in here!
	 * 
	 * @throws Exception,
	 *             if the class loader cannot be set up
	 */
	protected VelocityEngine setClasspathLoaderForVelocity() throws Exception {

		VelocityEngine result = new VelocityEngine();

		Properties properties = new Properties();
		properties.put("resource.loader", "file, class");

		// So we can still access templates from the classpath
		properties
				.put("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		// To access templates from the file system.
		properties
				.put("file.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		properties.put("file.resource.loader.path",
				((TigerstripePluginProjectHandle) getProject()).getPPProject()
						.getBaseDir().getCanonicalPath());
		// properties.put("file.resource.loader.path",
		// "/TEMP/plugins/.Plug-1.0_temp");

		properties.put("file.resource.loader.cache", "true");
		properties.put("file.resource.loader.modificationCheckInterval", "2");

		properties.put("velocimacro.permissions.allow.inline", "true");
		properties.put(
				"velocimacro.permissions.allow.inline.to.replace.global",
				"true");
		// properties.put("velocimacro.permissions.allow.inline.local.scope","true");

		// comment out for runtime !
		// properties.put("velocimacro.library.autoreload","true");
		// Comment out for Dev purposes!
		properties.put("class.resource.loader.cache", "true");

		// properties.put("velocimacro.library","org/eclipse/tigerstripe/workbench/internal/core/plugin/ossj/resources/lib/Velocimacros.vm");
		// The above line would allow for macros - but due the the "once only"
		// nature of init, this is not much use
		// the way we have it configured at present.

		if (hasMacroLibrary()) {
			String libraryList = "";
			String comma = "";
			for (int i = 0; i < this.macroLibraries.size(); i++) {
				libraryList = libraryList + comma + macroLibraries.get(i);
				comma = ",";
			}
			properties.put("velocimacro.library", libraryList);
		}

		result.init(properties);
		return result;
	}

	/**
	 * Returns the default velocity context.
	 * 
	 * @return VelocityContext - the default context
	 */
	protected VelocityContext getDefaultContext(
			PluggablePluginConfig pluginConfig, IPluginRuleExecutor exec)
			throws TigerstripeException {
		if (this.defaultVContext == null) {
			this.defaultVContext = new VelocityContext();
			VelocityContextUtil util = new VelocityContextUtil();
			this.defaultVContext.put("util", util);
		}

		// TODO allow to reference a filter from Use-defined java object
		ArtifactFilter filter = new ArtifactNoFilter();

		ITigerstripeModelProject handle = pluginConfig.getProjectHandle();
		// (ITigerstripeProject) API
		// .getDefaultProjectSession().makeTigerstripeProject(
		// pluginConfig.getProject().getBaseDir().toURI(), null);
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
				.getArtifactManagerSession();
		ArtifactManager artifactMgr = session.getArtifactManager();

		// Let's put what we'll need in the context and get going
		Collection entities = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(ManagedEntityArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection datatypes = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(DatatypeArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection events = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(EventArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection enums = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(EnumArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection exceptions = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(ExceptionArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection queries = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(QueryArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection sessions = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(SessionFacadeArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection updateProcedures = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(UpdateProcedureArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection associations = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(AssociationArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection dependencies = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(DependencyArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		Collection associationClasses = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(AssociationClassArtifact.MODEL, false,
						new NullProgressMonitor()), filter);

		// Bug 928: removed ArtifactFilter that was used to filter all lists
		// below
		// since the facet is now propagated to all dependencies/referenced
		// projects
		Collection allEntities = artifactMgr.getArtifactsByModel(
				ManagedEntityArtifact.MODEL, true, false,
				new NullProgressMonitor());

		Collection allDatatypes = artifactMgr.getArtifactsByModel(
				DatatypeArtifact.MODEL, true, false, new NullProgressMonitor());

		Collection allEvents = artifactMgr.getArtifactsByModel(
				EventArtifact.MODEL, true, false, new NullProgressMonitor());

		Collection allEnums = artifactMgr.getArtifactsByModel(
				EnumArtifact.MODEL, true, false, new NullProgressMonitor());

		Collection allExceptions = artifactMgr
				.getArtifactsByModel(ExceptionArtifact.MODEL, true, false,
						new NullProgressMonitor());

		Collection allQueries = artifactMgr.getArtifactsByModel(
				QueryArtifact.MODEL, true, false, new NullProgressMonitor());

		Collection allSessions = artifactMgr.getArtifactsByModel(
				SessionFacadeArtifact.MODEL, true, false,
				new NullProgressMonitor());

		Collection allUpdateProcedures = artifactMgr.getArtifactsByModel(
				UpdateProcedureArtifact.MODEL, true, false,
				new NullProgressMonitor());

		Collection allAssociations = artifactMgr.getArtifactsByModel(
				AssociationArtifact.MODEL, true, false,
				new NullProgressMonitor());

		Collection allDependencies = artifactMgr.getArtifactsByModel(
				DependencyArtifact.MODEL, true, false,
				new NullProgressMonitor());

		Collection allAssociationClasses = artifactMgr.getArtifactsByModel(
				AssociationClassArtifact.MODEL, true, false,
				new NullProgressMonitor());

		Collection artifacts = artifactMgr.getAllArtifacts(false,
				new NullProgressMonitor());
		Collection allArtifacts = artifactMgr.getAllArtifacts(true, false,
				new NullProgressMonitor());

		defaultVContext.put("artifacts", artifacts);
		defaultVContext.put("allArtifacts", allArtifacts);

		defaultVContext.put("entities", entities);
		defaultVContext.put("datatypes", datatypes);
		defaultVContext.put("events", events);
		defaultVContext.put("enumerations", enums);
		defaultVContext.put("exceptions", exceptions);
		defaultVContext.put("queries", queries);
		defaultVContext.put("updateProcedures", updateProcedures);
		defaultVContext.put("associations", associations);
		defaultVContext.put("associationClasses", associationClasses);
		defaultVContext.put("dependencies", dependencies);
		defaultVContext.put("sessions", sessions);

		defaultVContext.put("allEntities", allEntities);
		defaultVContext.put("allDatatypes", allDatatypes);
		defaultVContext.put("allEvents", allEvents);
		defaultVContext.put("allEnumerations", allEnums);
		defaultVContext.put("allExceptions", allExceptions);
		defaultVContext.put("allQueries", allQueries);
		defaultVContext.put("allUpdateProcedures", allUpdateProcedures);
		defaultVContext.put("allAssociations", allAssociations);
		defaultVContext.put("allAssociationClasses", allAssociationClasses);
		defaultVContext.put("allDependencies", allDependencies);
		defaultVContext.put("allSessions", allSessions);

		defaultVContext.put("pluginConfig", pluginConfig);
		defaultVContext.put("runtime", TigerstripeRuntime.getInstance());

		// This should eventually get removed as TigerstripeProject is not in
		// the API
		defaultVContext.put("tsProject", pluginConfig.getProject());
		defaultVContext.put("exp", new Expander(pluginConfig));
		defaultVContext.put("manager", artifactMgr);

		defaultVContext.put("tsProjectHandle", handle);
		defaultVContext.put("managerSession", session);
		defaultVContext.put("pluginDir", getProject().getLocation().toFile());

		return this.defaultVContext;
	}

	// protected VelocityContext getRule

	protected File getOutputFile(PluggablePluginConfig pluginConfig,
			String outputFile, RunConfig config) throws TigerstripeException {
		String outputPath = "";
		String outputDir = pluginConfig.getProjectHandle().getProjectDetails()
				.getOutputDirectory();
		String projectDir = pluginConfig.getProjectHandle().getLocation()
				.toOSString();

		outputPath = projectDir + File.separator + outputDir + File.separator
				+ outputFile;
		if (config != null && config.getAbsoluteOutputDir() != null) {
			outputPath = config.getAbsoluteOutputDir() + File.separator
					+ outputDir + File.separator + outputFile;
		}

		// create any subdir in the outputDir if any is included
		// in the outputFile
		File outputFileF = new File(outputPath);
		return outputFileF;
	}

	protected Writer getDefaultWriter(PluggablePluginConfig pluginConfig,
			String outputFile, RunConfig config) throws TigerstripeException {

		try {
			File outputFileF = getOutputFile(pluginConfig, outputFile, config);
			if (outputFileF.getParentFile() != null
					&& !outputFileF.getParentFile().exists()) {
				outputFileF.getParentFile().mkdirs();
			}

			Writer result = new FileWriter(outputFileF);
			return result;
		} catch (IOException e) {
			throw new TigerstripeException("Error while trying to create '"
					+ outputFile + "': " + e.getMessage());
		}
	}

	// Definitions for the local velocity context
	public VelocityContextDefinition[] getVelocityContextDefinitions() {
		return this.contextDefinitions
				.toArray(new VelocityContextDefinition[contextDefinitions
						.size()]);
	}

	public void addVelocityContextDefinition(VelocityContextDefinition def) {
		contextDefinitions.add(def);
		markProjectDirty();
	}

	public void addVelocityContextDefinitions(VelocityContextDefinition[] def) {
		contextDefinitions.addAll(Arrays.asList(def));
		markProjectDirty();
	}

	public void removeVelocityContextDefinition(VelocityContextDefinition def) {
		contextDefinitions.remove(def);
		markProjectDirty();
	}

	public void removeVelocityContextDefinitions(VelocityContextDefinition[] def) {
		contextDefinitions.removeAll(Arrays.asList(def));
		markProjectDirty();
	}

	public String[] getMacroLibraries() {
		if (hasMacroLibrary())
			return this.macroLibraries
					.toArray(new String[macroLibraries.size()]);
		else
			return new String[0];
	}

	public void addMacroLibraries(String[] library) {
		macroLibraries.addAll(Arrays.asList(library));
		markProjectDirty();
	}

	public void addMacroLibrary(String library) {
		macroLibraries.add(library);
		markProjectDirty();
	}

	public void removeMacroLibraries(String[] library) {
		macroLibraries.removeAll(Arrays.asList(library));
		markProjectDirty();
	}

	public void removeMacroLibrary(String library) {
		macroLibraries.remove(library);
		markProjectDirty();
	}

	public boolean hasMacroLibrary() {
		if (this.macroLibraries.size() > 0)
			return true;
		else
			return false;
	}

}
