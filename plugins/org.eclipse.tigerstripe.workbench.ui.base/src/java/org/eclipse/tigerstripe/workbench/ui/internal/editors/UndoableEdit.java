/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors;

/**
 * 
 * @author erdillon
 * 
 */
public class UndoableEdit {

	public boolean canUndo() {
		return false;
	}

	public boolean canRedo() {
		return false;
	}

	public void undo() {

	}

	public void redo() {

	}
}
