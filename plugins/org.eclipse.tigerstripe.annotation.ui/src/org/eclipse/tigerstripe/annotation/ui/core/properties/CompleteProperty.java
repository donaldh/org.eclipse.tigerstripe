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
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.CellEditorFactory;

/**
 * @author Yuri Strot
 *
 */
public class CompleteProperty extends AbstractProperty {
	
	private static final String CREATE = "Create";
	private static final String DESTROY = "Destroy";
	
	public CompleteProperty(EObject object, EStructuralFeature feature) {
		super(object, feature);
	}

	@SuppressWarnings("unchecked")
    public CellEditor createEditor(Composite parent) {
		Object value = getValue();
		if (feature.getEType() instanceof EEnum) {
			EEnum eEnum = (EEnum)feature.getEType();
			List<Object> enumerators = new ArrayList<Object>();
			for (EEnumLiteral eEnumLiteral :  eEnum.getELiterals()) {
				enumerators.add(eEnumLiteral.getInstance());
			}
			return CellEditorFactory.createComboBoxCellEditor(parent, enumerators);
		}
		if (feature.getEType() instanceof EClass) {
			if (!feature.isMany()) {
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
		}
		if (feature.getEType() instanceof EDataType) {
			EDataType eDataType = (EDataType)feature.getEType();
			if (Date.class.equals(eDataType.getInstanceClass())) {
				return CellEditorFactory.createDateTimeCellEditor(parent, feature, (Date)value);
			}
			else {
				List<String> enumeration = ExtendedMetaData.INSTANCE.getEnumerationFacet(eDataType);
				if (!enumeration.isEmpty()) {
					List<Object> enumerators = new ArrayList<Object>();
					for (String enumerator : enumeration) {
						enumerators.add(EcoreUtil.createFromString(eDataType, enumerator));
					}
					return CellEditorFactory.createComboBoxCellEditor(parent, enumerators);
				}
			}
		}
		if (feature.isMany() && value instanceof List<?>) {
			boolean allString = true;
			List<?> list = (List<?>)value;
			for (Object object : list) {
				if (!(object instanceof String)) {
					allString = false;
					break;
				}
			}
			if (allString) {
				return CellEditorFactory.createDialogCellEditor(parent, feature, (List<String>)value);
			}
		}
	    return null;
    }

	public void setValue(Object value) {
		if (feature.isMany()) {
			//ignore
		}
		else if (feature.getEType() instanceof EClass) {
			EClass clazz = (EClass)feature.getEType();
			if (CREATE.equals(value)) {
				value = clazz.getEPackage().getEFactoryInstance().create(clazz);
			}
			else if (DESTROY.equals(value)) {
				value = null;
			}
			else return;
		}
		if (value instanceof EDataTypeUniqueEList)
			return;
		super.setValue(value);
    }

}
