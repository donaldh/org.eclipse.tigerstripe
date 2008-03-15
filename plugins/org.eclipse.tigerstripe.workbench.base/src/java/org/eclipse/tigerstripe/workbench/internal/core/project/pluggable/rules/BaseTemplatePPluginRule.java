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
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.plugins.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateRunRule;

public abstract class BaseTemplatePPluginRule extends BasePPluginRule implements
		ITemplateRunRule {

	public BaseTemplatePPluginRule() {
		super();
		contextDefinitions = new ArrayList<VelocityContextDefinition>();
		macroLibraries = new ArrayList<String>();
	}

	private List<String> macroLibraries;

	private List<VelocityContextDefinition> contextDefinitions;

	private String template = "";

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
		properties.put("file.resource.loader.path", getContainingDescriptor()
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

	protected abstract VelocityContext getDefaultContext(
			PluggablePluginConfig pluginConfig, IPluginRuleExecutor exec)
			throws TigerstripeException;

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

}
