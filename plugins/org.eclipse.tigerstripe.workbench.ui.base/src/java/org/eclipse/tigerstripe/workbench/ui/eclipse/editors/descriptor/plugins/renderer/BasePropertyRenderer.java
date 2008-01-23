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
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProperty;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class BasePropertyRenderer {

	private Composite parent;

	private ITigerstripeProject project;

	private IPluggablePluginProperty property;

	private FormToolkit toolkit;

	private IPluggablePluginPropertyListener persister;

	private boolean silentUpdate;

	public BasePropertyRenderer(Composite parent, FormToolkit toolkit,
			ITigerstripeProject project, IPluggablePluginProperty property,
			IPluggablePluginPropertyListener persister) {
		this.parent = parent;
		this.project = project;
		this.property = property;
		this.toolkit = toolkit;
		this.persister = persister;
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	protected void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	protected boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	protected IPluggablePluginPropertyListener getPersister() {
		return this.persister;
	}

	protected Composite getParent() {
		return this.parent;
	}

	protected ITigerstripeProject getProject() {
		return this.project;
	}

	public IPluggablePluginProperty getProperty() {
		return this.property;
	}

	protected FormToolkit getToolkit() {
		return this.toolkit;
	}

	public abstract void render() throws TigerstripeException;

	public abstract void setEnabled(boolean enabled);

	public abstract void applyDefault();

	public abstract void update(String serializedValue);
}
