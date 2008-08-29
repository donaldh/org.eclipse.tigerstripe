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
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yuri Strot
 *
 */
public class PrimitiveProperty extends EPropertyImpl {

	/**
	 * @param object
	 * @param feature
	 */
	public PrimitiveProperty(IEditableValue value) {
		super(value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.AbstractProperty#getEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public CellEditor createEditor(Composite parent) {
		return new TextCellEditor(parent);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.AbstractProperty#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		EFactory factory = getEType().getEPackage().getEFactoryInstance();
		if (value == null) {
			value = this.value.getDefaultValue();
		}
		else {
			String text = value.toString();
			value = factory.createFromString((EDataType)getEType(), text);
		}
		super.setValue(value);
	}

}
