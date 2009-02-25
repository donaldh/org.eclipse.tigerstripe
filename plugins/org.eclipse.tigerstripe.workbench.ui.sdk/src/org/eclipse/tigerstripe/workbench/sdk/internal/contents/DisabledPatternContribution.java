/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.contents;

import org.eclipse.pde.core.plugin.IPluginModelBase;


public class DisabledPatternContribution {

	public DisabledPatternContribution(IPluginModelBase contributor,
			String disabledPatternName, boolean readOnly) {
		super();
		this.contributor = contributor;
		this.disabledPatternName = disabledPatternName;
		this.readOnly = readOnly;
	}
	private IPluginModelBase contributor;
	private String disabledPatternName;
	private boolean readOnly;
	public IPluginModelBase getContributor() {
		return contributor;
	}
	public void setContributor(IPluginModelBase contributor) {
		this.contributor = contributor;
	}
	public String getDisabledPatternName() {
		return disabledPatternName;
	}
	public void setDisabledPatternName(String disabledPatternName) {
		this.disabledPatternName = disabledPatternName;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
}
