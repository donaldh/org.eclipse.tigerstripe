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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties;

import org.eclipse.tigerstripe.workbench.internal.AbstractContainedObject;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class BasePPluginProperty extends AbstractContainedObject
		implements IPluginProperty, IContainedObject {

	private String name = "";
	private String tipToolText = "";

	protected BasePPluginProperty() {
	}

	protected BasePPluginProperty(BasePPluginProperty other) {
		setName(other.getName());
		setTipToolText(other.getTipToolText());
		setProject(other.getProject());
	}

	private ITigerstripeGeneratorProject project;

	private Object defaultValue;

	public void setProject(ITigerstripeGeneratorProject project) {
		this.project = project;
	}

	public ITigerstripeGeneratorProject getProject() {
		return this.project;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty();
	}

	public Object getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(Object value) {
		this.defaultValue = value;
		markDirty();
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof BasePPluginProperty) {
			BasePPluginProperty prop = (BasePPluginProperty) arg0;
			return prop.getName() != null && prop.getName().equals(getName());
		}
		return false;
	}

	public abstract Node getBodyAsNode(Document document);

	public abstract void buildBodyFromNode(Node node);

	public String getTipToolText() {
		return tipToolText;
	}

	public void setTipToolText(String text) {
		tipToolText = text;
		markDirty();
	}

	public abstract Object clone();
}
