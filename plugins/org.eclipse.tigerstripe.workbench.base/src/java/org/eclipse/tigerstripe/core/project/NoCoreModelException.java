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
package org.eclipse.tigerstripe.core.project;

import org.eclipse.tigerstripe.api.TigerstripeException;

/**
 * This exception is thrown to signal that the core model
 * (IDependency.DEFAULT_CORE_MODEL_DEPENDENCY) is not marked as a dependency to
 * a project.
 * 
 * @author Eric Dillon
 * @since 1.0.3
 */
public class NoCoreModelException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7092362146255321671L;

	public NoCoreModelException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoCoreModelException(String message, Exception e) {
		super(message, e);
		// TODO Auto-generated constructor stub

	}

}
