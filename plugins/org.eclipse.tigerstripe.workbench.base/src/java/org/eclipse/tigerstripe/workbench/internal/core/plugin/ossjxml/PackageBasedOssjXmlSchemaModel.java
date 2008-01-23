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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.ossjxml;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PackageToSchemaMapper.PckXSDMapping;

/**
 * @author Eric Dillon
 * 
 * TODO: The scope of an XSD produced by Tigerstripe needs to be specified
 * somewhere. As of now, XML Schemas are created per SessionFacade. This needs
 * to be investigated further.
 */
public class PackageBasedOssjXmlSchemaModel {

	public final static String TEMPLATE_PREFIX = "org/eclipse/tigerstripe/workbench/internal/core/plugin/ossjxml/resources";

	private final static String TEMPLATE = "XMLSchema.vm";
	private final static String COMMON_TEMPLATE = "XMLSchemaNoOperations.vm";
	private final static String OPERATION_TEMPLATE = "XMLSchemaOperationsOnly.vm";

	private Collection content = new ArrayList();

	private String template;
	private String commonTemplate;
	private String operationTemplate;

	private PckXSDMapping mapping;

	private PluginRef pluginRef;

	public String getTargetNamespace() {
		return mapping.getTargetNamespace();
	}

	public String getSchemaLocation() {
		return mapping.getXsdLocation();
	}

	public String getName() {
		return mapping.getXsdName();
	}

	/**
	 * Default constructor
	 * 
	 */
	protected PackageBasedOssjXmlSchemaModel(PckXSDMapping mapping,
			PluginRef pluginRef) throws TigerstripeException {
		this.pluginRef = pluginRef;
		setTemplate(TEMPLATE_PREFIX + "/" + pluginRef.getActiveVersion() + "/"
				+ TEMPLATE);
		setCommonTemplate(TEMPLATE_PREFIX + "/" + pluginRef.getActiveVersion()
				+ "/" + COMMON_TEMPLATE);
		setOperationTemplate(TEMPLATE_PREFIX + "/"
				+ pluginRef.getActiveVersion() + "/" + OPERATION_TEMPLATE);
		this.mapping = mapping;
	}

	public PckXSDMapping getMapping() {
		return this.mapping;
	}

	protected void setTemplate(String template) {
		this.template = template;
	}

	protected void setCommonTemplate(String template) {
		this.commonTemplate = template;
	}

	protected void setOperationTemplate(String template) {
		this.operationTemplate = template;
	}

	public String getTemplate() {
		return this.template;
	}

	public String getCommonTemplate() {
		return this.commonTemplate;
	}

	public String getOperationTemplate() {
		return this.operationTemplate;
	}

	public String getDestinationDir() {
		return pluginRef.getProject().getProjectDetails().getOutputDirectory();
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

	public Collection getContent() {
		return content;
	}

}
