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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.OpenEditorCellEditor;

/**
 * @author Yuri Strot
 *
 */
public class EditorProperty extends AnnotatedProperty {
	
	public static final String ANNOTATION_EDITOR = "editor";

	/**
	 * @param object
	 * @param feature
	 */
	public EditorProperty(IEditableValue value) {
		super(value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotatedProperty#getDetailName()
	 */
	@Override
	protected String getDetailName() {
		return ANNOTATION_EDITOR;
	}
	
	public static boolean isCorrectValue(String value) {
		return value != null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotatedProperty#createEditor(org.eclipse.swt.widgets.Composite, java.lang.String)
	 */
	@Override
	protected CellEditor createEditor(Composite parent, String value) {
		if (isCorrectValue(value))
			return new OpenEditorCellEditor(parent, this, value);
		return null;
	}

}
