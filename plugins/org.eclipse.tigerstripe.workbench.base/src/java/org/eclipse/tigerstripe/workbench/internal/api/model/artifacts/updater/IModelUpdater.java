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

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * An IModelUpdater is able to perform actions on a model that can be undone.
 * 
 * It takes IModelChangeRequests that will be applied onto a model.
 * 
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IModelUpdater {

	/**
	 * Handles the given Change request to perform the change on the model
	 * 
	 * @param request
	 * @throws TigerstripeException
	 */
	public void handleChangeRequest(IModelChangeRequest request)
			throws TigerstripeException;

	public IModelChangeRequestFactory getRequestFactory();

	public void addModelChangeListener(IModelChangeListener listener);

	public void removeModelChangeListener(IModelChangeListener listener);

	public IArtifactManagerSession getSession();
}
