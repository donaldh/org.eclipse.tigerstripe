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
package org.eclipse.tigerstripe.workbench;

public interface IOriginalChangeEvent {

	public final static int ORIGINAL_CHANGED = 0;
	
	public final static int ORIGINAL_DELETED = 1;
	
	public int getChangeType();
}
