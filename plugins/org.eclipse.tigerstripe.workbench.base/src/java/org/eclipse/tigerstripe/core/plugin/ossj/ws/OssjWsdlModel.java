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
package org.eclipse.tigerstripe.core.plugin.ossj.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.Tag;
import org.eclipse.tigerstripe.core.plugin.PluginRef;

/**
 * @author Eric Dillon
 * 
 * TODO: The scope of an XSD produced by Tigerstripe needs to be specified
 * somewhere. As of now, XML Schemas are created per SessionFacade. This needs
 * to be investigated further.
 */
public class OssjWsdlModel {

	public final static String TEMPLATE_PREFIX = "org/eclipse/tigerstripe/core/plugin/ossj/ws/resources";
	private final static String TEMPLATE = "wsdl.vm";
	private final static String XSD_TAG = "tigerstripe.xsd";

	private String destinationDir;
	private String template;

	private AbstractArtifact artifact;

	private String targetNamespace;
	private String schemaLocation;
	private String wsdlNamespace;

	private Collection content = new ArrayList();

	private PluginRef pluginRef;

	public String getTargetNamespace() {
		return this.targetNamespace;
	}

	public String getSchemaLocation() {
		return this.schemaLocation;
	}

	public String getWsdlNamespace() {
		return this.wsdlNamespace;
	}

	/**
	 * Returns the AbstractArtifact used to build this OssjInterfaceModel
	 * 
	 * @return AbstractArtifact - the artifact used to build this model
	 */
	public AbstractArtifact getArtifact() {
		return this.artifact;
	}

	private void setArtifact(AbstractArtifact artifact) {
		this.artifact = artifact;
	}

	public String getName() {
		return this.artifact.getName();
	}

	/**
	 * Default constructor
	 * 
	 */
	protected OssjWsdlModel(AbstractArtifact artifact, PluginRef pluginRef)
			throws TigerstripeException {
		this.pluginRef = pluginRef;
		setTemplate(TEMPLATE_PREFIX + "/" + pluginRef.getActiveVersion() + "/"
				+ TEMPLATE);
		setArtifact(artifact);
		build(artifact.getArtifactManager());
	}

	protected void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setDestinationDir(String absoluteDir) {
		this.destinationDir = absoluteDir;
	}

	/**
	 * Populates this based on the context.
	 * 
	 * @param manager
	 * @param context
	 * @throws TigerstripeException
	 */
	protected void build(ArtifactManager manager) throws TigerstripeException {
		// TODO extract destination directory from context
		this.destinationDir = this.pluginRef.getProject().getProjectDetails()
				.getOutputDirectory();

		// Read the tigerstripe.interface tag
		Tag xsdTag = getArtifact().getFirstTagByName(XSD_TAG);
		if (xsdTag != null) {
			Properties prop = xsdTag.getProperties();
			this.targetNamespace = prop.getProperty("targetNamespace",
					"http://www.ossj.org/xml/" + getArtifact().getName());
			this.wsdlNamespace = prop.getProperty("wsdlLocation",
					"http://www.ossj.org/wsdl/" + getArtifact().getName());
			this.schemaLocation = prop.getProperty("schemaLocation",
					getArtifact().getName() + ".xsd");
		} else {
			this.targetNamespace = "http://www.eclipse.tigerstripe.org/xml/"
					+ getArtifact().getName();
			this.wsdlNamespace = "http://www.eclipse.tigerstripe.org/wsdl/"
					+ getArtifact().getName();
			// set schema to local
			// this.schemaLocation = "http://wwww.eclipse.tigerstripe.org/xml/"
			// + getArtifact().getName() + "/schema.xsd";
			this.schemaLocation = getArtifact().getName() + ".xsd";
		}

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
