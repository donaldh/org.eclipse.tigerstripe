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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.Rule;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IRuleReport;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A rule report is sub-type of report used in pluggable plugins
 * 
 * @author Richard Craddock
 */
public class RuleReport extends PluginReport implements IRuleReport {

	public RuleReport(PluginConfig pluginConfig) {
		super(pluginConfig);
	}

	private String name;
	private String type;
	private boolean enabled;
	private boolean overwriteFiles;
	private boolean suppressEmptyFiles;
	private boolean includeDependencies;
	private Rule rule;

	// CopyRule specifics
	private String filesetMatch;
	private int copyFrom;
	private String toDirectory;

	// Runnable specifics
	private String runnableClassName;

	private Collection<String> suppressedFiles = new ArrayList<String>();
	private Collection<String> preservedFiles = new ArrayList<String>();

	// This block should really be in a ArtifactBased rule Report only..
	private ArrayList<String> matchedArtifacts = new ArrayList<String>();

	private String artifactType;

	// This should really be in a ArtifactBased rule Report only..

	public ArrayList<String> getMatchedArtifacts() {
		return matchedArtifacts;
	}

	public void setArtifactsMatched(ArrayList<String> artifactsMatched) {
		this.matchedArtifacts = artifactsMatched;
	}

	public String getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}
	
	
	// End of block
	
	// This block should really be in a Model rule Report only..
	private ArrayList<ITigerstripeModelProject> modules = new ArrayList<ITigerstripeModelProject>();

	
	public ArrayList<ITigerstripeModelProject> getModules() {
		return modules;
	}

	public void setModules(ArrayList<ITigerstripeModelProject> modules) {
		this.modules = modules;
	}

	
	
	
	// End of block
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isOverwriteFiles() {
		return overwriteFiles;
	}

	public void setOverwriteFiles(boolean overwriteFiles) {
		this.overwriteFiles = overwriteFiles;
	}

	public boolean isSuppressEmptyFiles() {
		return suppressEmptyFiles;
	}

	public void setSuppressEmptyFiles(boolean suppressEmptyFiles) {
		this.suppressEmptyFiles = suppressEmptyFiles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.IRuleReport
	 * #getPreservedFiles()
	 */
	public Collection<String> getPreservedFiles() {
		return preservedFiles;
	}

	public Collection<String> getSuppressedFiles() {
		return suppressedFiles;
	}

	public boolean isIncludeDependencies() {
		return includeDependencies;
	}

	public void setIncludeDependencies(boolean includeDependencies) {
		this.includeDependencies = includeDependencies;
	}

	// =========== for CopyRules
	public String getFilesetMatch() {
		return filesetMatch;
	}

	public void setFilesetMatch(String filesetMatch) {
		this.filesetMatch = filesetMatch;
	}

	public String getCopyFrom() {
		if (copyFrom == ICopyRule.FROM_PLUGIN)
			return "From Plugin";
		else
			return "From Project";
	}

	public void setCopyFrom(int copyFrom) {
		this.copyFrom = copyFrom;
	}

	public void setToDirectory(String toDirectory) {
		this.toDirectory = toDirectory;
	}

	public String getToDirectory() {
		return this.toDirectory;
	}

	public String getRunnableClassName() {
		return runnableClassName;
	}

	public void setRunnableClassName(String runnableClassName) {
		this.runnableClassName = runnableClassName;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public Rule getRule() {
		return rule;
	}
}
