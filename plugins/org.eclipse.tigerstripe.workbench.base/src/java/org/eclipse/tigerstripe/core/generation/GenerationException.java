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
package org.eclipse.tigerstripe.core.generation;

import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;

public class GenerationException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -597020633833963651L;
	private IPluginReference pluginRef;

	public GenerationException(String message, IPluginReference pluginRef) {
		super(message);
		this.pluginRef = pluginRef;
	}

	public GenerationException(String message, IPluginReference pluginRef,
			Exception e) {
		super(message, e);
		this.pluginRef = pluginRef;
	}

	public IPluginReference getPluginRef() {
		return this.pluginRef;
	}
}
