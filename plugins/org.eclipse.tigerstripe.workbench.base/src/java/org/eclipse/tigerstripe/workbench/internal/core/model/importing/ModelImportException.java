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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

public class ModelImportException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8449281642313909520L;

	public ModelImportException(String message, Exception e) {
		super(message, e);
		// TODO Auto-generated constructor stub
	}

	public ModelImportException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
