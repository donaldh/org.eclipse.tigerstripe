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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.base;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;

/**
 * @author Eric Dillon
 * 
 */
public class ReportModel {

	private final static String TEMPLATE_PREFIX = "org/eclipse/tigerstripe/core/plugin/base/resources";
	private final static String TEMPLATE = "Report.vm";
	// private final static String XSD_TAG = "tigerstripe.xsd";

	private String destinationDir;
	private String template;

	private Collection content = new ArrayList();

	// private PluginRef pluginRef;
	private TigerstripeProject project;

	/**
	 * Default constructor
	 * 
	 */
	public ReportModel(TigerstripeProject project) throws TigerstripeException {
		this.project = project;
		setTemplate(TEMPLATE);
		build();
	}

	protected void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return TEMPLATE_PREFIX + "/" + this.template;
	}

	public TigerstripeProject getProject() {
		return this.project;
	}

	public String getName() {
		return "TigerstripeReport.xml";
	}

	/**
	 * Populates this based on the context.
	 * 
	 * @param manager
	 * @param context
	 * @throws TigerstripeException
	 */
	protected void build() throws TigerstripeException {
		// TODO extract destination directory from context
		this.destinationDir = this.project.getProjectDetails()
				.getOutputDirectory();

	}

	public String getDestinationDir() {
		return this.destinationDir;
	}

	/**
	 * Adds the collection of artifacts to the content of this model so we can
	 * later assess the references...
	 * 
	 * @param artifacts
	 */
	public void addContent(Collection artifacts) {
		content.addAll(artifacts);
	}

	public void addContent(AbstractArtifact artifact) {
		content.add(artifact);
	}

	public Collection getContent() {
		return content;
	}
}
