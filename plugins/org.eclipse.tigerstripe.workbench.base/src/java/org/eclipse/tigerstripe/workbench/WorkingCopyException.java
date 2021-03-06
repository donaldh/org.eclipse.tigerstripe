/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench;

public class WorkingCopyException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3021551580990014449L;

	public WorkingCopyException(String message) {
		super(message);
	}

	public WorkingCopyException(String message, Exception e) {
		super(message, e);
	}

}
