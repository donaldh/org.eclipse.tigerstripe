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
package org.eclipse.tigerstripe.annotation.ui.core.view;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.ui.internal.view.property.DirtyListener;

public class NoteLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof INote) {
			INote note = (INote) element;
			StringBuilder builder = new StringBuilder();
			if (isDirty(note)) {
				builder.append('*');
			}
			builder.append(note.getLabel());
			return builder.toString();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof INote) {
			INote note = (INote) element;
			return note.getImage();
		}
		return super.getImage(element);
	}

	private boolean isDirty(INote note) {
		INoteChangeListener[] listeners = note.getListeners();
		if (listeners != null) {
			for (INoteChangeListener listener : listeners) {
				if (listener instanceof DirtyListener) {
					return ((DirtyListener) listener).isDirty();
				}
			}
		}
		return false;
	}

}
