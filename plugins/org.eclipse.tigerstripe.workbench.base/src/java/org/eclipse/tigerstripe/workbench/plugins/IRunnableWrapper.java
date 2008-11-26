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

public interface IRunnableWrapper {

	/**
	 * Set the context object from which the rule execution "environment"
	 * can be extracted - this will be the collections etc 
	 * Equivalent to the velocity context.
	 *  
	 *  In the case of an Artifact rule, this should include "artifact" 
	 *  
	 * @param context
	 */
	public void setContext(Map<String, Object> context);
	
	/**
	 * Implementations should contain the "body" here.
	 * @throws Exception
	 */
	public void run() throws Exception;
	
}
