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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.renderer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IBooleanPPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IStringPPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePPluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
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
	private ITigerstripeProject project;

	public PropertyRendererFactory(Composite parent, FormToolkit toolkit,
			ITigerstripeProject project,
			IPluggablePluginPropertyListener persister) {
		this.parent = parent;
		this.toolkit = toolkit;
		this.persister = persister;
	}

	public BasePropertyRenderer getRenderer(IPluggablePluginProperty property)
			throws TigerstripeException {
		if (property instanceof IStringPPluginProperty)
			return new StringPropertyRenderer(parent, toolkit, project,
					property, persister);
		else if (property instanceof IBooleanPPluginProperty)
			return new BooleanPropertyRenderer(parent, toolkit, project,
					property, persister);
		else if (property instanceof ITablePPluginProperty)
			return new TablePropertyRenderer(parent, toolkit, project,
					property, persister);

		throw new TigerstripeException("Unable to render property '"
				+ property.getName() + "'");
	}
}
