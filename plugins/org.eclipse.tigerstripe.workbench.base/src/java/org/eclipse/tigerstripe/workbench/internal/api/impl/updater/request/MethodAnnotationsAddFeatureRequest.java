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
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodAnnotationsAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodChangeRequest;

/**
 * We have to subclass this to allow for keeping the target up to date
 * as the method label changes
 * 
 * @author rcraddoc
 *
 */
public class MethodAnnotationsAddFeatureRequest extends
		AnnotationAddFeatureRequest implements IMethodChangeRequest, IMethodAnnotationsAddFeatureRequest {

	private String afterChange;
	
	public String getMethodLabelAfterChange() {
		return this.afterChange;
	}

	public void setMethodLabelBeforeChange(String methodLabel) {
		setTarget(methodLabel);
		// This will not change the label...
		this.afterChange = methodLabel;
		
	}

}
