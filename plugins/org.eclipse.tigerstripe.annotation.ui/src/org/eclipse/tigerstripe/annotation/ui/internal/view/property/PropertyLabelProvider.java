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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;

/**
 * @author Yuri Strot
 * 
 */
public class PropertyLabelProvider extends LabelProvider implements
		ITableLabelProvider, IColorProvider {

	private boolean readOnly;

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof EProperty) {
			EProperty property = (EProperty) element;
			switch (columnIndex) {
			case 0:
				return property.getName();
			case 2:
				String description = property.getDescription();
				if (description == null) {
					return null;
				}
				return description.replaceAll("\n", " ");
			default:
				return property.getDisplayName();
			}
		}
		return null;
	}

	public Color getBackground(Object element) {
		return null;
	}

	public Color getForeground(Object element) {
		if (readOnly) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
		}
		return null;
	}

}
