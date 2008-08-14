/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;

/**
 * This class is used because 
 * Stereotypes may change method Labels
 * @author rcraddoc
 *
 */
public interface IMethodChangeRequest extends IModelChangeRequest {

	/**
	 * The method label to uniquely identify the target method BEFORE the change
	 * is applied (so the match can happen!).
	 * 
	 * @param methodLabel
	 */
	public void setMethodLabelBeforeChange(String methodLabel);
	
	public String getMethodLabelAfterChange();
	
}
