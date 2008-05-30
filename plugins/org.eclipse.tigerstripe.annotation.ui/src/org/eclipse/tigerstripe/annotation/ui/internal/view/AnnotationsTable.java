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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.DelegateAction;
import org.eclipse.tigerstripe.annotation.ui.internal.util.TableSortController;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationsTable {
	
	private Table table;
	private TableViewer viewer;
	
	public AnnotationsTable() {
	}
	
	public Control createContent(Composite parent) {
		table = new Table(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.HIDE_SELECTION);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		viewer = new TableViewer(table);
		viewer.setLabelProvider(new AnnotationsLabelProvider());
		viewer.setContentProvider(new ArrayContentProvider());
		
		TableColumn typeColumn = new TableColumn(table, SWT.NONE);
		typeColumn.setText("Type");
		typeColumn.setToolTipText("Annotation Type");
		TableSortController.add(viewer, typeColumn, AnnotationComparator.TYPE, true);
		
		TableColumn uriColumn = new TableColumn(table, SWT.NONE);
		uriColumn.setText("URI");
		uriColumn.setToolTipText("Annotation URI");
		TableSortController.add(viewer, uriColumn, AnnotationComparator.URI, true);

        table.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                Rectangle area = table.getClientArea();
                TableColumn[] columns = table.getColumns();
                if (area.width > 0) {
                    columns[0].setWidth(area.width * 20 / 100);
                    columns[1].setWidth(area.width * 80 / 100 - 4);
                    table.removeControlListener(this);
                }
            }
        });

		return table;
	}
	
	public Control getControl() {
		return viewer.getControl();
	}
	
	public void addToDoublClick(final DelegateAction action) {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
		
			public void doubleClick(DoubleClickEvent event) {
				action.selectionChanged(action, viewer.getSelection());
				if (action.isEnabled())
					action.run();
			}
		
		});
	}
	
	public Viewer getViewer() {
		return viewer;
	}
	
	public void refresh() {
		viewer.refresh();
	}
	
	public void setInput(Annotation[] annotations) {
		viewer.setInput(annotations);
	}

}
