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
package org.eclipse.tigerstripe.workbench.internal.core.model.persist;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.util.encode.XmlEscape;

public abstract class AbstractArtifactPersister {

	private static final Object ENGINE_LOCK = new Object(); 
	private static VelocityEngine engine;
	
	private static VelocityEngine getEngine() throws Exception {
		synchronized(ENGINE_LOCK) {
			if(engine == null) {
				engine = createEngine();
			}
			return engine;
		}
	}

	/**
	 * Initializes the Velocity framework and sets it up with a classpath
	 * loader.
	 * 
	 * @throws Exception,
	 *             if the class loader cannot be set up
	 */
	private static VelocityEngine createEngine() throws Exception {

		VelocityEngine engine = new VelocityEngine();
		Properties properties = new Properties();
		properties.put(RuntimeConstants.RESOURCE_LOADER, "class");
		properties
				.put("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		properties.put("class.resource.loader.cache", "true");
		properties.put("runtime.log", "tigerstripe/velocity.log");
		
		ClassLoader startingLoader = Thread.currentThread().getContextClassLoader();
		try {
			if( engine.getClass().getClassLoader() != Thread.currentThread().getContextClassLoader()) {
				Thread.currentThread().setContextClassLoader(engine.getClass().getClassLoader());
			}
			engine.init(properties);
		} finally {
			Thread.currentThread().setContextClassLoader(startingLoader);
		}
		return engine;
	}

	private IAbstractArtifactInternal artifact;

	/**
	 * The velocity template that will be used to perform the generation
	 */
	private String template;

	/**
	 * The write to write to
	 */
	private Writer writer;

	protected AbstractArtifactPersister(IAbstractArtifactInternal artifact,
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

	protected IAbstractArtifactInternal getArtifact() {
		return artifact;
	}

	protected void setArtifact(IAbstractArtifactInternal artifact) {
		this.artifact = artifact;
	}

	// ========================================================================
	// ========================================================================

	
	public void applyTemplate() throws TigerstripeException {
		try {
			Template template = getEngine().getTemplate(getTemplate());

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
