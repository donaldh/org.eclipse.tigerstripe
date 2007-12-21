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

public class GenerationCanceledException extends GenerationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2174195954192075358L;

	public GenerationCanceledException(String message,
			IPluginReference pluginRef, Exception e) {
		super(message, pluginRef, e);
		// TODO Auto-generated constructor stub
	}

	public GenerationCanceledException(String message,
			IPluginReference pluginRef) {
		super(message, pluginRef);
		// TODO Auto-generated constructor stub
	}

}
