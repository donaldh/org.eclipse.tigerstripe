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

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * This exception signals that the core model attached to the current project
 * (IDependency.DEFAULT_CORE_MODEL_DEPENDENCY) is doesn't match the version that
 * is shipped with this instance of Tigerstripe.
 * 
 * This means that the project was created with another version of Tigerstripe
 * and another version of IDependency.DEFAULT_CORE_MODEL_DEPENDENCY had been
 * attached.
 * 
 * @author Eric Dillon
 * @since 1.0.3
 */
public class MismatchedCoreModelException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7538798956485660654L;

	public MismatchedCoreModelException(String message) {
		super(message);
	}

	public MismatchedCoreModelException(String message, Exception e) {
		super(message, e);
	}

}
