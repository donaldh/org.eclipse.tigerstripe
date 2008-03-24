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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BaseContainerObject;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.IContainerObject;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.RuleReport;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.plugins.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class Rule extends BaseContainerObject implements
		IContainedObject, IContainerObject, IRule {

	private boolean enabled = true;

	private String name = "";

	private String description = "";

	private RuleReport report;

	// =====================================================================
	// IContainedObject
	private boolean isLocalDirty = false;
	private IContainerObject container = null;

	public void setContainer(IContainerObject container) {
		isLocalDirty = false;
		this.container = container;
	}

	public void clearDirty() {
		isLocalDirty = false;
	}

	public boolean isDirty() {
		return isLocalDirty;
	}

	protected abstract String getReportTemplatePath();

	protected void initializeReport(PluggablePluginConfig pluginConfig) {
		this.report = new RuleReport(pluginConfig);
		this.report.setTemplate(getReportTemplatePath());
		this.report.setName(getName());
		this.report.setType(getLabel());
		this.report.setEnabled(isEnabled());
	}

	/**
	 * Marks this object as dirty and notify the container if any
	 * 
	 */
	protected void markDirty() {
		isLocalDirty = true;
		if (container != null) {
			container.notifyDirty(this);
		}
	}

	public IContainerObject getContainer() {
		return container;
	}

	// =====================================================================
	// =====================================================================

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty();
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		markDirty();
		this.description = description;
	}

	public abstract String getLabel();

	public abstract Node getBodyAsNode(Document document);

	public abstract void buildBodyFromNode(Node node);

	public GeneratorProjectDescriptor getContainingDescriptor() {
		return (GeneratorProjectDescriptor) getContainer();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		markDirty();
		this.enabled = enabled;
	}

	public String isEnabledStr() {
		return Boolean.toString(enabled);
	}

	public void setEnabledStr(String enabledStr) {
		setEnabled(Boolean.parseBoolean(enabledStr));
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof IRule) {
			IRule other = (IRule) arg0;
			if (other.getName() != null)
				return other.getName().equals(getName());
		}
		return false;
	}

	public abstract void trigger(PluggablePluginConfig pluginConfig,
			IPluginRuleExecutor exec) throws TigerstripeException;

	public RuleReport getReport() {
		return report;
	}

}
