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
package org.eclipse.tigerstripe.annotation.ui.core.properties;

import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.CellEditorFactory;

/**
 * @author Yuri Strot
 *
 */
public class DateProperty extends EPropertyImpl {

	/**
	 * @param object
	 * @param feature
	 */
	public DateProperty(IEditableValue value) {
		super(value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AbstractProperty#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected CellEditor createEditor(Composite parent) {
		return CellEditorFactory.createDateTimeCellEditor(parent, value.getName(), (Date)getValue());
	}

}
