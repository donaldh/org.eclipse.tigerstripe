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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins.renderer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.plugins.IBooleanPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IStringPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Renders a IPluggablePluginProperty with the corresponding set of widgets
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PropertyRendererFactory {

	private Composite parent;
	private FormToolkit toolkit;
	private IPluggablePluginPropertyListener persister;
	private ITigerstripeModelProject project;

	public PropertyRendererFactory(Composite parent, FormToolkit toolkit,
			ITigerstripeModelProject project,
			IPluggablePluginPropertyListener persister) {
		this.parent = parent;
		this.toolkit = toolkit;
		this.persister = persister;
	}

	public BasePropertyRenderer getRenderer(IPluginProperty property)
			throws TigerstripeException {
		if (property instanceof IStringPluginProperty)
			return new StringPropertyRenderer(parent, toolkit, project,
					property, persister);
		else if (property instanceof IBooleanPluginProperty)
			return new BooleanPropertyRenderer(parent, toolkit, project,
					property, persister);
		else if (property instanceof ITablePluginProperty)
			return new TablePropertyRenderer(parent, toolkit, project,
					property, persister);

		throw new TigerstripeException("Unable to render property '"
				+ property.getName() + "'");
	}
}
