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
package org.eclipse.tigerstripe.core.model.persist;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.util.encode.XmlEscape;

public abstract class AbstractArtifactPersister {

	private AbstractArtifact artifact;

	/**
	 * The velocity template that will be used to perform the generation
	 */
	private String template;

	/**
	 * The write to write to
	 */
	private Writer writer;

	protected AbstractArtifactPersister(AbstractArtifact artifact,
			String template, Writer writer) {
		setArtifact(artifact);
		setTemplate(template);
		setWriter(writer);
	}

	// ========================================================================
	// ========================================================================
	// Getters and Setters
	// ========================================================================

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

	protected AbstractArtifact getArtifact() {
		return artifact;
	}

	protected void setArtifact(AbstractArtifact artifact) {
		this.artifact = artifact;
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
	protected VelocityEngine setClasspathLoaderForVelocity() throws Exception {

		VelocityEngine engine = new VelocityEngine();
		Properties properties = new Properties();
		properties.put("resource.loader", "class");
		properties
				.put("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		engine.init(properties);
		return engine;
	}

	public void applyTemplate() throws TigerstripeException {
		try {
			VelocityEngine engine = setClasspathLoaderForVelocity();
			Template template = engine.getTemplate(getTemplate());

			VelocityContext context = new VelocityContext();
			context.put("artifact", getArtifact());
			context.put("utils", new PersistUtils());
			context.put("runtime", TigerstripeRuntime.getInstance());

			XmlEscape xmlEncode = new XmlEscape();
			context.put("xmlEncode", xmlEncode);
			template.merge(context, getWriter());

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			e.printStackTrace();
			throw new TigerstripeException("Error while persisting artifact "
					+ getArtifact().getFullyQualifiedName() + ": "
					+ e.getMessage(), e);
		} finally {
			try {
				getWriter().flush();
				getWriter().close();
			} catch (IOException e) {
				// ignore
				throw new TigerstripeException(
						"IO Error while persisting artifact: " + getArtifact(),
						e);
			}
		}
	}
}
