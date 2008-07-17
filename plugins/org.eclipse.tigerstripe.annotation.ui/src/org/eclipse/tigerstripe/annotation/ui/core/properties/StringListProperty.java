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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.CellEditorFactory;

/**
 * @author Yuri Strot
 *
 */
public class StringListProperty extends EPropertyImpl {

	/**
	 * @param object
	 * @param feature
	 */
	public StringListProperty(EObject object, EStructuralFeature feature) {
		super(object, feature);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AbstractProperty#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected CellEditor createEditor(Composite parent) {
		return CellEditorFactory.createDialogCellEditor(parent, feature, (List<String>)getValue());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AbstractProperty#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		if (value instanceof EDataTypeUniqueEList)
			return;
		super.setValue(value);
	}

}
