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
/**
 * 
 */
package org.eclipse.tigerstripe.eclipse.wizards.project;

import java.util.Map;

import org.eclipse.tigerstripe.core.plugin.PluginBody;

public class NewProjectDetails {

	public String projectName;

	public String projectDirectory;

	public String defaultArtifactPackage = "com.mycompany";

	public String defaultInterfacePackage = "com.mycompany";

	public String defaultXsdNamespace = "tns";

	public String outputDirectory;

	public String nature = PluginBody.OSSJ_NATURE;

	public String dependency;

	public Map reqModules;

	public boolean jvtPlugin;

	public boolean xmlPlugin;

	public boolean wsPlugin;

	public String generateReport;

	public String logMessages;

	public String messagePayloadSampleAllowNetwork;

	public String messagePayloadSampleDefaultlocation;

	public String getNature() {
		return this.nature;
	}

	public String getDefaultXsdNamespace() {
		return this.defaultXsdNamespace;
	}

	public void setDefaultXsdNamespace(String xsdNamepace) {
		this.defaultXsdNamespace = xsdNamepace;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public String getDefaultInterfacePackage() {
		return this.defaultInterfacePackage;
	}

	public String getProjectDirectory() {
		return this.projectDirectory;
	}

	public String getDefaultArtifactPackage() {
		return this.defaultArtifactPackage;
	}

	public void setDefaultArtifactPackage(String defaultArtifactPackage) {
		this.defaultArtifactPackage = defaultArtifactPackage;
	}

	public boolean getJvtPlugin() {
		return this.jvtPlugin;
	}

	public boolean getXmlPlugin() {
		return this.xmlPlugin;
	}

	public boolean getWsPlugin() {
		return this.wsPlugin;
	}

	public String getOutputDirectory() {
		return this.outputDirectory;
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	public Map getReqModules() {
		return reqModules;
	}

	public void setReqModules(Map reqModules) {
		this.reqModules = reqModules;
	}

	public String getGenerateReport() {
		return generateReport;
	}

	public void setGenerateReport(String generateReport) {
		this.generateReport = generateReport;
	}

	public String getLogMessages() {
		return logMessages;
	}

	public void setLogMessages(String logMessages) {
		this.logMessages = logMessages;
	}

	public String getMessagePayloadSampleAllowNetwork() {
		return messagePayloadSampleAllowNetwork;
	}

	public void setMessagePayloadSampleAllowNetwork(
			String messagePayloadSampleAllowNetwork) {
		this.messagePayloadSampleAllowNetwork = messagePayloadSampleAllowNetwork;
	}

	public String getMessagePayloadSampleDefaultlocation() {
		return messagePayloadSampleDefaultlocation;
	}

	public void setMessagePayloadSampleDefaultlocation(
			String messagePayloadSampleDefaultlocation) {
		this.messagePayloadSampleDefaultlocation = messagePayloadSampleDefaultlocation;
	}
}