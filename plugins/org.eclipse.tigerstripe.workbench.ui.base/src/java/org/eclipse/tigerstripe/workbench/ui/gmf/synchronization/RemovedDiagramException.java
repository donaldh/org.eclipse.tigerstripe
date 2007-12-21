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
package org.eclipse.tigerstripe.workbench.ui.gmf.synchronization;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * This Exception is only raised when trying to get a synchronizer on a
 * DiagramHandle that references a diagram that has been deleted.
 * 
 * @author erdillon
 * 
 */
public class RemovedDiagramException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6284306694869257924L;

	public RemovedDiagramException(String message, Exception e) {
		super(message, e);
		// TODO Auto-generated constructor stub
	}

	public RemovedDiagramException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
