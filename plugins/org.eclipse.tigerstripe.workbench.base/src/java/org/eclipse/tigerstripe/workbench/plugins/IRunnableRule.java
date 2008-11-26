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
package org.eclipse.tigerstripe.workbench.plugins;

import java.util.Map;


public interface IRunnableRule extends IFileGeneratingRule {

	public void setContext(Map<String, Object> context);
	
	public Map<String, Object> getContext();
	
	public String getRunnableClassName();
	
	public void setRunnableClassName(String className);
	
}
