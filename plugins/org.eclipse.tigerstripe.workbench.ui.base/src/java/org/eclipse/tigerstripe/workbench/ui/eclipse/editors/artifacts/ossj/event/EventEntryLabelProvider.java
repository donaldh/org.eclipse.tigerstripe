/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.event;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.core.model.EventDescriptorEntry;

public class EventEntryLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		EventDescriptorEntry entry = (EventDescriptorEntry) element;
		switch (columnIndex) {
		case EventDescriptorEntry.LABEL_COLUMN_INDEX:
			return entry.getLabel();
		case EventDescriptorEntry.TYPE_COLUMN_INDEX:
			return entry.getPrimitiveType();
		}
		return null;
	}

	public EventEntryLabelProvider() {
		super();
	}

}
