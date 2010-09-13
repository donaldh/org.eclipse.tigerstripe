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

/**
 * This interface is used to keep track of Tigerstripe Descriptors.  Used to manage the 
 * dirty state of a set of contained objects.
 * 
 * @author erdillon
 * 
 */
public interface IContainerObject {

	/**
	 * Allow contained objects to notify their container that they got dirty
	 * 
	 * @param contained
	 */
	public void notifyDirty(IContainedObject contained);

	/**
	 * Whether this container is dirty or not. A container can be dirty itself
	 * or dirty because a contained object notified being dirty.
	 * 
	 * @return
	 */
	public boolean isContainedDirty();

	public void clearDirtyOnContained();

}