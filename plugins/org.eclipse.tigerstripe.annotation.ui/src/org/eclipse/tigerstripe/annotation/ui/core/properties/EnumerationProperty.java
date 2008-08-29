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

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.CellEditorFactory;

/**
 * @author Yuri Strot
 *
 */
public class EnumerationProperty extends EPropertyImpl {

	/**
	 * @param object
	 * @param feature
	 */
	public EnumerationProperty(IEditableValue value) {
		super(value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.AbstractProperty#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected CellEditor createEditor(Composite parent) {
		EDataType type = (EDataType)getEType();
		List<String> enumeration = ExtendedMetaData.INSTANCE.getEnumerationFacet(type);
		if (!enumeration.isEmpty()) {
			List<Object> enumerators = new ArrayList<Object>();
			for (String enumerator : enumeration) {
				enumerators.add(EcoreUtil.createFromString(type, enumerator));
			}
			return CellEditorFactory.createComboBoxCellEditor(parent, enumerators);
		}
		if (type instanceof EEnum) {
			EEnum eEnum = (EEnum)type;
			List<Object> enumerators = new ArrayList<Object>();
			for (EEnumLiteral eEnumLiteral :  eEnum.getELiterals()) {
				enumerators.add(eEnumLiteral.getInstance());
			}
			return CellEditorFactory.createComboBoxCellEditor(parent, enumerators);
		}
		return null;
	}

}
