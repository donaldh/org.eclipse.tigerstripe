/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * If a model component object implements the interface than the object is from
 * a referenced module or project.
 */
public interface IContextProjectAware {

	public ITigerstripeModelProject getContextProject();

}
