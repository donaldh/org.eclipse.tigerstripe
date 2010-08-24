/*******************************************************************************
 * Copyright (c) 2010 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model;

/**
 * Used to mark artifacts as dirty.  It's up to the client dealing with the artifact to determine
 * what it wants to do with a dirty artifact.
 * 
 * @author Navid Mehregani
 * 
 */
public interface IMarkDirty {
	
	/**
	 * Used to set dirty bit
	 * @param dirty: true if artifact is dirty; false otherwise
	 */
	public void setDirty(boolean dirty);
	
	/**
	 * @return True if artifact is dirty; false otherwise
	 */
	public boolean isDirty();

}
