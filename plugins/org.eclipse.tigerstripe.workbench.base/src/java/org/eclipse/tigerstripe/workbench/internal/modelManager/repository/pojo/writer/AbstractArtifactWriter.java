/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.PersistUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.encode.XmlEscape;

public abstract class AbstractArtifactWriter {

	private IAbstractArtifact artifact;

	private Writer writer;

	private String template;

	protected AbstractArtifactWriter() {
		this(null);
	}

	protected AbstractArtifactWriter(IAbstractArtifact artifact) {
		setArtifact(artifact);
	}

	public void setArtifact(IAbstractArtifact artifact) {
		this.artifact = artifact;
	}

	protected IAbstractArtifact getArtifact() {
		return this.artifact;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	protected Writer getWriter() {
		return this.writer;
	}

	protected void setTemplate(String template) {
		this.template = template;
	}

	protected String getTemplate() {
		return this.template;
	}

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
			} catch (IOException e) {
				// ignore
				throw new TigerstripeException(
						"IO Error while persisting artifact: " + getArtifact(),
						e);
			}
		}
	}

}
