/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.refactor.diagrams;

import org.eclipse.core.runtime.IPath;

/**
 * This interface captures changes to a diagram itself (ie. move, copy, delete,
 * create), but not to its content.
 * 
 * @author erdillon
 * 
 */
public interface IDiagramChangeDelta {

	public final static int UNKNOWN = -1;
	public final static int MOVE = 1;
	public final static int COPY = 2;
	public final static int CREATE = 3;
	public final static int DELETE = 4;

	public void setAffectedDiagramHandle(HeadlessDiagramHandle handle);

	/**
	 * The destination path for the diagram without file extensions (since they
	 * will be based on the handle itself)
	 * 
	 * @param path
	 */
	public void setNewPath(IPath path);

	/**
	 * Returns the type of delta
	 * 
	 * @return
	 */
	public int getType();
}
