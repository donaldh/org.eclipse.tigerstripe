/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ChangeIdLazyObject implements ILazyObject {
	private final ITigerstripeModelProject project;

	public ChangeIdLazyObject(ITigerstripeModelProject project) {
		this.project = project;
	}

	public Object getObject() {
		return project;
	}
}