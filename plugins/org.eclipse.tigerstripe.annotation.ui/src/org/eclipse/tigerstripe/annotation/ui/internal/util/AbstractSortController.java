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
package org.eclipse.tigerstripe.annotation.ui.internal.util;

import java.util.Comparator;
import java.util.HashMap;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;

/**
 * @author Yuri Strot
 */
public abstract class AbstractSortController extends SelectionAdapter implements DisposeListener {

	private StructuredViewer viewer;
	private Item column;
	private Comparator<Object> columnComparator;
	private boolean state = true;
	
	private static HashMap<ViewerColumn, AbstractSortController> instantes = new HashMap<ViewerColumn, AbstractSortController>(); 
	
	private static class ViewerColumn {
		
		public ViewerColumn(Control control, Item column) {
			this.control = control;
			this.column = column;
		}
		
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj instanceof ViewerColumn) {
				ViewerColumn vc = (ViewerColumn)obj;
				return vc.column.equals(column) && vc.control.equals(control);
			}
			return false;
		}
		
		public int hashCode() {
			return control.hashCode() + column.hashCode();
		}
		
		private Control control;
		private Item column;
	}
	
	public static AbstractSortController getController(Control control, Item column) {
		return instantes.get(new ViewerColumn(control, column));
	}

	public AbstractSortController(StructuredViewer viewer, Item column, Comparator<Object> columnComparator, boolean defaultDesc) {
		instantes.put(new ViewerColumn(viewer.getControl(), column), this);
		this.viewer = viewer;
		this.column = column;
		this.columnComparator = columnComparator;
		this.state = defaultDesc;
		column.addDisposeListener(this);
	}

	public void widgetSelected(SelectionEvent e) {
		stateChanged();
		this.state = !state;
	}
	
	public void widgetDisposed(DisposeEvent e) {
		instantes.remove(new ViewerColumn(viewer.getControl(), column));
	}
	
	public void setState(boolean state) {
		stateChanged();
		this.state = !state;
	}
	
	private boolean getState() {
		return state;
	}
	
	protected StructuredViewer getViewer() {
		return viewer;
	}
	
	protected Item getColumn() {
		return column;
	}
	
	private ViewerSorter getViewerSorter() {
		final boolean state = getState();
		return new ViewerSorter() {

			public int compare(Viewer viewer, Object e1, Object e2) {
				int rev = state ? 1 : -1;
				return rev * columnComparator.compare(e1, e2);
			}

		};
	}
	
	protected int getSortDirection() {
		return getState() ? SWT.DOWN : SWT.UP;
	}
	
	public void initState() {
		state = getRealSortDirection() == SWT.DOWN;  
		update();
		this.state = !state;
	}
	
	protected void update() {
		getViewer().setSorter(getViewerSorter());
	}
	
	public abstract int getRealSortDirection();

	public abstract void stateChanged();

}
