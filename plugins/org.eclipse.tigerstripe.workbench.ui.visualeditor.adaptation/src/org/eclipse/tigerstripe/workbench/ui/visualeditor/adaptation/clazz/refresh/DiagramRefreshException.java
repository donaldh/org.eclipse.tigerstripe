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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * This exception denotes a problem while refreshing a Tigerstripe Diagram for a
 * change in the TS model.
 * 
 * @author Eric Dillon
 * 
 */
public class DiagramRefreshException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6797946231893651503L;

	public DiagramRefreshException(String message, Exception e) {
		super(message, e);
	}

	public DiagramRefreshException(String message) {
		super(message);
	}

}
