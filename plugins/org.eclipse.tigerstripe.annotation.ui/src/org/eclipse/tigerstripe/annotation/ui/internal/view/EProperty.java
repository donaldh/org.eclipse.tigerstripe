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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yuri Strot
 *
 */
public class EProperty implements IProperty {
	
	private EObject object;
	private EStructuralFeature feature;
	
	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";
	private static final String ANNOTATION_MULTILINE = "multiline";
	
	private static final String CREATE = "Create";
	private static final String DESTROY = "Destroy";
	
	private CellEditor cellEditor;
	
	public EProperty(EObject object, EStructuralFeature feature) {
		this.object = object;
		this.feature = feature;
	}

	public void applyEditorValue() {
		if (cellEditor != null)
			setValue(cellEditor.getValue());
    }
	
	@SuppressWarnings("unchecked")
    public String getDisplayName() {
		if (feature.isMany()) {
			List<Object> list = (List<Object>)getValue();
			Iterator<Object> it = list.iterator();
			StringBuffer buffer = new StringBuffer();
			if (it.hasNext()) buffer.append(it.next());
			while (it.hasNext()) {
				Object elem = it.next();
				buffer.append(", ");
				buffer.append(elem);
			}
			return buffer.toString();
		}
		Object value = getValue();
		if (value == null)
			return "";
		return value.toString();
	}
	
	public EClassifier getEType() {
		return feature.getEType();
	}

	@SuppressWarnings("unchecked")
    public CellEditor getEditor(Composite parent) {
		if (feature.getName().equals("uri")) {
			cellEditor = null;
			return null;
		}
		EClassifier classifier = feature.getEType();
		Class<?> clazz = classifier.getInstanceClass();
		if (clazz.equals(boolean.class)) {
			cellEditor = createBooleanCellEditor(parent);
			return cellEditor;
		}
		if (feature.getEType() instanceof EEnum) {
			EEnum eEnum = (EEnum)feature.getEType();
			List<Object> enumerators = new ArrayList<Object>();
			for (EEnumLiteral eEnumLiteral :  eEnum.getELiterals()) {
				enumerators.add(eEnumLiteral.getInstance());
			}
			cellEditor = createComboBoxCellEditor(parent, enumerators);
			return cellEditor;
		}
		if (feature.getEType() instanceof EClass) {
			if (!feature.isMany()) {
				EObject value = (EObject)getValue();
				if (value == null) {
					List<Object> values = new ArrayList<Object>();
					values.add(CREATE);
					createComboBoxCellEditor(parent, values);
					cellEditor = createComboBoxCellEditor(parent, values);
					return cellEditor;
				}
				else {
					List<Object> values = new ArrayList<Object>();
					values.add(getDisplayName());
					values.add(DESTROY);
					createComboBoxCellEditor(parent, values);
					cellEditor = createComboBoxCellEditor(parent, values);
					return cellEditor;
				}
			}
		}
		if (feature.getEType() instanceof EDataType) {
			EDataType eDataType = (EDataType)feature.getEType();
			if (eDataType.getInstanceClass() == Date.class) {
				cellEditor = createDateTimeCellEditor(parent, feature, (Date)getValue());
				return cellEditor;
			}
			else {
				List<String> enumeration = ExtendedMetaData.INSTANCE.getEnumerationFacet(eDataType);
				if (!enumeration.isEmpty()) {
					List<Object> enumerators = new ArrayList<Object>();
					for (String enumerator : enumeration) {
						enumerators.add(EcoreUtil.createFromString(eDataType, enumerator));
					}
					cellEditor = createComboBoxCellEditor(parent, enumerators);
					return cellEditor;
				}
			}
		}
		if (feature.isMany()) {
			cellEditor = createDialogCellEditor(parent, feature, (List<String>)getValue());
			return cellEditor;
		}
		if (clazz.equals(String.class) || clazz.equals(int.class)) {
	    	EAnnotation annotation = feature.getEAnnotation(ANNOTATION_MARKER);
	    	if (annotation != null) {
				String value = annotation.getDetails().get(ANNOTATION_MULTILINE);
				if (value != null && Boolean.valueOf(value)) {
					cellEditor = new MultiLineCellEditor(parent, 
						(EDataType)classifier, feature.getName());
					return cellEditor;
				}
	    	}
			cellEditor = new TextCellEditor(parent);
		}
	    return cellEditor;
    }
	
	public CellEditor createDateTimeCellEditor(Composite composite,
			EStructuralFeature feature, Date value) {
		return new DateTimeCellEditor(composite, feature.getName(), value);
	}	
	
	public List<Enumerator> getEnums() {
		return null;
	}

	public String getName() {
	    return feature.getName();
    }

	public Object getValue() {
		return object.eGet(feature);
    }

	public void setValue(Object value) {
		if (feature.getEType().getInstanceClass().equals(int.class)) {
			if (value == null) value = new Integer(0);
			else {
				try {
					value = new Integer(Integer.parseInt(value.toString()));
				}
				catch (Exception e) {
					return;
				}
			}
		}
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
		object.eSet(feature, value);
		//AnnotationPlugin.getManager().save(annotation);
    }

    /*
     * @param composite @return
     */
    protected CellEditor createComboBoxCellEditor(Composite composite,
    		final List<Object> choiceOfValues) {

        return new ExtendedComboBoxCellEditor(composite, new ArrayList<Object>(
        		choiceOfValues), new DefaultLabelProvider(), true);

    }
    
    /**
	 * Creates a dialog cell editor for editing multivalued features.
	 * 
	 * @param composite
	 *            the composite to contain the new cell editor
	 * @param feature
	 *            the feature being edited
	 * @param choiceOfValues
	 *            the possible values for that feature
	 * @return the new cell editor
	 */
    protected CellEditor createDialogCellEditor(Composite composite,
			final EStructuralFeature feature, final List<String> values) {
        return new StringListCellEditor(composite, values, feature.getName());
	}

    /*
	 * @param composite @return
	 */
    protected CellEditor createBooleanCellEditor(Composite composite) {
        return new ExtendedComboBoxCellEditor(composite,
                Arrays.asList(new Object[] { Boolean.FALSE,
                          Boolean.TRUE }), new DefaultLabelProvider(), true);
    }

}
