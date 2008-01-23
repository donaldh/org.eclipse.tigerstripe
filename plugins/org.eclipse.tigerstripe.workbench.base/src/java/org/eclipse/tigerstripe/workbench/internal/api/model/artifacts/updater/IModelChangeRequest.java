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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater;

/**
 * Top Level interface for model change requests
 * 
 * @author Eric Dillon
 * 
 */
public interface IModelChangeRequest {

	/**
	 * Returns the source of the request if known. null otherwise;
	 * 
	 * @return
	 */
	public Object getSource();

	/**
	 * Sets the source of the request
	 * 
	 * @param source
	 */
	public void setSource(Object source);
}
