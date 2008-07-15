/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - jworrell
 *******************************************************************************//**
 * 
 */
package org.eclipse.tigerstripe.workbench.internal.core.model;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * @author jworrell
 *
 */
public class InvalidAnnotationTargetException extends TigerstripeException {

	public InvalidAnnotationTargetException(String message, Exception e) {
		super("Annotation target is invalid: "+message, e);
	}

	public InvalidAnnotationTargetException(String message) {
		super("Annotation target is invalid: "+message);
	}

}
