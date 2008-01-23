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
package org.eclipse.tigerstripe.api.publish;

import org.eclipse.tigerstripe.api.TigerstripeException;

public interface IProjectPublisher {

	/**
	 * Return true if necessary information is set in the project descriptor to
	 * perform publish.
	 * 
	 * @return
	 */
	public boolean isPublishable();

	public void publish() throws TigerstripeException;

	public boolean generateBeforePublish();

	public boolean clearBeforePublish();

	public void clearPublishDirectory();

	public String getPublishDirectory();
}
