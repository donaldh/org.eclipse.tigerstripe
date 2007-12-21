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
package org.eclipse.tigerstripe.eclipse.wizards.pluggablePlugin;

import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;

/**
 * @author Eric Dillon
 * @since 1.2
 */
public class NewTigerstripePluginDescriptorGenerator {

	private final static String TEMPLATE = "org/eclipse/tigerstripe/core/project/pluggable/wizard/resources/ts-plugin.vm";

	/**
	 * The velocity template that will be used to perform the generation
	 */
	private String template;

	/**
	 * The write to write to
	 */
	private Writer writer;

	/**
	 * The properties coming from the wizard
	 */
	private NewPluginProjectWizardPage.NewProjectDetails projectDetails;

	// ========================================================================
	// ========================================================================
	// Default constructor

	public NewTigerstripePluginDescriptorGenerator(
			NewPluginProjectWizardPage.NewProjectDetails projectDetails,
			Writer writer) {
		setTemplate(TEMPLATE);
		setProjectDetails(projectDetails);
		setWriter(writer);
	}

	// ========================================================================
	// ========================================================================
	// Getters and Setters
	// ========================================================================

	/**
	 * Sets the calling wizard
	 */
	public void setProjectDetails(
			NewPluginProjectWizardPage.NewProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}

	/**
	 * gets the calling wizard
	 * 
	 * @return NewArtifact
	 */
	public NewPluginProjectWizardPage.NewProjectDetails getProjectDetails() {
		return this.projectDetails;
	}

	/**
	 * Sets the path to the template to use for this generation
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Returns the path to the template to use for this generation
	 * 
	 * @return
	 */
	public String getTemplate() {
		return this.template;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	public Writer getWriter() {
		return writer;
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Initializes the Velocity framework and sets it up with a classpath
	 * loader.
	 * 
	 * @throws Exception,
	 *             if the class loader cannot be set up
	 */
	protected void setClasspathLoaderForVelocity() throws Exception {
		Properties properties = new Properties();
		properties.put("resource.loader", "class");
		properties
				.put("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Velocity.init(properties);
	}

	protected void applyTemplate() {
		try {
			setClasspathLoaderForVelocity();
			Template template = Velocity.getTemplate(getTemplate());

			VelocityContext context = new VelocityContext();
			context.put("details", getProjectDetails());
			template.merge(context, getWriter());
			getWriter().flush();
			getWriter().close();

		} catch (Exception e) {
			EclipsePlugin.log(e);
		}
	}
}
