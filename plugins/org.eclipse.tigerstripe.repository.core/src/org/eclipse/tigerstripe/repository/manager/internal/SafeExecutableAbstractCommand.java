/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.manager.internal;

import org.eclipse.emf.common.command.AbstractCommand;

public abstract class SafeExecutableAbstractCommand extends AbstractCommand {

	private Exception e;

	@Override
	public boolean canExecute() {
		return true;
	}

	public void execute() {
		try {
			run();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void redo() {
		try {
			reRun();
		} catch (Exception e) {
			handleException(e);
		}
	}

	protected abstract void run() throws Exception;

	protected void reRun() throws Exception {
		run();
	}

	protected void handleException(Exception e) {
		this.e = e;
	}

	public Exception getException() {
		return e;
	}

	public boolean successful() {
		return e == null;
	}
}
