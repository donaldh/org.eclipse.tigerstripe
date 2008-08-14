/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.patterns;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;

public class Pattern implements IPattern {

	protected String name;
	protected String description;
	protected String uiLabel;
	protected String iconURL;
	protected Collection<IModelChangeRequest> requests = new ArrayList<IModelChangeRequest>();
	
	protected void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUILabel() {
		return uiLabel;
	}

	public void setUILabel(String uiLabel) {
		this.uiLabel = uiLabel;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	

}
