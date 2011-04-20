/*******************************************************************************
 * Copyright (c) 2011 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Richard Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginVelocityLog;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IModelRule;
import org.eclipse.tigerstripe.workbench.plugins.IModelTemplateRule;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementation of an Model-based rule, ie. a rule that run as a result of
 * a loop over a set of Models/Modules.
 * 
 * @author Richard Craddock
 * 
 */
public class ModelTemplateRule extends TemplateBasedRule implements
		IModelRule {

	private final static String REPORTTEMPLATE = "IModelTemplateRunRule.vm";

	public final static String LABEL = "Model Run Rule";
	

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return IModelTemplateRule.class.getCanonicalName();
	}

	@Override
	public void buildBodyFromNode(Node node) {
		Element elm = (Element) node;
		NodeList bodies = elm.getElementsByTagName("body");
		if (bodies.getLength() != 0) {
			Element body = (Element) bodies.item(0);
			setTemplate(Util.fixWindowsPath(body.getAttribute("template")));
			setOutputFile(body.getAttribute("outputFile"));
		
			setSuppressEmptyFilesStr(body.getAttribute("suppressFiles"));
			setOverwriteFilesStr(body.getAttribute("overwriteFiles"));
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
		elm.setAttribute("suppressFiles", isSuppressEmptyFilesStr());
		elm.setAttribute("overwriteFiles", isOverwriteFilesStr());
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
	}

	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {

		ITigerstripeModelProject currentModel = null;
		
		Writer writer = null;
		try {
			initializeReport(pluginConfig);
			//#####################################################################################
			// Take account of the "All Rules As Local" advnanced property
			boolean includeDependencies = true;
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
			Collection<ITigerstripeModelProject> resultSet;
			if(!((IM1RunConfig) runConfig).isAllRulesAsLocal()){
				resultSet = ModelRuleHelper.getResultSet(pluginConfig, monitor);
			} else {
				resultSet = new HashSet<ITigerstripeModelProject>();
				IAbstractTigerstripeProject aProject = pluginConfig
					.getProjectHandle();
				if (aProject != null
						&& aProject instanceof ITigerstripeModelProject) {
					ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
					//Add the base project itself!
					resultSet.add(project);
				}
			}
			

			// Velocity specifics......
			VelocityEngine engine = setClasspathLoaderForVelocity(pluginConfig,
					exec);
			Template template = engine.getTemplate(getTemplate());
			Expander expander = new Expander(pluginConfig);


			VelocityContext defaultContext = getDefaultContext(pluginConfig,
					context);

			// LOOP
			for (ITigerstripeModelProject model : resultSet) {

				VelocityContext localContext = exec.getPlugin()
						.getLocalVelocityContext(defaultContext, this);

				currentModel = model;
				
				//TODO - is this the best name?
				localContext.put(MODULE, currentModel);
				// add the context entries for this "module"
				
				Map<String, Object> moduleContext = getModuleContext(currentModel);
				for ( Entry<String, Object> entry : moduleContext.entrySet()){
					localContext.put(entry.getKey(), entry.getValue());
				}
				
				
				
				localContext.put("templateName", template.getName());
				// Logging stuff
				localContext.put("pluginLog", new PluginVelocityLog(template
						.getName()));

				expander.setCurrentTSModel(currentModel);
				String targetFile = expander.expandVar(getOutputFile(),currentModel);
				File outputFileF = getOutputFile(pluginConfig, targetFile, exec
						.getConfig());

				Collection<ITigerstripeModelProject> modules = getReport()
						.getModules();
				modules.add(currentModel);
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


		} catch (Exception e) {
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
		}
	}



}
