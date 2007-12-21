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
package org.eclipse.tigerstripe.core.model.importing.mapper;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * This exception is thrown when an exception occurs during a mapping operation
 * 
 * @author Eric Dillon
 * 
 */
public class MappingException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1791904747394326787L;

	public MappingException(String message, Exception e) {
		super(message, e);
		// TODO Auto-generated constructor stub
	}

	public MappingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
