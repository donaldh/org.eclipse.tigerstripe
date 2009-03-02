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


public class PatternFileContribution implements IContribution{

	public PatternFileContribution(IPluginModelBase contributor, String fileName,
			String validatorClass, boolean readOnly) {
		super();
		this.contributor = contributor;
		this.fileName = fileName;
		this.validatorClass = validatorClass;
		this.readOnly = readOnly;
	}
	
	private IPluginModelBase contributor;
	private String fileName;
	private String validatorClass;
	private boolean readOnly;
	
	public IPluginModelBase getContributor() {
		return contributor;
	}
	public void setContributor(IPluginModelBase contributor) {
		this.contributor = contributor;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getValidatorClass() {
		return validatorClass;
	}
	public void setValidatorClass(String validatorClass) {
		this.validatorClass = validatorClass;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}


	
	
}
