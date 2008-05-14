/******************************************************************************* 
 * 
 * Copyright (c) 2008 Cisco Systems, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Cisco Systems, Inc. - dkeysell
********************************************************************************/

package org.eclipse.tigerstripe.plugins.xml;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactModel;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

public class XmlModel implements IArtifactModel{

	private IAbstractArtifact artifact;
	private IPluginConfig pluginRef;
	
	public void setIArtifact(IAbstractArtifact artifact ){
		this.artifact = artifact;
	}
	
	public void setPluginConfig(IPluginConfig newPluginRef) {
		this.pluginRef = newPluginRef;
}
	
	/**
	 * 	Returns the path which is the package name transformed into a path description.
	 * 
	 * @return String - the path to be used
	 */
	public String getOutPath(){
		return this.artifact.getPackage().replace(".", "/");
	}
	
}
