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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import org.eclipse.emf.common.command.AbstractCommand;

/**
 * Base command for all refresh commands
 * 
 * @author Eric Dillon
 * 
 */
public abstract class BaseCommand extends AbstractCommand {

	private boolean isInitialRefresh;

	public void setInitialRefresh(boolean isInitialRefresh) {
		this.isInitialRefresh = isInitialRefresh;
	}

	public boolean isInitialRefresh() {
		return this.isInitialRefresh;
	}
}
