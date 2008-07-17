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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.CellEditorFactory;

/**
 * @author Yuri Strot
 *
 */
public class EObjectProperty extends EPropertyImpl {
	
	private static final String CREATE = "Create";
	private static final String DESTROY = "Destroy";

	/**
	 * @param object
	 * @param feature
	 */
	public EObjectProperty(EObject object, EStructuralFeature feature) {
		super(object, feature);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AbstractProperty#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected CellEditor createEditor(Composite parent) {
		Object value = getValue();
		if (value == null) {
			List<Object> values = new ArrayList<Object>();
			values.add(CREATE);
			return CellEditorFactory.createComboBoxCellEditor(parent, values);
		}
		else {
			List<Object> values = new ArrayList<Object>();
			values.add(getDisplayName());
			values.add(DESTROY);
			return CellEditorFactory.createComboBoxCellEditor(parent, values);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AbstractProperty#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		EClass clazz = (EClass)feature.getEType();
		if (CREATE.equals(value))
			value = clazz.getEPackage().getEFactoryInstance().create(clazz);
		else if (DESTROY.equals(value))
			value = null;
		else return;
		super.setValue(value);
	}

}
