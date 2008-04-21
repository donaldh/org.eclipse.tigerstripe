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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Yuri Strot
 *
 */
public class TableSortController extends AbstractSortController {
	
	public static void add(TableViewer viewer, TableColumn column,
			Comparator<Object> columnComparator, boolean defaultDesc) {
		new TableSortController(viewer, column, columnComparator, defaultDesc);
	}

	/**
	 * @param viewer
	 * @param column
	 * @param columnComparator
	 * @param defaultDesc - true, if default sorting whould be from
	 *  the maximum value to the minimum value 
	 */
	private TableSortController(TableViewer viewer, TableColumn column,
			Comparator<Object> columnComparator, boolean defaultDesc) {
		super(viewer, column, columnComparator, defaultDesc);
		column.addSelectionListener(this);
	}

	public void stateChanged() {
		TableViewer viewer = (TableViewer)getViewer();
		viewer.getTable().setSortColumn((TableColumn)getColumn());
		viewer.getTable().setSortDirection(getSortDirection());
		update();
	}
	
	public int getRealSortDirection() {
		return ((TableViewer)getViewer()).getTable().getSortDirection();
	}

}