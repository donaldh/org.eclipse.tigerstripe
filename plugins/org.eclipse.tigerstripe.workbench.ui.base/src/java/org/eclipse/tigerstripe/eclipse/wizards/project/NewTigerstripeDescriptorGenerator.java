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
package org.eclipse.tigerstripe.eclipse.wizards.project;

import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;

/**
 * @author Eric Dillon
 * 
 * TODO: this needs to be removed. The project descriptor should be saved
 * directly rather than using velocity here. This is old legacy code.
 * 
 */
public class NewTigerstripeDescriptorGenerator {

	private final static String TEMPLATE = "org/eclipse/tigerstripe/core/project/wizard/resources/tigerstripe.vm";

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
	private NewProjectDetails projectDetails;

	// ========================================================================
	// ========================================================================
	// Default constructor

	public NewTigerstripeDescriptorGenerator(NewProjectDetails projectDetails,
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
	public void setProjectDetails(NewProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}

	/**
	 * gets the calling wizard
	 * 
	 * @return NewArtifact
	 */
	public NewProjectDetails getProjectDetails() {
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

	public void applyTemplate() {
		try {
			setClasspathLoaderForVelocity();
			Template template = Velocity.getTemplate(getTemplate());

			VelocityContext context = new VelocityContext();
			context.put("details", getProjectDetails());
			template.merge(context, getWriter());
			getWriter().flush();
			getWriter().close();

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}
	// {
	// try {
	// setClasspathLoaderForVelocity();
	//
	// Template template = Velocity
	// .getTemplate("org/tigerstripe/wizards/session/resources/sessionDefinition.vm");
	//
	// VelocityContext context = new VelocityContext();
	// context.put("wizard", wizard);
	//
	// // Create a collection with the entity names so it's accessible from
	// // the
	// // velocity template
	// Collection entities = new ArrayList();
	// List outputList = wizard.getEntitySelectorPage().getOutputList();
	// String[] names = outputList.getItems();
	// for (int i = 0; i < names.length; i++) {
	// entities.add(names[i]);
	// }
	// context.put("entities", entities);
	//
	// // Create a collection with the query names so it's accessible from
	// // the
	// // velocity template
	// Collection queries = new ArrayList();
	// outputList = wizard.getQuerySelectorPage().getOutputList();
	// names = outputList.getItems();
	// for (int i = 0; i < names.length; i++) {
	// queries.add(names[i]);
	// }
	// context.put("queries", queries);
	// String interfaceName = wizard.getDetailsPage().getInterfaceName();
	// String packageName = wizard.getDetailsPage().getInterfacePackage();
	//
	// if ("".equals(packageName)) {
	// packageName = "idc";
	// } else {
	// packageName = packageName + ".idc";
	// }
	//
	// String targetClass = packageName + "." + interfaceName;
	// targetClass = targetClass.replace('.', File.separatorChar);
	// setDefaultDestination(new File("main/src/java" + File.separator
	// + targetClass + ".java"));
	//
	// // create the output
	// template.merge(context, getDefaultWriter());
	//
	// getDefaultWriter().close();
	// } catch (Exception e) {
	// TigerstripeRuntime.logErrorMessage("Exception detected", e);
	// }

}
