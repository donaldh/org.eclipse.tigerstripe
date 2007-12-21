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
package org.eclipse.tigerstripe.core.module;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * Signals an invalid Module.
 * 
 * @author Eric Dillon
 * 
 */
public class InvalidModuleException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3080327871529310974L;

	public InvalidModuleException(String message) {
		super(message);
	}

	public InvalidModuleException(String message, Exception e) {
		super(message, e);
	}

}
