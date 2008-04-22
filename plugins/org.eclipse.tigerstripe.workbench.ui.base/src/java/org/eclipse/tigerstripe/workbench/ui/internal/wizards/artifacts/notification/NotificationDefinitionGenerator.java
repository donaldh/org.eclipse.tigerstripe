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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.notification;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NotificationDefinitionGenerator extends
		ArtifactDefinitionGenerator {

	private final static String TEMPLATE = VM_TEMPLATE_BASE + "notification.vm";

	/**
	 * We need to set up a few things: - the template to use - the path to the
	 * file that will be created - the calling wizard so it can be placed into
	 * the velocity context for generation
	 */
	public NotificationDefinitionGenerator(Properties properties, Writer writer) {
		super();
		setTemplate(TEMPLATE);
		setWizardProperties(properties);
		setWriter(writer);
	}

}
