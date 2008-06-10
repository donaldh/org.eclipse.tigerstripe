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
package org.eclipse.tigerstripe.generators.utils;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactFilter;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

/**
 * This can be used to prevent the oss/j legacy artifacts from appearing
 *
 */

public class LegacyFilter implements IArtifactFilter {

	public boolean select(IAbstractArtifact artifact) {
		
		if (artifact.getPackage().startsWith("java")){
			PluginLog.logDebug("Filtered  "+artifact.getName());
			return false;
		} else {
			PluginLog.logDebug("Passed  "+artifact.getName());
			return true;
		}
	}
	
	

}
