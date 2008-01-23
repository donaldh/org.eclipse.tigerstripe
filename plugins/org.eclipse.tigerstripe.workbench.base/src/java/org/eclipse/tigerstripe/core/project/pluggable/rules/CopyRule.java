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
package org.eclipse.tigerstripe.core.project.pluggable.rules;

import java.io.File;
import java.io.IOException;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.plugins.pluggable.ICopyRule;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluginRuleExecutor;
import org.eclipse.tigerstripe.core.plugin.Expander;
import org.eclipse.tigerstripe.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.core.plugin.pluggable.PluggablePluginRef;
import org.eclipse.tigerstripe.core.plugin.pluggable.RuleReport;
import org.eclipse.tigerstripe.core.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CopyRule extends BasePPluginRule implements ICopyRule {

	private final static String REPORTTEMPLATE = "ICopyRule.vm";

	public final static String LABEL = "File/Directory Copy Rule";

	private int copyFrom = ICopyRule.FROM_PLUGIN;

	private String filesetMatch = "";

	private String toDirectory = "";

	private RuleReport report;

	public int getCopyFrom() {
		return copyFrom;
	}

	public String getFilesetMatch() {
		return filesetMatch;
	}

	public String getToDirectory() {
		return toDirectory;
	}

	public void setCopyFrom(int from) {
		copyFrom = from;
	}

	public void setFilesetMatch(String filesetMatch) {
		this.filesetMatch = filesetMatch;
	}

	public void setToDirectory(String toDirectory) {
		this.toDirectory = toDirectory;
	}

	public RuleReport getReport() {
		return report;
	}

	@Override
	public void buildBodyFromNode(Node node) {
		Element elm = (Element) node;
		NodeList bodies = elm.getElementsByTagName("body");
		if (bodies.getLength() != 0) {
			Element body = (Element) bodies.item(0);
			setToDirectory(body.getAttribute("toDirectory"));
			setFilesetMatch(body.getAttribute("filesetMatch"));
			setCopyFrom(Integer.parseInt(body.getAttribute("copyFrom")));
		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		Element elm = document.createElement("body");
		elm.setAttribute("toDirectory", getToDirectory());
		elm.setAttribute("filesetMatch", getFilesetMatch());
		elm.setAttribute("copyFrom", String.valueOf(getCopyFrom()));
		return elm;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return ICopyRule.class.getCanonicalName();
	}

	public void trigger(PluggablePluginRef pluginRef, IPluginRuleExecutor exec)
			throws TigerstripeException {

		this.report = new RuleReport(pluginRef);
		this.report.setTemplate(PluggablePlugin.TEMPLATE_PREFIX + "/"
				+ REPORTTEMPLATE);
		this.report.setName(getName());
		this.report.setType(getLabel());
		this.report.setEnabled(isEnabled());
		this.report.setFilesetMatch(filesetMatch);
		this.report.setCopyFrom(getCopyFrom());

		Expander expander = new Expander(pluginRef);
		String expandedToDir = expander.expandVar(getToDirectory(), pluginRef
				.getProject());
		this.report.setToDirectory(expandedToDir);

		// Get target absolute directory (Created it if needed)
		String outputPath = "";
		File outputDirectory = null;
		try {
			String outputDir = pluginRef.getProjectHandle().getProjectDetails()
					.getOutputDirectory();
			String projectDir = pluginRef.getProjectHandle().getBaseDir()
					.getCanonicalPath();

			outputPath = projectDir + File.separator + outputDir;
			if (exec.getConfig() != null
					&& exec.getConfig().getAbsoluteOutputDir() != null) {
				outputPath = exec.getConfig().getAbsoluteOutputDir()
						+ File.separator + outputDir;
			}

			// create any subdir in the outputDir if any is included
			// in the outputFile
			outputDirectory = new File(outputPath + File.separator
					+ expandedToDir);
			if (!outputDirectory.exists()) {
				outputDirectory.mkdirs();
			}
		} catch (IOException e) {
			throw new TigerstripeException("Error while trying to create '"
					+ outputPath + "': " + e.getMessage());
		}

		String srcPrefix = pluginRef.getProjectHandle().getBaseDir()
				.getAbsolutePath();
		if (getCopyFrom() == ICopyRule.FROM_PLUGIN) {
			srcPrefix = exec.getPlugin().getPProject().getBaseDir()
					.getAbsolutePath();
		}

		if (getFilesetMatch().indexOf("*") != -1) {
			// there is a wildcard in there...
			String newFileset = srcPrefix + File.separator + getFilesetMatch();
			int index = newFileset.indexOf("*");
			String srcDirStr = newFileset.substring(0, index - 1);
			String includes = newFileset.substring(index);
			File srcDir = new File(srcDirStr);
			try {
				FileUtils.copyFileset(srcDir.getAbsolutePath(), outputDirectory
						.getAbsolutePath(), includes, true);
			} catch (IOException e) {
				throw new TigerstripeException("Error while copying fileset: " + e.getMessage(), e);
			}
		} else {
			File srcFile = new File(srcPrefix + File.separator
					+ getFilesetMatch());
			if (!srcFile.exists()) {
				String src = "project '"
						+ pluginRef.getProjectHandle().getProjectLabel() + "'";
				if (getCopyFrom() == ICopyRule.FROM_PLUGIN) {
					src = "deployed plugin.";
				}
				throw new TigerstripeException("Cannot find '"
						+ getFilesetMatch() + "' for copy in " + src);
			} else if (srcFile.isFile()) {
				this.report.getCopiedFiles().add(srcFile.getAbsolutePath());
				try {
					FileUtils.copy(srcFile.getAbsolutePath(), outputDirectory
							.getAbsolutePath(), true);
				} catch (IOException e) {
					throw new TigerstripeException(
							"Error while trying to copy "
									+ srcFile.getAbsolutePath() + ":"
									+ e.getMessage(), e);
				}
			} else if (srcFile.isDirectory()) {
				this.report.getCopiedFiles().add(srcFile.getAbsolutePath());
				File newTarget = new File(outputDirectory.getAbsolutePath()
						+ File.separator + srcFile.getName());
				try {
					FileUtils.copyDir(srcFile.getAbsolutePath(), newTarget
							.getAbsolutePath(), true);
				} catch (IOException e) {
					throw new TigerstripeException(
							"Error while copying directory "
									+ srcFile.getAbsolutePath() + ": "
									+ e.getMessage(), e);
				}
			}
		}
	}
}
