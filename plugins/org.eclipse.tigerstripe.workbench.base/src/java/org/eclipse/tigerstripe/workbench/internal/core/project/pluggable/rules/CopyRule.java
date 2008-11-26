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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.util.FileUtils;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CopyRule extends Rule implements ICopyRule, IGlobalRule {

	private final static String REPORTTEMPLATE = "ICopyRule.vm";

	public final static String LABEL = "File/Directory Copy Rule";

	private int copyFrom = ICopyRule.FROM_PLUGIN;

	private String filesetMatch = "";

	private String toDirectory = "";

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
		markDirty();
		copyFrom = from;
	}

	public void setFilesetMatch(String filesetMatch) {
		markDirty();
		this.filesetMatch = filesetMatch;
	}

	public void setToDirectory(String toDirectory) {
		markDirty();
		this.toDirectory = toDirectory;
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
	protected String getReportTemplatePath() {
		return PluggablePlugin.TEMPLATE_PREFIX + "/" + REPORTTEMPLATE;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return ICopyRule.class.getCanonicalName();
	}

	protected void initializeReport(PluggablePluginConfig pluginConfig) {
		super.initializeReport(pluginConfig);
		getReport().setFilesetMatch(filesetMatch);
		getReport().setCopyFrom(getCopyFrom());

	}

	public void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException {

		initializeReport(pluginConfig);

		Expander expander = new Expander(pluginConfig);
		String expandedToDir = expander.expandVar(getToDirectory(),
				pluginConfig.getProject());

		getReport().setToDirectory(expandedToDir);

		// Get target absolute directory (Created it if needed)
		String outputPath = "";
		File outputDirectory = null;
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

		outputPath = projectDir + File.separator + outputDir;
		if (exec.getConfig() != null
				&& exec.getConfig().getAbsoluteOutputDir() != null) {
			outputPath = exec.getConfig().getAbsoluteOutputDir()
					+ File.separator + outputDir;
		}

		// create any subdir in the outputDir if any is included
		// in the outputFile
		outputDirectory = new File(outputPath + File.separator + expandedToDir);
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}

		String srcPrefix = pluginConfig.getProjectHandle().getLocation()
				.toOSString();
		if (getCopyFrom() == ICopyRule.FROM_PLUGIN) {
			srcPrefix = exec.getPlugin().getPProject().getBaseDir()
					.getAbsolutePath();
		}

		String expandedFromDir = expander.expandVar(getFilesetMatch(),
				pluginConfig.getProject());

		if (expandedFromDir.indexOf("*") != -1) {
			// there is a wildcard in there...
			String newFileset = srcPrefix + File.separator + expandedFromDir;
			int index = newFileset.indexOf("*");
			String srcDirStr = newFileset.substring(0, index - 1);
			String includes = newFileset.substring(index);
			File srcDir = new File(srcDirStr);
			try {
				FileUtils.copyFileset(srcDir.getAbsolutePath(), outputDirectory
						.getAbsolutePath(), includes, true);
			} catch (IOException e) {
				throw new TigerstripeException("Error while copying fileset: "
						+ e.getMessage(), e);
			}
		} else {
			File srcFile = new File(srcPrefix + File.separator
					+ expandedFromDir);
			if (!srcFile.exists()) {
				String src = "project '"
						+ pluginConfig.getProjectHandle().getName() + "'";
				if (getCopyFrom() == ICopyRule.FROM_PLUGIN) {
					src = "deployed plugin.";
				}
				throw new TigerstripeException("Cannot find '"
						+ expandedFromDir + "' for copy in " + src);
			} else if (srcFile.isFile()) {
				getReport().getCopiedFiles().add(srcFile.getAbsolutePath());
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
				getReport().getCopiedFiles().add(srcFile.getAbsolutePath());
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
