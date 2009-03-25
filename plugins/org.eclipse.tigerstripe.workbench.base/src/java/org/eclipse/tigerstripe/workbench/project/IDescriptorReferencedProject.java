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
package org.eclipse.tigerstripe.workbench.project;

import org.eclipse.core.runtime.IPath;


/**
 * Interface for Referenced projects listed in project descriptor
 * 
 * @author dkeysell
 * 
 */

public interface IDescriptorReferencedProject {
	public void setProjectName(String name);
	public void setProject(ITigerstripeModelProject project);	
	public String getProjectName();
	public ITigerstripeModelProject getProject();
}
