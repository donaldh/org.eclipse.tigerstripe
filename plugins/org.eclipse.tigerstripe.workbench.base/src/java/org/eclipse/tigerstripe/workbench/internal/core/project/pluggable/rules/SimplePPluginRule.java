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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginVelocityLog;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.RuleReport;
import org.eclipse.tigerstripe.workbench.plugins.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.plugins.ISimpleTemplateRunRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementation of a simple rule that is run once per generation cycle
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class SimplePPluginRule extends M1LevelRule implements
		ISimpleTemplateRunRule {

	private final static String REPORTTEMPLATE = "ISimpleTemplateRunRule.vm";

	private RuleReport report;

	private boolean suppressEmptyFiles = true;

	private boolean overwriteFiles = true;

	public final static String LABEL = "Simple Run Rule";

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return ISimpleTemplateRunRule.class.getCanonicalName();
	}

	@Override
	public void buildBodyFromNode(Node node) {
		Element elm = (Element) node;
		NodeList bodies = elm.getElementsByTagName("body");
		if (bodies.getLength() != 0) {
			Element body = (Element) bodies.item(0);
			setTemplate(body.getAttribute("template"));
			setOutputFile(body.getAttribute("outputFile"));
			setSuppressEmptyFilesStr(body.getAttribute("suppressFiles"));
			setOverwriteFilesStr(body.getAttribute("overwriteFiles"));

			NodeList libraries = body.getElementsByTagName("library");
			for (int i = 0; i < libraries.getLength(); i++) {
				addMacroLibrary(((Element) libraries.item(i))
						.getAttribute("name"));
			}
		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		Element elm = document.createElement("body");
		elm.setAttribute("template", getTemplate());
		elm.setAttribute("outputFile", getOutputFile());
		elm.setAttribute("suppressFiles", isSuppressEmptyFilesStr());
		elm.setAttribute("overwriteFiles", isOverwriteFilesStr());

		if (hasMacroLibrary()) {
			for (int i = 0; i < getMacroLibraries().length; i++) {
				Element lib = (document.createElement("library"));
				lib.setAttribute("name", getMacroLibraries()[i]);
				elm.appendChild(lib);
			}
		}

		return elm;
	}

	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {
		Writer writer = null;
		try {
			this.report = new RuleReport(pluginConfig);
			this.report.setTemplate(PluggablePlugin.TEMPLATE_PREFIX + "/"
					+ REPORTTEMPLATE);
			this.report.setName(getName());
			this.report.setType(getLabel());
			this.report.setEnabled(isEnabled());
			this.report.setOverwriteFiles(isOverwriteFiles());
			this.report.setSuppressEmptyFiles(isSuppressEmptyFiles());

			VelocityEngine engine = setClasspathLoaderForVelocity();
			Template template = engine.getTemplate(getTemplate());

			Expander expander = new Expander(pluginConfig);
			String targetFile = expander.expandVar(getOutputFile());
			File outputFileF = getOutputFile(pluginConfig, targetFile, exec
					.getConfig());

			// Only create the flag if we are allowed to overwrite Or the file
			// doesn't exist
			if (isOverwriteFiles() || !outputFileF.exists()) {

				writer = getDefaultWriter(pluginConfig, targetFile, exec
						.getConfig());

				// TODO add referenced user-java objects into the context
				VelocityContext defaultContext = getDefaultContext(
						pluginConfig, exec);
				VelocityContext localContext = exec.getPlugin()
						.getLocalVelocityContext(defaultContext, this);

				localContext.put("templateName", template.getName());

				// Logging stuff
				localContext.put("pluginLog", new PluginVelocityLog(template
						.getName()));

				template.merge(localContext, writer);
				writer.close();

				Long fred = outputFileF.length();
				if (fred.intValue() == 0 && isSuppressEmptyFiles()) {
					outputFileF.delete();
					Collection<String> files = this.report.getSuppressedFiles();
					files.add(targetFile);
				} else {
					Collection<String> files = this.report.getGeneratedFiles();
					files.add(targetFile);
				}
			} else {
				Collection<String> files = this.report.getPreservedFiles();
				files.add(targetFile);
			}

		} catch (TigerstripeException e) {
			throw e;
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException("Unexpected error while merging '"
					+ getTemplate() + "' template: " + e.getMessage(), e);
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

	public RuleReport getReport() {
		return report;
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
		setSuppressEmptyFiles(Boolean.parseBoolean(suppressEmptyFilesStr));
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
		setOverwriteFiles(Boolean.parseBoolean(overwriteFilesStr));
	}

}
