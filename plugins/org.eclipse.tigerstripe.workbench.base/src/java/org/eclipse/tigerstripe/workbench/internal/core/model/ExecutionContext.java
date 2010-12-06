/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ExecutionContext {

	private final IProgressMonitor monitor;
	private final Set<String> referencesCycle = new HashSet<String>();
	private final Set<String> installedModuleCycle = new HashSet<String>();

	public ExecutionContext(IProgressMonitor monitor) {
		this.monitor = CheckUtils.notNull(monitor, "monitor");
	}

	public IProgressMonitor getMonitor() {
		return monitor;
	}

	public boolean addInGenericReferencesCycle(ITigerstripeModelProject project) {
		try {
			return referencesCycle.add(project.getModelId());
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return false;
		}
	}

	public boolean addInInstalledModulesCycle(ITigerstripeModelProject project) {
		try {
			return installedModuleCycle.add(project.getModelId());
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return false;
		}
	}
}
