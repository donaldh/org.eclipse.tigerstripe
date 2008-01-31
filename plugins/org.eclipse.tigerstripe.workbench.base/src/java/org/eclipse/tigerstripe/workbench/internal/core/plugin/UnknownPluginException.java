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
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;

/**
 * @author Eric Dillon
 * 
 * Thrown when a plugin ref cannot be resolved.
 */
public class UnknownPluginException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3377151236277856787L;
	private PluginConfig unresolvedRef;

	public UnknownPluginException(PluginConfig ref) {
		super(Messages
				.formatMessage(Messages.UNKNOWN_PLUGIN, ref.getPluginId()));
		this.unresolvedRef = ref;
	}

	public PluginConfig getUnresolvedRef() {
		return this.unresolvedRef;
	}
}