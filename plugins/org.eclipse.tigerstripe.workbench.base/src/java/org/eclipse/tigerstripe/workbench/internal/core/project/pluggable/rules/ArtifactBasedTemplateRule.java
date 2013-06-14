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
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginVelocityLog;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactFilter;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactWrappedRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactWrapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementation of an Artifact-based rule, ie. a rule that run as a result of
 * a loop over a set of identified Artifact types.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class ArtifactBasedTemplateRule extends TemplateBasedRule implements
		IArtifactWrappedRule {

	private final static String REPORTTEMPLATE = "IArtifactBasedTemplateRunRule.vm";

	private boolean includeDependencies = false;

	public final static String LABEL = "Artifact Model Run Rule";

	private String modelClass = "";

	private String modelClassName = "wrapper";

	private String filterClass = "";

	private String artifactType = "";

	public String getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(String artifactType) {
		markDirty();
		this.artifactType = artifactType;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		markDirty();
		this.modelClass = modelClass;
	}

	public String getModelClassName() {
		return modelClassName;
	}

	public void setModelClassName(String modelClassName) {
		markDirty();
		this.modelClassName = modelClassName;
	}

	public String getArtifactFilterClass() {
		return this.filterClass;
	}

	public void setArtifactFilterClass(String filterClass) {
		markDirty();
		this.filterClass = filterClass;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return IArtifactBasedTemplateRule.class.getCanonicalName();
	}

	@Override
	public void buildBodyFromNode(Node node) {
		Element elm = (Element) node;
		NodeList bodies = elm.getElementsByTagName("body");
		if (bodies.getLength() != 0) {
			Element body = (Element) bodies.item(0);
			setTemplate(Util.fixWindowsPath(body.getAttribute("template")));
			setOutputFile(body.getAttribute("outputFile"));
			setModelClass(body.getAttribute("modelClass"));
			setModelClassName(body.getAttribute("modelClassName"));
			setArtifactFilterClass(body.getAttribute("artifactFilterClass"));
			setArtifactType(MigrationHelper.pluginMigrateArtifactType(body
					.getAttribute("artifactType")));
			setSuppressEmptyFilesStr(body.getAttribute("suppressFiles"));
			setOverwriteFilesStr(body.getAttribute("overwriteFiles"));
			setIncludeDependenciesStr(body.getAttribute("includeDependencies"));
			NodeList libraries = body.getElementsByTagName("library");
			for (int i = 0; i < libraries.getLength(); i++) {
				addMacroLibrary(Util.fixWindowsPath(((Element) libraries
						.item(i)).getAttribute("name")));
			}

		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		Element elm = document.createElement("body");
		elm.setAttribute("template", Util.fixWindowsPath(getTemplate()));
		elm.setAttribute("outputFile", getOutputFile());
		elm.setAttribute("modelClass", getModelClass());
		elm.setAttribute("modelClassName", getModelClassName());
		elm.setAttribute("artifactFilterClass", getArtifactFilterClass());
		elm.setAttribute("artifactType", getArtifactType());
		elm.setAttribute("suppressFiles", isSuppressEmptyFilesStr());
		elm.setAttribute("overwriteFiles", isOverwriteFilesStr());
		elm.setAttribute("includeDependencies", isIncludeDependenciesStr());
		if (hasMacroLibrary()) {
			for (int i = 0; i < getMacroLibraries().length; i++) {
				Element lib = (document.createElement("library"));
				lib.setAttribute("name", Util
						.fixWindowsPath(getMacroLibraries()[i]));
				elm.appendChild(lib);
			}
		}
		return elm;
	}

	@Override
	protected String getReportTemplatePath() {
		return PluggablePlugin.TEMPLATE_PREFIX + "/" + REPORTTEMPLATE;
	}

	@Override
	protected void initializeReport(PluggablePluginConfig pluginConfig) {
		super.initializeReport(pluginConfig);
		getReport().setIncludeDependencies(isIncludeDependencies());
	}

	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {

		IAbstractArtifact currentArtifact = null;
		

		Writer writer = null;
		VelocityEngine engine = null;
		try {
			initializeReport(pluginConfig);

			//#####################################################################################
			// Take account of the "All Rules As Local" advnanced property
			boolean includeDependencies = isIncludeDependencies();
			RunConfig runConfig = exec.getConfig();
			if (runConfig instanceof IM1RunConfig){
				boolean overrideMe =((IM1RunConfig) runConfig).isAllRulesAsLocal();
				if (overrideMe){
					includeDependencies = false;
				}
			}
			
			Map<String, Object> context = getGlobalContext(pluginConfig, includeDependencies);
			
			// setContext(context);

			IProgressMonitor monitor = exec.getConfig().getMonitor();
			Collection<IAbstractArtifact> resultSet = ArtifactRuleHelper
					.getResultSet(getArtifactType(), pluginConfig,
							includeDependencies, monitor);
			IArtifactFilter filter = ArtifactRuleHelper.getArtifactFilter(
					getArtifactFilterClass(), exec);
			getReport().setArtifactType(getArtifactType());

			// Velocity specifics......
			engine = setClasspathLoaderForVelocity(pluginConfig, exec);
			
			
			Template template = engine.getTemplate(getTemplate());
			Expander expander = new Expander(pluginConfig);
			expander.addVelocityContextDefinitions(getVelocityContextDefinitions(), 
					exec.getPlugin());
			// VelocityContext defaultContext = getDefaultContext(
			// pluginConfig, exec);
			VelocityContext defaultContext = getDefaultContext(pluginConfig, context, exec);

			// LOOP
			for (IAbstractArtifact artifact : resultSet) {

				VelocityContext localContext = exec.getPlugin()
						.getLocalVelocityContext(defaultContext, this);

				currentArtifact = artifact;
				if (filter != null && !filter.select(artifact)) {
					continue;
				}

				// Deal with wrappers
				if (getModelClass() != null && getModelClass().length() != 0) {
					Object modelObj = exec.getPlugin().getInstance(
							getModelClass());
					if (modelObj instanceof IArtifactWrapper) {
						IArtifactWrapper wrapper = null;
						wrapper = (IArtifactWrapper) modelObj;
						wrapper.setIArtifact(artifact);
						wrapper.setPluginConfig(pluginConfig);

						localContext.put(getModelClassName(), wrapper);
						expander
								.setCurrentWrapper(wrapper, getModelClassName());
					} else {
						TigerstripeRuntime
								.logInfoMessage("Error: "
										+ getModelClass()
										+ " doesn't implement IArtifactWrapper, ignoring.");
					}
				}

				localContext.put("artifact", artifact);
				localContext.put("templateName", template.getName());

				// Logging stuff
				localContext.put("pluginLog", new PluginVelocityLog(template
						.getName()));

				expander.setCurrentArtifact(artifact);
				String targetFile = expander.expandVar(getOutputFile());
				File outputFileF = getOutputFile(pluginConfig, targetFile, exec
						.getConfig());

				Collection<String> artifacts = getReport()
						.getMatchedArtifacts();
				artifacts.add(artifact.getFullyQualifiedName());
				// Only create the file if we are allowed to overwrite Or
				// the file doesn't exist
				if (isOverwriteFiles() || !outputFileF.exists()) {

					writer = getDefaultWriter(pluginConfig, targetFile, exec
							.getConfig());
					template.merge(localContext, writer);
					writer.close();

					Long fred = outputFileF.length();
					if (fred.intValue() == 0 && isSuppressEmptyFiles()) {
						outputFileF.delete();
						Collection<String> files = getReport()
								.getSuppressedFiles();
						files.add(targetFile);
					} else {
						Collection<String> files = getReport()
								.getGeneratedFiles();
						files.add(targetFile);
					}
				} else if (outputFileF.exists()) {
					Collection<String> files = getReport().getPreservedFiles();
					if (!files.contains(targetFile))
						files.add(targetFile);
				}
			}

		} catch (TigerstripeException e) {
			TigerstripeException newException;
			if (currentArtifact != null) {
				newException = new TigerstripeException(e.getMessage()
						+ ", current artifact: "
						+ currentArtifact.getFullyQualifiedName(), e);
			} else {
				newException = e;
			}
			throw newException;
		} catch (Exception e) {
			if (currentArtifact != null)
				throw new TigerstripeException(
						"Unexpected error while merging '" + getTemplate()
								+ "' template: " + e.getMessage()
								+ ", current artifact: "
								+ currentArtifact.getFullyQualifiedName(), e);
			else
				throw new TigerstripeException(
						"Unexpected error while merging '" + getTemplate()
								+ "' template: " + e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (engine!=null) {
				// nmehrega - bugzilla 251858: Shut down logger so logger streams are closed properly
				Object logger = engine.getApplicationAttribute(LOGGER_KEY);
				engine.setApplicationAttribute(LOGGER_KEY, "");
				if (logger instanceof Log4JLogChute) {
					((Log4JLogChute)logger).shutdown();
				}
			}
			
		}
	}

	public boolean isIncludeDependencies() {
		return this.includeDependencies;
	}

	public String isIncludeDependenciesStr() {
		return Boolean.toString(includeDependencies);
	}

	public void setIncludeDependencies(boolean includeDependencies) {
		markDirty();
		this.includeDependencies = includeDependencies;
	}

	public void setIncludeDependenciesStr(String includeDependencies) {
		markDirty();
		this.includeDependencies = Boolean.parseBoolean(includeDependencies);
	}

}
