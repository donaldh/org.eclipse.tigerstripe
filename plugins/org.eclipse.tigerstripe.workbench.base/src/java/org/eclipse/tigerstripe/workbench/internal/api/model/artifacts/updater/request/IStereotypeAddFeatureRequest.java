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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public interface IStereotypeAddFeatureRequest extends IModelChangeRequest {

	public static String STEREOTYPE_FEATURE = "stereotype";
	public static String RETURN_STEREOTYPE_FEATURE = "returnStereotype";
	
	public enum ECapableClass{
		ARTIFACT,END,FIELD,LITERAL,METHOD,METHODRETURN, METHODARGUMENT;
	}
	
	public void setArtifactFQN(String artifactFQN);
	
	public void setCapableName(String capableName);
	
	/**
	 * This is only used in the case of METHODARGUMENT setting to identify the relevant argument.
	 * @param position
	 */
	public void setArgumentPosition(Integer position);
	
	public void setCapableClass(ECapableClass capableClass);

	public void setFeatureValue(IStereotypeInstance value);
}
