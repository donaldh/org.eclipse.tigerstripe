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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.session;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SessionDefinitionGenerator extends ArtifactDefinitionGenerator {

	private final static String TEMPLATE = VM_TEMPLATE_BASE + "session.vm";

	private Collection entityOptions;
	private Collection queries;
	private Collection updateProcedures;
	private Collection events;

	/**
	 * We need to set up a few things: - the template to use - the path to the
	 * file that will be created - the calling wizard so it can be placed into
	 * the velocity context for generation
	 * 
	 * DO NOT USE, to make the compiler happy only
	 */
	public SessionDefinitionGenerator(Properties properties, Writer writer) {
		this(new ArrayList(), new ArrayList(), new ArrayList(),
				new ArrayList(), properties, writer);
	}

	public SessionDefinitionGenerator(Collection entityOptions,
			Collection queries, Collection updateProcedures, Collection events,
			Properties properties, Writer writer) {
		super();
		setTemplate(TEMPLATE);
		setWizardProperties(properties);
		setWriter(writer);

		this.entityOptions = entityOptions;
		this.queries = queries;
		this.updateProcedures = updateProcedures;
		this.events = events;
	}

	@Override
	public void applyTemplate() {
		try {
			setClasspathLoaderForVelocity();
			Template template = Velocity.getTemplate(getTemplate());

			VelocityContext context = new VelocityContext();
			context.put("entityOptions", this.entityOptions);
			context.put("queries", this.queries);
			context.put("updateProcedures", this.updateProcedures);
			context.put("events", this.events);
			context.put("wizard", getWizardProperties());
			context.put("eclipseUtils", new EclipseUtils());
			template.merge(context, getWriter());
			getWriter().flush();
			getWriter().close();

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}
}