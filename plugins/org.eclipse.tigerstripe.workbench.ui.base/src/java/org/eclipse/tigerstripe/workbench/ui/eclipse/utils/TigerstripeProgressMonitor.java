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
package org.eclipse.tigerstripe.workbench.ui.eclipse.utils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;

public class TigerstripeProgressMonitor implements ITigerstripeProgressMonitor {

	private IProgressMonitor monitor;

	public TigerstripeProgressMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public void beginTask(String name, int totalWork) {
		monitor.beginTask(name, totalWork);
	}

	public void done() {
		monitor.done();
	}

	public void internalWorked(double work) {
		monitor.internalWorked(work);
	}

	public boolean isCanceled() {
		return monitor.isCanceled();
	}

	public void setCanceled(boolean value) {
		monitor.setCanceled(value);
	}

	public void setTaskName(String name) {
		monitor.setTaskName(name);
	}

	public void subTask(String name) {
		monitor.subTask(name);
	}

	public void worked(int work) {
		monitor.worked(work);
	}

}
