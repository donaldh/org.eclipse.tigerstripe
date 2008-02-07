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
package org.eclipse.tigerstripe.workbench.internal.core.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;

public class TigerstripeLogProgressMonitor implements
		IProgressMonitor {

	public void beginTask(String name, int totalWork) {
		TigerstripeRuntime.logInfoMessage(name);
	}

	public void done() {
		// TODO Auto-generated method stub

	}

	public void internalWorked(double work) {
		// TODO Auto-generated method stub

	}

	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCanceled(boolean value) {
		// TODO Auto-generated method stub

	}

	public void setTaskName(String name) {
		// TODO Auto-generated method stub

	}

	public void subTask(String name) {
		// TODO Auto-generated method stub

	}

	public void worked(int work) {
		// TODO Auto-generated method stub

	}

}
