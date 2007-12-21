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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * This exception denotes a problem while refreshing a Tigerstripe Diagram for a
 * change in the TS model.
 * 
 * @author Eric Dillon
 * 
 */
public class InstanceDiagramRefreshException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7932088027745536643L;

	public InstanceDiagramRefreshException(String message, Exception e) {
		super(message, e);
	}

	public InstanceDiagramRefreshException(String message) {
		super(message);
	}

}
