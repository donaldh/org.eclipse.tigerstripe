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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts;

import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.tigerstripe.eclipse.wizards.WizardUtils;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;

/**
 * @author Eric Dillon
 * 
 */
public abstract class ArtifactDefinitionGenerator {

	public static String VM_TEMPLATE_BASE = "org/eclipse/tigerstripe/workbench/internal/core/model/wizards/resources/";

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
	private Properties wizardProperties;

	// ========================================================================
	// ========================================================================
	// Default constructor

	public ArtifactDefinitionGenerator() {
	}

	// ========================================================================
	// ========================================================================
	// Getters and Setters
	// ========================================================================

	/**
	 * Sets the calling wizard
	 */
	public void setWizardProperties(Properties wizardProperties) {
		this.wizardProperties = wizardProperties;
	}

	/**
	 * gets the calling wizard
	 * 
	 * @return NewArtifact
	 */
	public Properties getWizardProperties() {
		return this.wizardProperties;
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
			context.put("wizard", getWizardProperties());
			context.put("wizardUtils", new WizardUtils());
			context.put("eclipseUtils", new EclipseUtils());
			context.put("runtime", TigerstripeRuntime.getInstance());
			template.merge(context, getWriter());
			getWriter().flush();
			getWriter().close();

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}

	public class EclipseUtils {

		public String intToDim(int dim) {
			String result = "";
			for (int i = 0; i < dim; i++) {
				result = result + "[]";
			}
			return result;
		}

		private String[] modifierMap = { "public", "private", "protected", "" };

		public String intToModifier(int dim) {
			if (dim > 3) {
				dim = 0;
			}
			return modifierMap[dim];
		}
	}
}