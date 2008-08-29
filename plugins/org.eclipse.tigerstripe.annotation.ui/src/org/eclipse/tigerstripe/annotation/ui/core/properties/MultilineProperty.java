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

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.MultiLineCellEditor;

/**
 * @author Yuri Strot
 *
 */
public class MultilineProperty extends AnnotatedProperty {
	
	public static final String ANNOTATION_MULTILINE = "multiline";

	/**
	 * @param object
	 * @param feature
	 */
	public MultilineProperty(IEditableValue value) {
		super(value);
	}
	
	public static boolean isCorrectValue(String value) {
		return value != null && Boolean.valueOf(value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotatedProperty#getDetailName()
	 */
	@Override
	protected String getDetailName() {
		return ANNOTATION_MULTILINE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotatedProperty#createEditor(org.eclipse.swt.widgets.Composite, java.lang.String)
	 */
	@Override
	protected CellEditor createEditor(Composite parent, String value) {
		if (isCorrectValue(value)) {
			return new MultiLineCellEditor(parent, 
				(EDataType)getEType(), getFeature().getName());
		}
		return null;
	}

}
