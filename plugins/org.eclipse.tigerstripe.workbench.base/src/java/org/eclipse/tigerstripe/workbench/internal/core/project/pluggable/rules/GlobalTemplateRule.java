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
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
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
public class GlobalTemplateRule extends TemplateBasedRule implements
		ITemplateBasedRule, IGlobalRule {

	private final static String REPORTTEMPLATE = "ISimpleTemplateRunRule.vm";

	public final static String LABEL = "Global Template Rule";

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return IGlobalTemplateRule.class.getCanonicalName();
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
				addMacroLibrary(((Element) libraries.item(i))
						.getAttribute("name"));
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
				lib.setAttribute("name", getMacroLibraries()[i]);
				elm.appendChild(lib);
			}
		}

		return elm;
	}

	@Override
	protected String getReportTemplatePath() {
		return PluggablePlugin.TEMPLATE_PREFIX + "/" + REPORTTEMPLATE;
	}

	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {
		Writer writer = null;
		try {
			initializeReport(pluginConfig);

			VelocityEngine engine = setClasspathLoaderForVelocity(pluginConfig,
					exec);
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
					Collection<String> files = getReport().getSuppressedFiles();
					files.add(targetFile);
				} else {
					Collection<String> files = getReport().getGeneratedFiles();
					files.add(targetFile);
				}
			} else {
				Collection<String> files = getReport().getPreservedFiles();
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

}
