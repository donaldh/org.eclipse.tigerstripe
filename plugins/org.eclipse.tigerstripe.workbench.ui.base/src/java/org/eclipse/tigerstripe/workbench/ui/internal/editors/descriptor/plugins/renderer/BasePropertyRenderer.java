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
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class BasePropertyRenderer {

	private Composite parent;

	private ITigerstripeModelProject project;

	private IPluginProperty property;

	private FormToolkit toolkit;

	private IPluggablePluginPropertyListener persister;

	private boolean silentUpdate;

	public BasePropertyRenderer(Composite parent, FormToolkit toolkit,
			ITigerstripeModelProject project, IPluginProperty property,
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

	protected ITigerstripeModelProject getProject() {
		return this.project;
	}

	public IPluginProperty getProperty() {
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
