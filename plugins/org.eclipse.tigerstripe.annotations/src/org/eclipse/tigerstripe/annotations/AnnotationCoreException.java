/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations;

public class AnnotationCoreException extends Exception {

	public AnnotationCoreException() {
		super();
	}

	public AnnotationCoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnnotationCoreException(String message) {
		super(message);
	}

	public AnnotationCoreException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6867636078001811590L;

}
