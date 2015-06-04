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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.apache.velocity.runtime.log.LogChute;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
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
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.DiagramGenerator;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.internal.core.util.VelocityContextUtil;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class TemplateBasedRule extends Rule implements
		ITemplateBasedRule {

	protected final String LOGGER_KEY = "CUSTOM_LOGGER";
	
	public TemplateBasedRule() {
		super();
		contextDefinitions = new ArrayList<VelocityContextDefinition>();
		macroLibraries = new ArrayList<String>();
	}

	private boolean suppressEmptyFiles = true;

	private boolean overwriteFiles = true;

	private List<String> macroLibraries;

	private List<VelocityContextDefinition> contextDefinitions;

	private String template = "";

	private VelocityContext defaultVContext;

	protected void initializeReport(PluggablePluginConfig pluginConfig) {
		super.initializeReport(pluginConfig);
		getReport().setOverwriteFiles(isOverwriteFiles());
		getReport().setSuppressEmptyFiles(isSuppressEmptyFiles());
	}

	protected VelocityContext getDefaultContext(
			PluggablePluginConfig pluginConfig, Map<String, Object> context, IPluginRuleExecutor exec)
			throws TigerstripeException {
		if (this.defaultVContext == null) {
			this.defaultVContext = new VelocityContext();
			VelocityContextUtil util = new VelocityContextUtil();
			this.defaultVContext.put("util", util);
		}
		for (Entry<String, Object> e : context.entrySet()) {
			this.defaultVContext.put(e.getKey(), e.getValue());
		}

		return this.defaultVContext;
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
		if (artifactMgr.getRegisteredArtifacts().contains(
				ManagedEntityArtifact.MODEL)) {
			Collection<IAbstractArtifact> entities = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(
							ManagedEntityArtifact.MODEL, false,
							new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allEntities = artifactMgr
					.getArtifactsByModel(ManagedEntityArtifact.MODEL, true,
							false, new NullProgressMonitor());

			defaultVContext.put("entities", entities);
			defaultVContext.put("allEntities", allEntities);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(
				DatatypeArtifact.MODEL)) {
			Collection<IAbstractArtifact> datatypes = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(DatatypeArtifact.MODEL,
							false, new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allDatatypes = artifactMgr
					.getArtifactsByModel(DatatypeArtifact.MODEL, true, false,
							new NullProgressMonitor());

			defaultVContext.put("datatypes", datatypes);
			defaultVContext.put("allDatatypes", allDatatypes);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(EventArtifact.MODEL)) {
			Collection<IAbstractArtifact> events = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(EventArtifact.MODEL, false,
							new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allEvents = artifactMgr
					.getArtifactsByModel(EventArtifact.MODEL, true, false,
							new NullProgressMonitor());

			defaultVContext.put("events", events);
			defaultVContext.put("allEvents", allEvents);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(EnumArtifact.MODEL)) {
			Collection<IAbstractArtifact> enums = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(EnumArtifact.MODEL, false,
							new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allEnums = artifactMgr
					.getArtifactsByModel(EnumArtifact.MODEL, true, false,
							new NullProgressMonitor());

			defaultVContext.put("enumerations", enums);
			defaultVContext.put("allEnumerations", allEnums);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(
				ExceptionArtifact.MODEL)) {
			Collection<IAbstractArtifact> exceptions = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(ExceptionArtifact.MODEL,
							false, new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allExceptions = artifactMgr
					.getArtifactsByModel(ExceptionArtifact.MODEL, true, false,
							new NullProgressMonitor());

			defaultVContext.put("exceptions", exceptions);
			defaultVContext.put("allExceptions", allExceptions);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(QueryArtifact.MODEL)) {
			Collection<IAbstractArtifact> queries = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(QueryArtifact.MODEL, false,
							new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allQueries = artifactMgr
					.getArtifactsByModel(QueryArtifact.MODEL, true, false,
							new NullProgressMonitor());

			defaultVContext.put("queries", queries);
			defaultVContext.put("allQueries", allQueries);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(
				SessionFacadeArtifact.MODEL)) {
			Collection<IAbstractArtifact> sessions = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(
							SessionFacadeArtifact.MODEL, false,
							new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allSessions = artifactMgr
					.getArtifactsByModel(SessionFacadeArtifact.MODEL, true,
							false, new NullProgressMonitor());

			defaultVContext.put("sessions", sessions);
			defaultVContext.put("allSessions", allSessions);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(
				UpdateProcedureArtifact.MODEL)) {
			Collection<IAbstractArtifact> updateProcedures = ArtifactFilter
					.filter(artifactMgr.getArtifactsByModel(
							UpdateProcedureArtifact.MODEL, false,
							new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allUpdateProcedures = artifactMgr
					.getArtifactsByModel(UpdateProcedureArtifact.MODEL, true,
							false, new NullProgressMonitor());

			defaultVContext.put("updateProcedures", updateProcedures);
			defaultVContext.put("allUpdateProcedures", allUpdateProcedures);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(
				AssociationArtifact.MODEL)) {
			Collection<IAbstractArtifact> associations = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(AssociationArtifact.MODEL,
							false, new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allAssociations = artifactMgr
					.getArtifactsByModel(AssociationArtifact.MODEL, true,
							false, new NullProgressMonitor());

			defaultVContext.put("associations", associations);
			defaultVContext.put("allAssociations", allAssociations);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(
				DependencyArtifact.MODEL)) {
			Collection<IAbstractArtifact> dependencies = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(DependencyArtifact.MODEL,
							false, new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allDependencies = artifactMgr
					.getArtifactsByModel(DependencyArtifact.MODEL, true, false,
							new NullProgressMonitor());

			defaultVContext.put("dependencies", dependencies);
			defaultVContext.put("allDependencies", allDependencies);
		}

		if (artifactMgr.getRegisteredArtifacts().contains(
				AssociationClassArtifact.MODEL)) {
			Collection<IAbstractArtifact> associationClasses = ArtifactFilter
					.filter(artifactMgr.getArtifactsByModel(
							AssociationClassArtifact.MODEL, false,
							new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allAssociationClasses = artifactMgr
					.getArtifactsByModel(AssociationClassArtifact.MODEL, true,
							false, new NullProgressMonitor());

			defaultVContext.put("associationClasses", associationClasses);
			defaultVContext.put("allAssociationClasses", allAssociationClasses);
		}

		if (artifactMgr.getRegisteredArtifacts()
				.contains(PackageArtifact.MODEL)) {
			Collection<IAbstractArtifact> packages = ArtifactFilter.filter(
					artifactMgr.getArtifactsByModel(PackageArtifact.MODEL,
							false, new NullProgressMonitor()), filter);
			Collection<IAbstractArtifact> allPackages = artifactMgr
					.getArtifactsByModel(PackageArtifact.MODEL, true, false,
							new NullProgressMonitor());

			defaultVContext.put("packages", packages);
			defaultVContext.put("allPackages", allPackages);
		}

		Collection<IAbstractArtifact> artifacts = artifactMgr.getAllArtifacts(
				false, new NullProgressMonitor());
		Collection<IAbstractArtifact> allArtifacts = artifactMgr
				.getAllArtifacts(true, false, new NullProgressMonitor());
		defaultVContext.put("artifacts", artifacts);
		defaultVContext.put("allArtifacts", allArtifacts);

		defaultVContext.put("pluginConfig", pluginConfig);
		defaultVContext.put("runtime", TigerstripeRuntime.getInstance());

		// This should eventually get removed as TigerstripeProject is not in
		// the API
		defaultVContext.put("tsProject", pluginConfig.getProject());
		defaultVContext.put("exp", new Expander(pluginConfig));
		defaultVContext.put("manager", artifactMgr);

		defaultVContext.put("tsProjectHandle", handle);
		defaultVContext.put("managerSession", session);
		defaultVContext
				.put("pluginDir", getContainingDescriptor().getBaseDir());

		// Added this - JKW
		defaultVContext.put("diagramGenerator", new DiagramGenerator(handle));

		if (session.getActiveFacet() != null
				&& session.getActiveFacet().canResolve())
			defaultVContext.put("annotationContext", session.getActiveFacet()
					.resolve().getAnnotationContext());

		return this.defaultVContext;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		markDirty();
		this.template = template;
	}

	private String outputFile = "";

	public String getOutputFile() {
		return this.outputFile;
	}

	public void setOutputFile(String outputFile) {
		markDirty();
		this.outputFile = outputFile;
	}

	/**
	 * Initializes the Velocity framework and sets it up with a classpath
	 * loader.
	 * 
	 * 
	 * @throws Exception
	 *             , if the class loader cannot be set up
	 */
	protected VelocityEngine setClasspathLoaderForVelocity(
			PluggablePluginConfig pluginConfig, IPluginRuleExecutor exec)
			throws Exception {

		VelocityEngine result = new VelocityEngine();

		result.setProperty("resource.loader", "file, class");

		// So we can still access templates from the classpath
		result.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		// To access templates from the file system.
		result.setProperty("file.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		result.setProperty("file.resource.loader.path", getContainingDescriptor()
				.getBaseDir().getCanonicalPath());

		// JS - DEBUG
//		System.out.println("***** file.resource.loader.path = "
//				+ getContainingDescriptor().getBaseDir().getCanonicalPath());

		result.setProperty("file.resource.loader.cache", "true");
		result.setProperty("file.resource.loader.modificationCheckInterval", "2");

		result.setProperty("velocimacro.permissions.allow.inline", "true");
		result.setProperty(
				"velocimacro.permissions.allow.inline.to.replace.global",
				"true");

		result.setProperty("class.resource.loader.cache", "true");

		if (hasMacroLibrary()) {
			StringBuilder libraryList = new StringBuilder();
			Iterator<String> it = macroLibraries.iterator();
			if (it.hasNext()) {
				libraryList.append(it.next());
			}
			while (it.hasNext()) {
				libraryList.append(",").append(it.next());
			}
			result.setProperty("velocimacro.library", libraryList.toString());
		}

		if (exec.getPlugin().isLogEnabled()) {
			
			
			String outputDir = pluginConfig.getProjectHandle()
					.getProjectDetails().getOutputDirectory();
			String logPath = exec.getPlugin().getLogPath();

			// Find the extension (if any) and insert ".velocity" before it
			String velocityLogPath;
			if (logPath.contains(".")) {

				// Pay attention in case of strange formats such as
				// path/road.street/avenue - we could easily put the extra word
				// in the middle of the path!

				IPath path = new Path(logPath);
				if (path.getFileExtension() != null) {
					String ext = path.getFileExtension();
					path = path.removeFileExtension()
							.addFileExtension("velocity").addFileExtension(ext);
				} else {
					path = path.addFileExtension("velocity");
				}
				velocityLogPath = path.toOSString();
			} else {
				velocityLogPath = logPath + ".velocity";
			}

			final String projectDir;
			if(exec.getConfig() != null && exec.getConfig().getAbsoluteOutputDir() != null) {
				projectDir = exec.getConfig().getAbsoluteOutputDir();
			} else if(pluginConfig.getProjectHandle().getLocation() != null) {
				projectDir = pluginConfig.getProjectHandle().getLocation().toOSString();				
			} else {
				throw new IOException("Project Directory is NULL");				
			}

			result.setProperty("runtime.log", projectDir + File.separatorChar
					+ outputDir + File.separator + velocityLogPath);
		} else {
			result.setProperty("runtime.log", "target/tigerstripe.gen/velocity.log");
		}

		LogChute logger = new Log4JLogChute();
		result.setApplicationAttribute(LOGGER_KEY, logger);
		result.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,logger);		

		ClassLoader startingLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			if (result.getClass().getClassLoader() != Thread.currentThread()
					.getContextClassLoader()) {
				Thread.currentThread().setContextClassLoader(
						result.getClass().getClassLoader());
			}
			result.init();	
		}  finally {
			Thread.currentThread().setContextClassLoader(startingLoader);
		}
		return result;
	}

	protected File getOutputFile(PluggablePluginConfig pluginConfig,
			String outputFile, RunConfig config) throws TigerstripeException {
		String outputPath = "";
		String outputDir = pluginConfig.getProjectHandle().getProjectDetails()
				.getOutputDirectory();

		if (pluginConfig.getProjectHandle().getActiveFacet() != null) {
			String relDir = pluginConfig.getProjectHandle().getActiveFacet()
					.getGenerationDir();
			if (relDir != null && !"".equals(relDir)) {
				outputDir = outputDir + File.separator + relDir;
			}
		}
		outputPath = outputDir + File.separator + outputFile;

		IPath projectDir = pluginConfig.getProjectHandle().getLocation();
		if(projectDir != null) {
			outputPath = projectDir.toOSString() + File.separator + outputPath;
		}

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
		markDirty();
		contextDefinitions.add(def);
		def.setContainer(this);
	}

	public void addVelocityContextDefinitions(VelocityContextDefinition[] defs) {
		for (VelocityContextDefinition def : defs) {
			addVelocityContextDefinition(def);
		}
	}

	public void removeVelocityContextDefinition(VelocityContextDefinition def) {
		markDirty();
		contextDefinitions.remove(def);
		def.setContainer(null);
	}

	public void removeVelocityContextDefinitions(
			VelocityContextDefinition[] defs) {
		for (VelocityContextDefinition def : defs) {
			removeVelocityContextDefinition(def);
		}
	}

	public String[] getMacroLibraries() {
		if (hasMacroLibrary())
			return this.macroLibraries
					.toArray(new String[macroLibraries.size()]);
		else
			return new String[0];
	}

	public void addMacroLibraries(String[] library) {
		markDirty();
		macroLibraries.addAll(Arrays.asList(library));
	}

	public void addMacroLibrary(String library) {
		markDirty();
		macroLibraries.add(library);
	}

	public void removeMacroLibraries(String[] library) {
		markDirty();
		macroLibraries.removeAll(Arrays.asList(library));
	}

	public void removeMacroLibrary(String library) {
		markDirty();
		macroLibraries.remove(library);
	}

	public boolean hasMacroLibrary() {
		if (this.macroLibraries.size() > 0)
			return true;
		else
			return false;
	}

	public boolean isSuppressEmptyFiles() {
		return suppressEmptyFiles;
	}

	public String isSuppressEmptyFilesStr() {
		return Boolean.toString(suppressEmptyFiles);
	}

	public void setSuppressEmptyFiles(boolean suppressEmptyFiles) {
		markDirty();
		this.suppressEmptyFiles = suppressEmptyFiles;
	}

	public void setSuppressEmptyFilesStr(String suppressEmptyFilesStr) {
		markDirty();
		this.suppressEmptyFiles = Boolean.parseBoolean(suppressEmptyFilesStr);
	}

	public boolean isOverwriteFiles() {
		return overwriteFiles;
	}

	public String isOverwriteFilesStr() {
		return Boolean.toString(overwriteFiles);
	}

	public void setOverwriteFiles(boolean overwriteFiles) {
		markDirty();
		this.overwriteFiles = overwriteFiles;
	}

	public void setOverwriteFilesStr(String overwriteFilesStr) {
		markDirty();
		this.overwriteFiles = Boolean.parseBoolean(overwriteFilesStr);
	}

}
