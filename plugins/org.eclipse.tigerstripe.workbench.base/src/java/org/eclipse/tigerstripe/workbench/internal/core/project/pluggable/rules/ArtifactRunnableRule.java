/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
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
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactFilter;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableWrapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ArtifactRunnableRule extends RunnableRule implements IArtifactRule, IRunnableRule {

	private final static String REPORTTEMPLATE = "IArtifactRunnableRule.vm";

	public final static String LABEL = "Artifact Runnable Rule";
	
	private boolean includeDependencies = false;
	
	private String filterClass = "";

	/**
	 *  Artifact Type is the FQN of the types of artifact that this rule uses, OR "AnyArtifact".
	 */
	private String artifactType = "";
	
	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(String artifactType) {
		markDirty();
		this.artifactType = artifactType;
	}

	public String getArtifactFilterClass() {
		return this.filterClass;
	}

	public void setArtifactFilterClass(String filterClass) {
		markDirty();
		this.filterClass = filterClass;
	}

	public String getType() {
		return IArtifactRunnableRule.class.getCanonicalName();
	}
	
	@Override
	public void buildBodyFromNode(Node node) {
		Element elm = (Element) node;
		NodeList bodies = elm.getElementsByTagName("body");
		if (bodies.getLength() != 0) {
			Element body = (Element) bodies.item(0);
			setArtifactFilterClass(body.getAttribute("artifactFilterClass"));
			setArtifactType(MigrationHelper.pluginMigrateArtifactType(body
					.getAttribute("artifactType")));
			setSuppressEmptyFilesStr(body.getAttribute("suppressFiles"));
			setOverwriteFilesStr(body.getAttribute("overwriteFiles"));
			setIncludeDependenciesStr(body.getAttribute("includeDependencies"));
			setRunnableClassName(body.getAttribute("runnableClassName"));

		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		Element elm = document.createElement("body");
		elm.setAttribute("artifactFilterClass", getArtifactFilterClass());
		elm.setAttribute("artifactType", getArtifactType());
		elm.setAttribute("suppressFiles", isSuppressEmptyFilesStr());
		elm.setAttribute("overwriteFiles", isOverwriteFilesStr());
		elm.setAttribute("includeDependencies", isIncludeDependenciesStr());
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
		getReport().setIncludeDependencies(isIncludeDependencies());
	}
	
	@Override
	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {

		IAbstractArtifact currentArtifact = null;
		Map<String, Object> context = getGlobalContext(pluginConfig);
		
		initializeReport(pluginConfig);
		getReport().setArtifactType(getArtifactType());
		getReport().setRunnableClassName(getRunnableClassName());
		
		// We need to add a few extra items that should be respected by the plugin, but are in fact out of our control!
		context.put(REPORT, getReport());
		context.put(SUPPRESSFILES,isSuppressEmptyFiles());
		context.put(OVERWRITEFILES, isOverwriteFiles());
		context.put(INCLUDEDEPENDENCIES, isIncludeDependencies());
		
		try {
			// TODO 
			
			// We need to add a few extra items that should be respected by the plugin, but are in fact out of our control!
			context.put(REPORT,getReport());
			setContext(context);

			IProgressMonitor monitor = exec.getConfig().getMonitor();
			Collection<IAbstractArtifact> resultSet = ArtifactRuleHelper.getResultSet(getArtifactType(), pluginConfig, isIncludeDependencies(), monitor);
			IArtifactFilter filter = ArtifactRuleHelper.getArtifactFilter(getArtifactFilterClass(), exec);

			// LOOP 

			for (IAbstractArtifact artifact : resultSet) {
				currentArtifact = artifact;
				if (filter != null && !filter.select(artifact)) {
					continue;
				}

				// Reporting
				Collection<String> artifacts = getReport()
					.getMatchedArtifacts();
				artifacts.add(artifact.getFullyQualifiedName());
				
				// Add the "artifact specific" entries to the context
				context.put(ARTIFACT,currentArtifact);

				// Instantiate the runnable class & run it
				Object runnableObj = exec.getPlugin().getInstance(
						getRunnableClassName());
				if (runnableObj instanceof IRunnableWrapper){
					IRunnableWrapper runnableWrapper = (IRunnableWrapper) runnableObj;
					runnableWrapper.setContext(context);
					runnableWrapper.run();
				}
				

				// Remove the artifact specifics
				context.remove(ARTIFACT);
				// 
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
						"Unexpected error while running '" + getName()
						+ "' rule " + e.getMessage()
						+ ", current artifact: "
						+ currentArtifact.getFullyQualifiedName(), e);
			else
				throw new TigerstripeException(
						"Unexpected error while running '" + getName()
						+ "' rule " + e.getMessage(), e);
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
