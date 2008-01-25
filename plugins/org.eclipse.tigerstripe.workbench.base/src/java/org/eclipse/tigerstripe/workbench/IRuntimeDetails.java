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
package org.eclipse.tigerstripe.workbench;

import org.osgi.framework.Constants;

/**
 * This interface provides a simple way to get details about the tigerstripe
 * runtime.
 * 
 * 
 * @author erdillon
 * 
 */
public interface IRuntimeDetails {

	/**
	 * Returns the Tigerstripe build ID that is running
	 * Valid values are {@link Constants}
	 * @return
	 */
	public String getBaseBundleValue(String key);
}
