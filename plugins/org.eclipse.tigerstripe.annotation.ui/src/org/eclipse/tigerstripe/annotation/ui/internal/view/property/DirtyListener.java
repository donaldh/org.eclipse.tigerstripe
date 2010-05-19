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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteChangeListener;

public abstract class DirtyListener implements INoteChangeListener {

	private INote note;
	private boolean dirty = false;

	public DirtyListener(INote note) {
		this.note = note;
		note.addChangeListener(this);
	}

	public void changed() {
		if (!dirty) {
			dirty = true;
			update();
		}
	}

	public void dispose() {
		note.removeChangeListener(this);
	}

	public INote getNote() {
		return note;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clear() {
		dirty = false;
		update();
	}

	protected abstract void update();

}
