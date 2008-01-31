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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.csv;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;

/**
 * @author Eric Dillon
 * 
 * TODO: The scope of an XSD produced by Tigerstripe needs to be specified
 * somewhere. As of now, XML Schemas are created per SessionFacade. This needs
 * to be investigated further.
 */
public class CSVModel {

	private final static String TEMPLATE_PREFIX = "org/eclipse/tigerstripe/workbench/internal/core/plugin/csv/resources";
	private final static String TEMPLATE = "csv.vm";
	private String destinationDir;
	private String template;

	private AbstractArtifact artifact;

	private Collection content = new ArrayList();

	private PluginConfig pluginConfig;

	/**
	 * Returns the AbstractArtifact used to build this OssjInterfaceModel
	 * 
	 * @return AbstractArtifact - the artifact used to build this model
	 */
	public AbstractArtifact getArtifact() {
		return this.artifact;
	}

	public String getName() {
		return this.artifact.getName();
	}

	/**
	 * Default constructor
	 * 
	 */
	protected CSVModel(PluginConfig pluginConfig) throws TigerstripeException {
		this.pluginConfig = pluginConfig;
		setTemplate(TEMPLATE);
		build();
	}

	protected void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return TEMPLATE_PREFIX + "/" + this.template;
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
		this.destinationDir = this.pluginConfig.getProject().getProjectDetails()
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
