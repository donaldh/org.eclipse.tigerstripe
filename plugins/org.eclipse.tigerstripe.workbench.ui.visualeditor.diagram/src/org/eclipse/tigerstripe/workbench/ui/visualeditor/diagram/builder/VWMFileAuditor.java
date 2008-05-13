/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.builder;


public class VWMFileAuditor extends DiagramFileAuditor{

	public String getMatchingExtension(){
		return "wvd";
	}
	
	public String getFileExtension() {
		return "vwm";
	}

}
