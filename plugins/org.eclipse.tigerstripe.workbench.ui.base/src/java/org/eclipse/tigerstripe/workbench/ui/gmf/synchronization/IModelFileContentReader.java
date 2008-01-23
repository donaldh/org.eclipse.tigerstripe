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

import java.util.Set;

import org.eclipse.tigerstripe.api.TigerstripeException;

/**
 * A IModelFileContentReader allows to read Diagram specific model files and
 * extract various information about it.
 * 
 * Because it relies on diagram specific EMF code it needs to be implemented in
 * the corresponding GMF generated plugin and be registered as a
 * modelFileContentReader extension to be accessible from the ui.base plugin
 * 
 * @author Eric Dillon
 * 
 */
public interface IModelFileContentReader {

	/**
	 * Returns the model file extension that can be processed by this
	 * 
	 * @return
	 */
	public String getSupportedModelFileExtension();

	/**
	 * Returns a set of all the FQNs contained in the top level map
	 * 
	 * @param handle
	 * @return
	 * @throws TigerstripeException
	 */
	public Set<String> getFQNs(DiagramHandle handle)
			throws TigerstripeException;
}
