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
package org.eclipse.tigerstripe.workbench;

/**
 * @author Eric Dillon
 * 
 * This is the base class for any Exception defined within the Tigerstripe(tm)
 * framework.
 * 
 */
public class TigerstripeException extends Exception {

	private static final long serialVersionUID = 7526472295622776147L;

	private String message;
	private Exception exception;

	@Override
	public String getMessage() {
		return this.message;
	}

	public Exception getException() {
		return this.exception;
	}

	public TigerstripeException(String message) {
		this(message, null);
	}

	public TigerstripeException(String message, Exception e) {
		this.message = message;
		this.exception = e;
	}

}
