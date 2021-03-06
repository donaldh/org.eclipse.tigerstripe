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

/**
 * This interface is intended to be implemented by clients of the IWorkingCopy
 * interface to be notified when the original object has changed
 * 
 * @author erdillon
 * 
 */
public interface IOriginalChangeListener {

	public void originalChanged(OriginalChangeEvent changeEvent);
}
