/*******************************************************************************
 * Copyright (c) 2011 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.plugins.IModelRule;
import org.eclipse.tigerstripe.workbench.plugins.IModelRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableWrapper;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModelRunnableRule extends RunnableRule implements IModelRule, IRunnableRule {

	private final static String REPORTTEMPLATE = "IModelRunnableRule.vm";

	public final static String LABEL = "Model Runnable Rule";
	
	private final boolean includeDependencies = false;
	

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return IModelRunnableRule.class.getCanonicalName();
	}
	
	@Override
	public void buildBodyFromNode(Node node) {
		Element elm = (Element) node;
		NodeList bodies = elm.getElementsByTagName("body");
		if (bodies.getLength() != 0) {
			Element body = (Element) bodies.item(0);
			setSuppressEmptyFilesStr(body.getAttribute("suppressFiles"));
			setOverwriteFilesStr(body.getAttribute("overwriteFiles"));
			setRunnableClassName(body.getAttribute("runnableClassName"));

		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		Element elm = document.createElement("body");
		elm.setAttribute("suppressFiles", isSuppressEmptyFilesStr());
		elm.setAttribute("overwriteFiles", isOverwriteFilesStr());
		elm.setAttribute("runnableClassName", getRunnableClassName());
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
	
	@Override
	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {

		ITigerstripeModelProject currentModel = null;
		
		
		initializeReport(pluginConfig);
		getReport().setRunnableClassName(getRunnableClassName());
		
		
		try {
			//  
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

			// We need to add a few extra items that should be respected by the plugin, but are in fact out of our control!
			context.put(REPORT, getReport());
			context.put(SUPPRESSFILES,isSuppressEmptyFiles());
			context.put(OVERWRITEFILES, isOverwriteFiles());
			
			// We need to add a few extra items that should be respected by the plugin, but are in fact out of our control!
			context.put(REPORT,getReport());
			setContext(context);

			IProgressMonitor monitor = exec.getConfig().getMonitor();
			Collection<ModelProject> resultSet;
			if(!((IM1RunConfig) runConfig).isAllRulesAsLocal()){
				resultSet = ModelRuleHelper.getResultSet(pluginConfig, monitor);
			} else {
				resultSet = new HashSet<ModelProject>();
				IAbstractTigerstripeProject aProject = pluginConfig
					.getProjectHandle();
				if (aProject != null
						&& aProject instanceof ITigerstripeModelProject) {
					ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
					//Add the base project itself!
					resultSet.add(new ModelProject(project));
				}
			}

			for (ModelProject model : resultSet) {
				currentModel = model.getProject();
				
				// Add the "module specific" entries to the context
				context.put(MODULE, currentModel);

				Map<String, Object> moduleContext = getModuleContext(model);
				for ( Entry<String, Object> entry : moduleContext.entrySet()){
					context.put(entry.getKey(), entry.getValue());
				}
				
				Collection<ITigerstripeModelProject> modules = getReport()
					.getModules();
				modules.add(currentModel);
				
				
				// Instantiate the runnable class & run it
				Object runnableObj = exec.getPlugin().getInstance(
						getRunnableClassName());
				if (runnableObj instanceof IRunnableWrapper){
					IRunnableWrapper runnableWrapper = (IRunnableWrapper) runnableObj;
					runnableWrapper.setContext(context);
					runnableWrapper.run();
				}
				

				// Remove the artifact specifics
				context.remove(MODULE);
				for ( String key : moduleContext.keySet()){
					context.remove(key);
				}
				// 
			}


		
		} catch (Exception e) {
			
				throw new TigerstripeException(
						"Unexpected error while running '" + getName()
						+ "' rule " + e.getMessage(), e);
		}
	}

	
	
}
