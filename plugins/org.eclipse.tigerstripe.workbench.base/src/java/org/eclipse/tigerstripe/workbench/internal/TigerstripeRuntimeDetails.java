/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal;

import java.util.Dictionary;

import org.eclipse.tigerstripe.workbench.IRuntimeDetails;
import org.osgi.framework.Bundle;

/**
 * Gathers all the runtime details for the current install of Tigerstripe
 * 
 * 
 * @author erdillon
 * 
 */
public class TigerstripeRuntimeDetails implements IRuntimeDetails {

	public final static IRuntimeDetails INSTANCE = new TigerstripeRuntimeDetails();

	private String featureID = null;

	private TigerstripeRuntimeDetails() {
		// lazy population of these details.
	}

	@SuppressWarnings( { "unchecked" })
	public String getBaseBundleValue(String key) {
		Bundle context = BasePlugin.getDefault().getBundle();
		Dictionary<String, String> headers = context.getHeaders();
		return headers.get(key);
	}

}
