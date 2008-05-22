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
package org.eclipse.tigerstripe.repository.manager;

public class ModelCoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3490749903849298278L;

	public ModelCoreException() {
		// default constructor
	}

	public ModelCoreException(String message) {
		super(message);
	}

	public ModelCoreException(String message, Exception e) {
		super(message, e);
	}
}
