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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.util.Dictionary;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;

public abstract class RunnableRule extends Rule implements IRunnableRule{
	
	private boolean suppressEmptyFiles = true;

	private boolean overwriteFiles = true;

	private String runnableClassName = "";
	
	private Map<String, Object> context;
	
	
	public boolean isSuppressEmptyFiles() {
		return suppressEmptyFiles;
	}

	public String isSuppressEmptyFilesStr() {
		return Boolean.toString(suppressEmptyFiles);
	}

	public void setSuppressEmptyFiles(boolean suppressEmptyFiles) {
		markDirty();
		this.suppressEmptyFiles = suppressEmptyFiles;
	}

	public void setSuppressEmptyFilesStr(String suppressEmptyFilesStr) {
		markDirty();
		this.suppressEmptyFiles = Boolean.parseBoolean(suppressEmptyFilesStr);
	}

	public boolean isOverwriteFiles() {
		return overwriteFiles;
	}

	public String isOverwriteFilesStr() {
		return Boolean.toString(overwriteFiles);
	}

	public void setOverwriteFiles(boolean overwriteFiles) {
		markDirty();
		this.overwriteFiles = overwriteFiles;
	}

	public void setOverwriteFilesStr(String overwriteFilesStr) {
		markDirty();
		this.overwriteFiles = Boolean.parseBoolean(overwriteFilesStr);
	}

	public String getRunnableClassName() {
		return runnableClassName;
	}

	public void setRunnableClassName(String runnableClassName) {
		markDirty();
		this.runnableClassName = runnableClassName;
	}
	
	public void setContext(Map<String, Object> context){
		this.context = context;
	}

	public Map<String, Object> getContext() {
		return context;
	}
	
	
	
}
