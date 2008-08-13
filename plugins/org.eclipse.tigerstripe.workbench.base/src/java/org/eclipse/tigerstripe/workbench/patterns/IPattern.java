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
package org.eclipse.tigerstripe.workbench.patterns;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IPattern {
	
	public String getName();
	
	public String getUILabel();
	
	public String getIconURL();
	
	public String getDescription();
	
}
