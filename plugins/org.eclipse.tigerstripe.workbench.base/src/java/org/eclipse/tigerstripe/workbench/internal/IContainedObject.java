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
package org.eclipse.tigerstripe.workbench.internal;

public interface IContainedObject {

	/**
	 * The local state of this object will be reset to non-dirty as the
	 * container is set.
	 * 
	 * @param container
	 */
	public void setContainer(IContainerObject container);

	public boolean isDirty();

	public void clearDirty();
	
	public IContainerObject getContainer();
}
