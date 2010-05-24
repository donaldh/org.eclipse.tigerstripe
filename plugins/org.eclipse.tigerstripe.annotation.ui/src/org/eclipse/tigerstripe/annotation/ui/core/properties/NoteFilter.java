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
package org.eclipse.tigerstripe.annotation.ui.core.properties;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.annotation.ui.internal.view.property.TabDescriptionManipulator;

/**
 * Class determines if note section should be displayed
 * 
 * @author Yuri Strot
 */
public class NoteFilter implements IFilter {

	/**
	 * Determines if the given note passes this filter
	 * 
	 * @param note
	 *            specified note
	 * @return true if note section should be displayed for the specified note
	 *         and false otherwise
	 */
	public boolean select(INote note) {
		return note.getContent() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 */
	public final boolean select(Object toTest) {
		if (toTest instanceof INote) {
			return TabDescriptionManipulator.getInstance().isEnabled(this);
		}
		return false;
	}

}
