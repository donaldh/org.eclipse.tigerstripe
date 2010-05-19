/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.CompositeSelectionFilter;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionConverter;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Yuri Strot
 * 
 */
public class AnnotationSelectionListener implements ISelectionListener {

	private List<ISelectionListener> listeners = new ArrayList<ISelectionListener>();
	private ISelectionConverter[] converters;
	private ISelection selection;

	private CompositeSelectionFilter filter;

	public AnnotationSelectionListener(ISelectionConverter[] converters) {
		this.converters = converters;
		filter = new CompositeSelectionFilter();
	}

	public CompositeSelectionFilter getFilter() {
		return filter;
	}

	public void addListener(ISelectionListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ISelectionListener listener) {
		listeners.remove(listener);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!filter.select(part, selection))
			return;
		this.selection = convertSelection(part, selection);
		for (ISelectionListener listener : listeners) {
			listener.selectionChanged(part, this.selection);
		}
	}

	public ISelection getSelection() {
		return selection;
	}

	public ISelection convertSelection(IWorkbenchPart part, ISelection selection) {
		for (int i = 0; i < converters.length; i++) {
			try {
				ISelection newSelection = converters[i]
						.convert(part, selection);
				if (newSelection != null)
					selection = newSelection;
			} catch (Exception e) {
				AnnotationUIPlugin.log(e);
			}
		}
		return selection;
	}

}